package com.whitemagic2014.command.impl;


import com.whitemagic2014.beans.Result;
import com.whitemagic2014.command.Command;
import com.whitemagic2014.command.CommandV2;
import com.whitemagic2014.utils.DateFormatUtil;
import io.github.WhiteMagic2014.beans.GptMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailV2 implements CommandV2 {
    @Override
    public Result<String> handle(String params) {
        System.out.println("MailV2 执行" + " - " + params);
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

    @Override
    public List<GptMessage> gptTemplate() {
        List<GptMessage> voList = new ArrayList<>();
        voList.add(GptMessage.systemMessage("请将给出的内容归纳为以下格式: 收件人:xxx 内容:xxxx"));
        voList.add(GptMessage.userMessage("帮我给ammy写一封邮件，告诉她3天后我会去接她"));
        voList.add(GptMessage.assistantMessage("收件人:ammy 内容:亲爱的ammy,3天后我会在机场为你接机，期待与你的相见。"));
        voList.add(GptMessage.userMessage("当前时间为" + DateFormatUtil.sdfv4.format(new Date()) + ";" + Command.paramsPlaceholder));
        return voList;
    }
}
