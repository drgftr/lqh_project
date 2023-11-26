package org.lqh.home.utils;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/20
 **/
@Data
public class ReturnT {
    private Integer code;
    private String mgs;
    private Object data;

    public ReturnT(){
        this.code = 200;
    }
    public ReturnT(Integer code, String mgs) {
        this.code = code;
        this.mgs = mgs;
    }
    public ReturnT(Integer code, String mgs, Object data) {
        this.code = code;
        this.mgs = mgs;
        this.data = data;
    }
    public static ReturnT getSuccess(){
        return  new ReturnT(200,"执行成功");
    }
    public static ReturnT getFail(String mgs){
        return  new ReturnT(400,mgs);
    }
}
