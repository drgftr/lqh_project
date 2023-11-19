package org.lqh.home.utils;

/**存redis里面的key
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/19
 **/
public class RedisKeyUtil {
    public static   String getSMSRedisKey(String phone){
        return  "sms_" + phone;
    }

    public static String getTokenRedisKey(String phone) {
        return "token_" + phone;
    }
}
