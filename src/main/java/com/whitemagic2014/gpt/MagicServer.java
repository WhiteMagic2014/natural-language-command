package com.whitemagic2014.gpt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whitemagic2014.beans.GptTemplate;
import com.whitemagic2014.utils.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class MagicServer implements Gpt {

    // personal gpt server
    static String chat = "";
    static String clearLog = "";
    static String setPersonality = "";
    static String image = "";
    static String oriChat = "";
    static String embedding = "";


    @Override
    public String originChat(List<GptTemplate> templates) {
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(templates));
        String result = HttpUtil.post(oriChat, array);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getString("data");
    }


    @Override
    public String chat(String session, String prompt) {
        JSONObject param = new JSONObject();
        param.put("sessionKey", session);
        param.put("prompt", prompt);
        String result = HttpUtil.post(chat, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getString("data");
    }

    @Override
    public String setPersonality(String session, String setting) {
        JSONObject param = new JSONObject();
        param.put("sessionKey", session);
        param.put("prompt", setting);
        String result = HttpUtil.post(setPersonality, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getString("data");
    }

    @Override
    public String clearLog(String session) {
        JSONObject param = new JSONObject();
        param.put("sessionKey", session);
        String result = HttpUtil.post(clearLog, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getString("data");
    }


    @Override
    public List<String> image(String prompt, int n) {
        JSONObject param = new JSONObject();
        param.put("prompt", prompt);
        param.put("n", n);
        String result = HttpUtil.post(image, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return JSONObject.parseArray(jsonObject.getJSONArray("data").toJSONString(), String.class);
    }


    @Override
    public List<List<Double>> input2Vector(List<String> inputs) {
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(inputs));
        String tempResult = HttpUtil.post(embedding, array);
        JSONObject jsonObject = JSONObject.parseObject(tempResult);

        JSONArray data = jsonObject.getJSONArray("data");
        List<List<Double>> resultList = new ArrayList<>();
        for (Object object : data) {
            JSONArray innerArray = (JSONArray) object;
            List<Double> innerList = innerArray.toJavaList(Double.class);
            resultList.add(innerList);
        }
        return resultList;

    }
}
