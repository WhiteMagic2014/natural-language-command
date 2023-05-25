package com.whitemagic2014.gpt;

import io.github.WhiteMagic2014.beans.Message;

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
    String originChat(List<Message> templates);


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

}
