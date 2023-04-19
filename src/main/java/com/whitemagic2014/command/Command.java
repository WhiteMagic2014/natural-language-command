package com.whitemagic2014.command;

import com.whitemagic2014.beans.Result;

/**
 * @Description: 指令定义 接受参数 执行一个任务
 * @author: magic chen
 * @date: 2023/4/19 11:50
 **/
public interface Command {

    /**
     * aiTemplate中可能会用到的参数占位符
     */
    public static final String paramsPlaceholder = "${paramsPlaceholder}";

    /**
     * 获取参数执行指令
     *
     * @param params
     * @return
     */
    Result<String> handle(String params);


    /**
     * 参数合法检查
     *
     * @param params
     * @return
     */
    Boolean checkParams(String params);
}
