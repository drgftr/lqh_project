package org.lqh.home.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.lqh.home.common.Constants;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/7
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    private RedisTemplate redisTemplate;

    public TokenInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        logger.info("请求来了" + request.getRequestURL().toString());
        //要求接口里面的请求头必须有token
        String token = request.getHeader("token");
        if (!StringUtil.isEmpty(token)) {
            //如果token有,那么就去redis拿到token 对应的用户信息
            Object obj = redisTemplate.opsForValue().get(token);
            if (obj == null) {
                writeRes(response, NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN, null);
                return false;
            } else {
                //更新token
                redisTemplate.opsForValue().set(token, obj, 30, TimeUnit.MINUTES);
                return true;
            }
        }
        writeRes(response, NetCode.TOKEN_NOT_EXIST, Constants.INVALID_REQUEST, null);
        return false;
    }

    private void writeRes(HttpServletResponse response, int code, String message, Object data) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        NetResult netResult = new NetResult();
        netResult.setResultCode(code);
        netResult.setMessage(message);
        if (data != null) {
            netResult.setData(data);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(netResult);
        writer.write(json);
        writer.flush();
        writer.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //在处理器处理请求完成后，返回ModelAndView之前执行。
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在DispatchServlet完全处理完请求后执行
    }
}
