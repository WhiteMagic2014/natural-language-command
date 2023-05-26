package com.whitemagic2014.command;

import com.github.WhiteMagic2014.beans.GptMessage;

import java.util.List;

/**
 * @Description: 识别自然语言参数的指令: 指令头 自然语言
 * @author: magic chen
 * @date: 2023/4/19 10:56
 **/
public interface CommandV2 extends Command {

    /**
     * 定义 指令头
     *
     * @return
     */
    String head();


    /**
     * 定义 gpt解析模版
     *
     * @return
     */
    List<GptMessage> gptTemplate();

}
