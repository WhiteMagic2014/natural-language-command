package com.whitemagic2014.gpt;

import com.github.WhiteMagic2014.gptApi.Chat.CreateChatCompletionRequest;
import com.whitemagic2014.beans.GptTemplate;

import java.util.List;

public class MagicSdk implements Gpt {

    private String key;

    public MagicSdk(String key) {
        this.key = key;
    }

    @Override
    public String chat(List<GptTemplate> templates) {
        CreateChatCompletionRequest request = new CreateChatCompletionRequest()
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
}
