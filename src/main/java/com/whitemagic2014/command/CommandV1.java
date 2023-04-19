package com.whitemagic2014.command;

/**
 * @Description: 传统指令: 指令头 指令参数
 * @author: magic chen
 * @date: 2023/4/19 10:37
 **/
public interface CommandV1 extends Command {

    /**
     * 定义指令头
     *
     * @return
     */
    String head();

}
