package com.whitemagic2014;

import com.github.WhiteMagic2014.gptApi.Chat.CreateChatCompletionRequest;
import com.whitemagic2014.beans.AiTemplate;
import com.whitemagic2014.beans.Result;
import com.whitemagic2014.command.Command;
import com.whitemagic2014.command.CommandV1;
import com.whitemagic2014.command.CommandV2;
import com.whitemagic2014.command.CommandV3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 解析器
 * @author: magic chen
 * @date: 2023/4/19 11:39
 **/
public class Parser {


    private String key = "";

    public Parser key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 默认解析模式 传统指令
     */
    private int model = 1;

    private final Map<String, CommandV1> commandsV1 = new HashMap<>();
    private final Map<String, CommandV2> commandsV2 = new HashMap<>();
    private final Map<String, CommandV3> commandsV3 = new HashMap<>();
    public final Map<String, List<String>> v3Intentions = new HashMap<>();

    /**
     * 注册指令
     *
     * @param commands
     * @return
     */
    public void registerCommands(List<Command> commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }

    public void registerCommand(Command command) {
        // 分配指令监听组
        if (command instanceof CommandV1) {
            commandsV1.put(((CommandV1) command).head(), (CommandV1) command);
        } else if (command instanceof CommandV2) {
            commandsV2.put(((CommandV2) command).head(), (CommandV2) command);
        } else if (command instanceof CommandV3) {
            commandsV3.put(((CommandV3) command).intention(), (CommandV3) command);
            v3Intentions.put(((CommandV3) command).intention(), ((CommandV3) command).intentionDemos());
        }
    }

    /**
     * 解析模式 传统指令
     *
     * @return
     */
    public Parser modelV1() {
        this.model = 1;
        System.out.println("解析模式 传统指令");
        return this;
    }

    /**
     * 解析模式 自然语言参数指令
     *
     * @return
     */
    public Parser modelV2() {
        if ("".equals(key)) {
            System.out.println("未设置key");
        } else {
            this.model = 2;
            System.out.println("解析模式 自然语言参数指令");
        }
        return this;
    }

    /**
     * 解析模式 自然语言指令
     *
     * @return
     */
    public Parser modelV3() {
        if ("".equals(key)) {
            System.out.println("未设置key");
        } else {
            this.model = 3;
            System.out.println("解析模式 自然语言指令");
        }
        return this;
    }

    /**
     * 输入解析 ,根据不同解析模式处理
     *
     * @param args
     * @return
     */
    public Result<String> parse(String args) {
        switch (this.model) {
            case 1:
                return parseV1(args);
            case 2:
                return parseV2(args);
            case 3:
                return parseV3(args);
        }
        return Result.error("parse model set error");
    }


    private Result<String> parseV1(String args) {
        String head = args.split(" ")[0];
        if (!commandsV1.containsKey(head)) {
            return Result.error("未找到匹配的v1指令,指令头: " + head);
        }
        CommandV1 command = commandsV1.get(head);
        // 去除指令头的参数
        String params = args.replaceFirst(command.head() + " ", "");
        if (command.checkParams(params)) {
            return command.handle(params);
        }
        return Result.error("错误的参数: " + params);
    }

    private Result<String> parseV2(String args) {
        String head = args.split(" ")[0];
        if (!commandsV1.containsKey(head)) {
            return Result.error("未找到匹配的v2指令,指令头: " + head);
        }
        // 获得指令
        CommandV2 command = commandsV2.get(head);
        // 去除指令头的参数
        String params = args.replaceFirst(command.head() + " ", "");

        // 参数解析
        String analysedParams = paramAnalyze(command.aiTemplate(), params);
        if (command.checkParams(analysedParams)) {
            return command.handle(analysedParams);
        }
        return Result.error("错误的参数: " + analysedParams + "\n请优化ai解析模版");
    }

    private Result<String> parseV3(String args) {
        // 意向解析
        String intention = paramIntention(args);
        if (!commandsV3.containsKey(intention)) {
            return Result.error("未找到匹配的v3指令,解析意向: " + intention + "\n请检查该意向是否注册");
        }
        // 获得指令
        CommandV3 command = commandsV3.get(intention);
        // 参数解析
        String analysedParams = paramAnalyze(command.aiTemplate(), args);
        if (command.checkParams(analysedParams)) {
            return command.handle(analysedParams);
        }
        return Result.error("错误的参数: " + analysedParams + "\n请优化ai解析模版");
    }


    /**
     * 参数解析
     *
     * @param templates
     * @param param
     * @return
     */
    private String paramAnalyze(List<AiTemplate> templates, String param) {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest()
                .key(key)
                .maxTokens(500);
        for (AiTemplate tmp : templates) {
            request.addMessage(tmp.getRole(), tmp.getPrompt().replace(Command.paramsPlaceholder, param));
        }
        String result = "";
        try {
            result = request.sendForChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "很抱歉，出错了";
        }
        return result.trim();
    }

    /**
     * 意向解析
     *
     * @param param
     * @return
     */
    private String paramIntention(String param) {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest()
                .key(key)
                .maxTokens(500);
        String intentions = "[" + String.join(",", v3Intentions.keySet()) + "]";
        request.addMessage("system", "请将给出的内容按照以下类别分类" + intentions);
        for (String key : v3Intentions.keySet()) {
            List<String> demos = v3Intentions.get(key);
            for (String demo : demos) {
                request.addMessage("user", demo);
                request.addMessage("assistant", key);
            }
        }
        request.addMessage("user", param);
        String result = "";
        try {
            result = request.sendForChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "很抱歉，出错了";
        }
        return result;
    }

}
