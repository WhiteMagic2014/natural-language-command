package com.whitemagic2014.command;

import com.whitemagic2014.beans.GptTemplate;

import java.util.List;

/**
 * @Description: 自然语言指令: 自然语言
 * @author: magic chen
 * @date: 2023/4/19 11:43
 **/
public interface CommandV3 extends Command {

    /**
     * 意向标识
     *
     * @return
     */
    String intention();

    /**
     * 定义意向解析案例
     *
     * @return
     */
    List<String> intentionDemos();

    /**
     * 定义 gpt解析模版
     *
     * @return
     */
    List<GptTemplate> gptTemplate();

}
