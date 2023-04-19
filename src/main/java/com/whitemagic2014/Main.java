package com.whitemagic2014;

import com.whitemagic2014.command.impl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Main {


    public static void main(String[] args) throws IOException {
        // 实例化解析器
        Parser parser = new Parser();

        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        parser.key((String) properties.get("key"));
        // 注册指令
        parser.registerCommand(new MailV1());
        parser.registerCommand(new MailV2());
        parser.registerCommand(new MailV3());
        parser.registerCommand(new RemindV1());
        parser.registerCommand(new RemindV2());
        parser.registerCommand(new RemindV3());


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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