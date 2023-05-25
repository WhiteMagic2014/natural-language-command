package com.whitemagic2014.gpt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.WhiteMagic2014.gptApi.Chat.CreateChatCompletionRequest;
import com.github.WhiteMagic2014.gptApi.Embeddings.CreateEmbeddingsRequest;
import com.github.WhiteMagic2014.gptApi.Images.CreateImageRequest;
import com.github.WhiteMagic2014.util.DefaultGptHttpUtil;
import com.github.WhiteMagic2014.util.GptHttpUtil;
import com.whitemagic2014.beans.ChatLog;
import com.whitemagic2014.beans.GptTemplate;

import java.util.*;

public class MagicSdk implements Gpt {


    private Map<String, Queue<ChatLog>> logs = new HashMap<>(); // 对话上下文
    private Map<String, String> personality = new HashMap<>(); //性格设定

    private int maxLog = 5; // 最大记忆层数

    private String server;

    private String key;

    public MagicSdk(String key) {
        this.key = key;
    }

    public MagicSdk(String server, String key) {
        this.server = server;
        this.key = key;
    }

    @Override
    public String originChat(List<GptTemplate> templates) {
        GptHttpUtil gptHttpUtil = new DefaultGptHttpUtil();

        System.out.println(JSON.toJSONString(templates));
        CreateChatCompletionRequest request = new CreateChatCompletionRequest()
                .server(server)
                .key(key)
                .maxTokens(500);
        for (GptTemplate tmp : templates) {
            request.addMessage(tmp.getRole(), tmp.getPrompt());
        }
        String result = "";
        try {
            result = request.sendForChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "很抱歉，出错了";
        }
        return result.trim();
    }


    @Override
    public String chat(String session, String prompt) {
        String personal = personality.getOrDefault(session, "与用户进行闲聊或娱乐性的对话，以改善用户体验。");
        // 构造初始请求
        CreateChatCompletionRequest request = new CreateChatCompletionRequest()
                .server(server)
                .key(key)
                .maxTokens(500)
                .addMessage("system", personal);
        // 拼接历史对话记录
        if (logs.containsKey(session)) {
            Queue<ChatLog> queue = logs.get(session);
            queue.forEach(l -> {
                request.addMessage("user", l.getUser());
                System.out.println(l.getUser());
                request.addMessage("assistant", l.getAssistant());
                System.out.println(l.getAssistant());
            });
        }
        request.addMessage("user", prompt);
        // 发送请求
        String result = "";
        try {
            result = request.sendForChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            try {
                JSONObject js = JSONObject.parseObject(e.getMessage());
                // 如果是长度超了。 遗忘一段记忆
                if (js.getJSONObject("error").getString("code").equals("context_length_exceeded")) {
                    if (logs.containsKey(session)) {
                        Queue<ChatLog> queue = logs.get(session);
                        queue.poll();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "很抱歉，出错了";
        }
        // 记忆上下文
        if (logs.containsKey(session)) {
            Queue<ChatLog> queue = logs.get(session);
            if (queue.size() > maxLog) {
                queue.poll();
            }
            queue.offer(new ChatLog(prompt, result));
        } else {
            Queue<ChatLog> queue = new LinkedList<>();
            queue.offer(new ChatLog(prompt, result));
            logs.put(session, queue);
        }
        return result;
    }


    @Override
    public String setPersonality(String session, String setting) {
        personality.put(session, setting);
        return "已经设定为: " + setting;
    }

    @Override
    public String clearLog(String session) {
        logs.remove(session);
        return "操作成功";
    }

    @Override
    public List<String> image(String prompt, int n) {
        JSONObject temp = null;
        try {
            temp = new CreateImageRequest()
                    .server(server)
                    .key(key)
                    .prompt(prompt)
                    .n(n)
                    .largeSize()
                    .send();
        } catch (Exception e) {
            return Collections.singletonList("出错了");
        }
        List<String> resultList = new ArrayList<>();
        JSONArray array = temp.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            resultList.add(jsonObject.getString("url"));
        }
        return resultList;
    }


    @Override
    public List<List<Double>> input2Vector(List<String> inputs) {
        CreateEmbeddingsRequest request = new CreateEmbeddingsRequest()
                .server(server)
                .key(key);
        if (inputs.size() == 1) {
            request.input(inputs.get(0));
        } else {
            String[] ins = new String[inputs.size()];
            inputs.toArray(ins);
            request.inputs(ins);
        }
        return request.sendForEmbeddings();
    }

}
