package com.whitemagic2014.gpt;


import com.github.WhiteMagic2014.gptApi.Chat.pojo.ChatMessage;

import java.util.List;

/**
 * gpt 调用器
 */
public interface Gpt {

    /**
     * origin open ai chat api
     *
     * @param templates
     * @return
     */
    String originChat(List<ChatMessage> messages);


    /**
     * really chat
     *
     * @param session
     * @param prompt
     * @return
     */
    String chat(String session, String prompt);


    /**
     * 性格设定
     *
     * @param session 对话session
     * @param setting 性格设定
     * @return
     */
    String setPersonality(String session, String setting);


    /**
     * 清除 session 上下文
     *
     * @param session
     * @return
     */
    String clearLog(String session);


    /**
     * 给 session 添加log
     *
     * @param session
     * @param user
     * @param assistant
     */
    void addChatLog(String session, String user, String assistant);

}
