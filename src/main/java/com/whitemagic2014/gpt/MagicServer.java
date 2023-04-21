package com.whitemagic2014.gpt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whitemagic2014.beans.GptTemplate;
import com.whitemagic2014.utils.HttpUtil;

import java.util.List;

public class MagicServer implements Gpt {

    // personal gpt server
    String magicServerUrl = "";

    @Override
    public String chat(List<GptTemplate> templates) {
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(templates));
        String result = HttpUtil.post(magicServerUrl, array);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getString("data");
    }


}
