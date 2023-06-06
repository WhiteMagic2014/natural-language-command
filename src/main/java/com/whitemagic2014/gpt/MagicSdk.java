package com.whitemagic2014.gpt;

import com.github.WhiteMagic2014.Gmp;
import com.github.WhiteMagic2014.gptApi.Chat.pojo.ChatMessage;

import java.util.List;

public class MagicSdk implements Gpt {

    private Gmp gmp;

    public MagicSdk(String key) {
        gmp = new Gmp(key);
    }

    public MagicSdk(String server, String key) {
        gmp = new Gmp(server, key);
    }

    @Override
    public String originChat(List<ChatMessage> messages) {
//        return gmp.originChat(messages, 500, true);
        return gmp.originChat(messages);
    }

    @Override
    public String chat(String session, String prompt) {
//        return gmp.chat(session, prompt, 500, true);
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
