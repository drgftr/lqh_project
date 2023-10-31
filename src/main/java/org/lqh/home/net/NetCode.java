package org.lqh.home.net;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/27
 **/
public class NetCode {
    /**
     * 创建部门失败
     */
    public static final int CREATE_DEPARTMENT_ERROR = 0x10;

    /**
     * 移除部门失败
     */
    public static final int REMOVE_DEPARTMENT_ERROR = 0x11;

    /**
     * 更新部门失败
     */
    public static final int UPDATE_DEPARTMENT_ERROR = 0x12;
    /**
     * 手机号不能空
     */
    public static final int PHONE_INVALID = 0x13;
    /**
     * 用户名不能空
     */
    public static final int USERNAME_INVALID = 0X14;
    /**
     * 邮箱不能空
     */
    public static final int EMAIL_INVALID = 0x15;
    /**
     * 验证did的
     */
    public static final int DID_INVALID = 0X16;
}
