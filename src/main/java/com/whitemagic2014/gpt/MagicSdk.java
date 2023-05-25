package com.whitemagic2014.gpt;

import io.github.WhiteMagic2014.Gmp;
import io.github.WhiteMagic2014.beans.Message;

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
    public String originChat(List<Message> templates) {
        return gmp.originChat(templates);
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

}
