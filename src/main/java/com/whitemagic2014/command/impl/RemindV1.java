package com.whitemagic2014.command.impl;


import com.whitemagic2014.beans.Result;
import com.whitemagic2014.command.CommandV1;
import com.whitemagic2014.utils.DateFormatUtil;

import java.text.ParseException;

public class RemindV1 implements CommandV1 {

    @Override
    public Result<String> handle(String params) {
        System.out.println("RemindV1 执行" + " - " + params);
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
    public String head() {
        return "备忘";
    }
}
