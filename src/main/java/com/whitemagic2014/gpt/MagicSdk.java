package com.whitemagic2014.gpt;

import com.github.WhiteMagic2014.Gmp;
import com.github.WhiteMagic2014.gptApi.Chat.CreateChatCompletionRequest;
import com.github.WhiteMagic2014.gptApi.Chat.pojo.ChatMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MagicSdk implements Gpt {

    private Gmp gmp;

    private String server;
    private String key;

    public MagicSdk(String key) {
        this.key = key;
        gmp = new Gmp(key);
    }

    public MagicSdk(String server, String key) {
        this.server = server;
        this.key = key;
        gmp = new Gmp(server, key);
    }

    @Override
    public String originChat(List<ChatMessage> messages) {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest()
                .key(key)
                .temperature(0.2f)
                .maxTokens(500);
        if (StringUtils.isNotBlank(server)) {
            request.server(server);
        }
        for (ChatMessage msg : messages) {
            request.addMessage(msg.getRole(), msg.getContent());
        }
        String result = "";
        try {
            result = gmp.streamRequest(request);
//            result = request.sendForChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            return "很抱歉，出错了";
        }
        return result.trim();
    }

    @Override
    public String chat(String session, String prompt) {
        return gmp.chat(session, prompt);
    }

    @Override
    public String setPersonality(String session, String setting) {
        return gmp.setPersonality(session, setting);
    }

    @Override
    public String clearLog(String session) {
        return gmp.clearLog(session);
    }

    @Override
    public void addChatLog(String session, String user, String assistant) {
        gmp.addChatLog(session, user, assistant);
    }
}
