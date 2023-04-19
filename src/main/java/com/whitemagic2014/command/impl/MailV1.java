package com.whitemagic2014.command.impl;

import com.whitemagic2014.beans.Result;
import com.whitemagic2014.command.CommandV1;


public class MailV1 implements CommandV1 {

    @Override
    public Result<String> handle(String params) {
        System.out.println("MailV1 执行" + " - " + params);
        return Result.success("success");
    }

    @Override
    public Boolean checkParams(String params) {
        // 期望参数
        // 收件人:xxx 内容:xxxx
        String[] temps = params.split(" ");
        return temps[0].trim().startsWith("收件人");
    }

    @Override
    public String head() {
        return "邮件";
    }
}
