package com.whitemagic2014.gpt;

import com.whitemagic2014.beans.GptTemplate;

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
    String originChat(List<GptTemplate> templates);


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
     * 作n副图
     *
     * @param prompt
     * @param n
     * @return
     */
    List<String> image(String prompt, int n);


    /**
     * 输入转向量
     *
     * @param inputs
     * @return
     */
    List<List<Double>> input2Vector(List<String> inputs);

}
