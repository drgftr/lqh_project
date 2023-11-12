package org.lqh.home.net.param;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.DateFormat;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
public class Result {

    public static JSONObject StringToJson(String s){
        return JSON.parseObject(s);
    }
}
