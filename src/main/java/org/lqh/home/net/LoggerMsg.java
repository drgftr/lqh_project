package org.lqh.home.net;

import org.lqh.home.controller.UserController;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/17
 **/
public class LoggerMsg {
    public static void errorMsg(UnsupportedEncodingException e){
        LoggerFactory.getLogger(UserController.class).error(e.getMessage());
    }
}
