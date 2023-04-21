package com.whitemagic2014.gpt;

import com.whitemagic2014.beans.GptTemplate;

import java.util.List;

/**
 * gpt 调用器
 */
public interface Gpt {

    String chat(List<GptTemplate> templates);

}
