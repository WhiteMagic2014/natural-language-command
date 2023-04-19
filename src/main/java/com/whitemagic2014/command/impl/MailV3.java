package com.whitemagic2014.command.impl;

import com.whitemagic2014.beans.AiTemplate;
import com.whitemagic2014.beans.Result;
import com.whitemagic2014.command.Command;
import com.whitemagic2014.command.CommandV3;
import com.whitemagic2014.utils.DateFormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailV3 implements CommandV3 {

    @Override
    public Result<String> handle(String params) {
        System.out.println("MailV3 执行" + " - " + params);
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
    public String intention() {
        return "邮件";
    }

    @Override
    public List<String> intentionDemos() {
        List<String> demos = new ArrayList<>();
        demos.add("帮我给ammy写一封邮件，告诉她3天后我会去接她");
        demos.add("写邮件给peter,让他3天后来参加ammy的欢迎会");
        return demos;
    }

    @Override
    public List<AiTemplate> aiTemplate() {
        List<AiTemplate> voList = new ArrayList<>();
        voList.add(new AiTemplate("system", "请将给出的内容归纳为以下格式: 收件人:xxx 内容:xxxx"));
        voList.add(new AiTemplate("user", "帮我给ammy写一封邮件，告诉她3天后我会去接她"));
        voList.add(new AiTemplate("assistant", "收件人:ammy 内容:亲爱的ammy,3天后我会在机场为你接机，期待与你的相见。"));
        voList.add(new AiTemplate("user", "当前时间为" + DateFormatUtil.sdfv4.format(new Date()) + ";" + Command.paramsPlaceholder));
        return voList;
    }
}
