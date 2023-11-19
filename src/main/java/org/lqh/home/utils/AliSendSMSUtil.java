package org.lqh.home.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.lqh.home.net.param.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/19
 **/
public class AliSendSMSUtil {
    private  static  final Logger logger = LoggerFactory.getLogger(AliSendSMSUtil.class);
    public static final String sendSms(String code,String phone){
        String host = "https://dfsmsv2.market.alicloudapi.com";
        String path = "/data/send_sms_v2";
        String method = "POST";
        String appcode = "dd31c4a2f9014af5b66dd61889cfcfb0";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
//
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "TPL_0000");
        bodys.put("phone_number", phone);
        //bodys.put("phone_number",phone);
        HttpResponse response = null;
        try {
            response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        } catch (Exception e) {
           logger.error(e.getMessage());
        }
        if (response == null){
            return  null;
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                result = StringUtil.convertStreamToString(inputStream);
                logger.info(result);
                return  result;
            } catch (IOException e) {
                // 处理异常
                logger.error(e.getMessage());
                return  null;
            }
        }
        return  null;
    }

}
