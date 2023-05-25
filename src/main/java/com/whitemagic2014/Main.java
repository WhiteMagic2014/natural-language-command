package com.whitemagic2014;

import com.whitemagic2014.command.impl.*;
import com.whitemagic2014.gpt.Gpt;
import com.whitemagic2014.gpt.MagicSdk;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Main {


    public static void main(String[] args) throws IOException {
        // 读取配置文件
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        // 实例化解析器
        Parser parser = new Parser();
        // 读取配置
        String proxyServer = (String) properties.get("proxyServer");
        String key = (String) properties.get("key");
        // 设置gpt调用器
        Gpt gptSender;
        if (StringUtils.isBlank(proxyServer)) {
            gptSender = new MagicSdk(key);
        } else {
            gptSender = new MagicSdk(proxyServer, key);
        }
        parser.registGptSender(gptSender);
        // 注册指令 如果在框架中，可以优化为注解扫描注册
        parser.registCommand(new MailV1());
        parser.registCommand(new MailV2());
        parser.registCommand(new MailV3());
        parser.registCommand(new RemindV1());
        parser.registCommand(new RemindV2());
        parser.registCommand(new RemindV3());
        // 命令行交互
        System.out.println("启动成功:\n" +
                "model1         - 传统指令\n" +
                "model2         - 自然语言参数指令\n" +
                "model3         - 自然语言指令\n" +
                "model3set      - 自然语言指令模式下设置对话性格\n" +
                "model3clear    - 自然语言指令模式下清除上下文\n" +
                "exit           - 退出");
        System.out.println();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String inLine = br.readLine();
            if ("model1".equals(inLine)) {
                parser.modelV1();
            } else if ("model2".equals(inLine)) {
                parser.modelV2();
            } else if ("model3".equals(inLine)) {
                parser.modelV3();
            } else if (inLine.startsWith("model3set")) {
                System.out.println(gptSender.setPersonality("nlc-magic", inLine.split(" ")[1]));
            } else if ("model3clear".equals(inLine)) {
                System.out.println(gptSender.clearLog("nlc-magic"));
            } else if ("exit".equals(inLine)) {
                System.exit(0);
            } else {
                System.out.println(parser.parse(inLine));
            }
            System.out.println();
        }

    }


}