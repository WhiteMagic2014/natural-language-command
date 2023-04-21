package com.whitemagic2014;

import com.whitemagic2014.command.impl.*;
import com.whitemagic2014.gpt.MagicSdk;
import com.whitemagic2014.gpt.MagicServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Main {


    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        String enviroment = (String) properties.get("enviroment");

        // 实例化解析器
        Parser parser = new Parser();
        // 设置gpt调用器
        if ("local".equals(enviroment)) {
            System.out.println("MagicServer model");
            parser.registGptSender(new MagicServer());
        } else {
            System.out.println("MagicSdk model");
            parser.registGptSender(new MagicSdk());
        }

        // 注册指令
        parser.registCommand(new MailV1());
        parser.registCommand(new MailV2());
        parser.registCommand(new MailV3());
        parser.registCommand(new RemindV1());
        parser.registCommand(new RemindV2());
        parser.registCommand(new RemindV3());


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("启动成功:\nmodel1 - 传统指令\nmodel2 - 自然语言参数指令\nmodel3 - 自然语言指令\nexit   - 退出");
        while (true) {
            System.out.println();
            String inLine = br.readLine();
            if ("model1".equals(inLine)) {
                parser.modelV1();
            } else if ("model2".equals(inLine)) {
                parser.modelV2();
            } else if ("model3".equals(inLine)) {
                parser.modelV3();
            } else if ("exit".equals(inLine)) {
                System.exit(0);
            } else {
                System.out.println(parser.parse(inLine));
            }
        }
    }


}