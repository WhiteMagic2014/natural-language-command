package com.whitemagic2014.command.impl;

import com.whitemagic2014.beans.GptTemplate;
import com.whitemagic2014.beans.Result;
import com.whitemagic2014.command.Command;
import com.whitemagic2014.command.CommandV3;
import com.whitemagic2014.utils.DateFormatUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RemindV3 implements CommandV3 {

    @Override
    public Result<String> handle(String params) {
        System.out.println("RemindV3 执行" + " - " + params);
        return Result.success("success");
    }

    @Override
    public Boolean checkParams(String params) {
        // 期望参数
        // yyyy-MM-dd/HH:mm:ss 内容
        String dateStr = params.substring(0, 19);  // 截取 yyyy-MM-dd/HH:mm:ss
        String msg = params.substring(20);// 截取后面的msg
        try {
            DateFormatUtil.sdfv3.parse(dateStr);
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    @Override
    public String intention() {
        return "备忘";
    }

    @Override
    public List<String> intentionDemos() {
        List<String> demos = new ArrayList<>();
        demos.add("3天后早上8点提醒我去机场接我的朋友ammy");
        demos.add("2023年05月10日是peter的生日，记得提前提醒我为他准备生日礼物");
        return demos;
    }

    @Override
    public List<GptTemplate> gptTemplate() {
        List<GptTemplate> voList = new ArrayList<>();
        voList.add(new GptTemplate("system", "请将给出的内容归纳为以下格式: yyyy-MM-dd/HH:mm:ss 内容"));
        voList.add(new GptTemplate("user", "当前时间为2023年04月13日11:26:02;3天后早上8点提醒我去机场接我的朋友ammy"));
        voList.add(new GptTemplate("assistant", "2023-04-16/08:00:00 记得去机场接你的朋友ammy"));
        voList.add(new GptTemplate("user", "当前时间为2023年04月13日11:26:02;2023年05月10日是peter的生日，记得提前提醒我为他准备生日礼物"));
        voList.add(new GptTemplate("assistant", "2023-05-07/00:00:00 距离peter的生日只剩下3天了，记得准备好生日礼物。"));
        voList.add(new GptTemplate("user", "当前时间为" + DateFormatUtil.sdfv4.format(new Date()) + ";" + Command.paramsPlaceholder));
        return voList;
    }
}
