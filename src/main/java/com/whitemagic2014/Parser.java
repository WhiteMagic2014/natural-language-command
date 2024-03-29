package com.whitemagic2014;

import com.github.WhiteMagic2014.gptApi.Chat.pojo.ChatMessage;
import com.whitemagic2014.beans.Result;
import com.whitemagic2014.command.Command;
import com.whitemagic2014.command.CommandV1;
import com.whitemagic2014.command.CommandV2;
import com.whitemagic2014.command.CommandV3;
import com.whitemagic2014.gpt.Gpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 解析器
 * @author: magic chen
 * @date: 2023/4/19 11:39
 **/
public class Parser {


    /**
     * gpt 调用器
     */
    private Gpt gpt;

    /**
     * 注册 gpt 调用器
     *
     * @param gpt
     */
    public void registGptSender(Gpt gpt) {
        this.gpt = gpt;
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
    public void registCommands(List<Command> commands) {
        for (Command command : commands) {
            registCommand(command);
        }
    }

    public void registCommand(Command command) {
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
        if (gpt == null) {
            System.out.println("未设置gpt调用器");
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
        if (gpt == null) {
            System.out.println("未设置gpt调用器");
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
        String analysedParams = paramAnalyze(command.gptTemplate(), params);
        if (command.checkParams(analysedParams)) {
            return command.handle(analysedParams);
        }
        return Result.error("错误的参数: " + analysedParams + "\n请优化gpt解析模版");
    }

    private Result<String> parseV3(String args) {
        // 指令，交谈，提问
        if (!isCommand(args)) {
            // 并非指令 对话
            String resp = gpt.chat("nlc-magic", args);
            return Result.success(resp);
        }
        // 指令意向解析
        String intention = paramIntention(args);
        if (!commandsV3.containsKey(intention)) {
            return Result.error("未找到匹配的v3指令,解析意向: " + intention + "\n请检查该意向是否注册");
        } else {
            System.out.println("匹配指令意向: " + intention);
        }
        // 获得指令
        CommandV3 command = commandsV3.get(intention);
        // 参数解析
        String analysedParams = paramAnalyze(command.gptTemplate(), args);
        if (command.checkParams(analysedParams)) {
            Result<String> result = command.handle(analysedParams);
            if (result.isSuccess()) {
                gpt.addChatLog("nlc-magic", args, "好的");
            }
            return result;
        }
        return Result.error("错误的参数: " + analysedParams + "\n请优化gpt解析模版");
    }


    /**
     * 指令，交谈，提问  区分
     *
     * @param args
     */
    private boolean isCommand(String args) {
        List<ChatMessage> templates = new ArrayList<>();
        templates.add(ChatMessage.systemMessage("请将给出的内容按照以下类别分类[交谈，提问，指令]，请仅给出类别，不要附加任何他字符"));
        templates.add(ChatMessage.userMessage("3天后早上8点提醒我去超市买东西"));
        templates.add(ChatMessage.assistantMessage("指令"));
        templates.add(ChatMessage.userMessage("帮我给ammy写一封邮件，告诉她3天后我会去接她"));
        templates.add(ChatMessage.assistantMessage("指令"));
        templates.add(ChatMessage.userMessage("真是个好天气呢"));
        templates.add(ChatMessage.assistantMessage("交谈"));
        templates.add(ChatMessage.userMessage("mysql中主键和外键有什么区别"));
        templates.add(ChatMessage.assistantMessage("提问"));
        templates.add(ChatMessage.userMessage(args));
        String tmp = gpt.originChat(templates);
        System.out.println("prompt 分类: " + tmp);
        return tmp.trim().startsWith("指令");
    }


    /**
     * 参数解析
     *
     * @param messages
     * @param param
     * @return
     */
    private String paramAnalyze(List<ChatMessage> messages, String param) {
        // 占位符替换参数
        for (ChatMessage msg : messages) {
            msg.setContent(msg.getContent().replace(Command.paramsPlaceholder, param));
        }
        return gpt.originChat(messages);
    }

    /**
     * 意向解析
     *
     * @param param
     * @return
     */
    private String paramIntention(String param) {
        List<ChatMessage> messages = new ArrayList<>();
        String intentions = "[" + String.join(",", v3Intentions.keySet()) + "]";
        messages.add(ChatMessage.systemMessage("请将给出的内容按照以下类别分类" + intentions + " 请仅给出类别，不要附加任何他字符"));
        for (String key : v3Intentions.keySet()) {
            List<String> demos = v3Intentions.get(key);
            for (String demo : demos) {
                messages.add(ChatMessage.userMessage(demo));
                messages.add(ChatMessage.assistantMessage(key));
            }
        }
        messages.add(ChatMessage.userMessage(param + " （包含（告诉，通知）等字样并不代表是[交谈]）"));
        String resp = gpt.originChat(messages);
        return resp.split(" ")[0].split("\n")[0];
    }

}
