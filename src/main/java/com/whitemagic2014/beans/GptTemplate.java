package com.whitemagic2014.beans;

/**
 * @Description: 调用gpt解析的模版
 * @author: magic chen
 * @date: 2023/4/19 11:05
 **/
public class GptTemplate {

    String role;//system,user,assistant

    String prompt;

    public GptTemplate(String role, String prompt) {
        this.role = role;
        this.prompt = prompt;
    }

    public GptTemplate() {
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

}
