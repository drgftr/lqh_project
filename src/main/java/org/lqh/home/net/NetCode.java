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
    /**
     * 验证id的
     */
    public static final int ID_INVALID = 0x17;
    /**
     * 修改人员
     */
    public static final int UPDATE_INVALID_ERROR = 0x18;
    /**
     * 店铺名不能空
     */
    public static final int SHOP_NAME_INVALID = 0x19;
    /**
     * 手机号不能空
     */
    public static final int PHONE_NAME_INVALID = 0x20;
    /**
     * logo不能空
     */
    public static final int LOGO_NAME_INVALID = 0x21;
    /**
     * 店铺地址不能空
     */
    public static final int ADDRESS_NAME_INVALID = 0x22;
    /**
     * token 不存在
     */
    public static final int TOKEN_NOT_EXIST = 0X23;

    /**
     * token过期
     */
    public static final int TOKEN_INVALID = 0X23;
    /**
     * 成年才能注册
     */
    public static final int AGE_INVALID = 0x24;

    /**
     * 宠物名不能空
     */
    public static final int PET_NAME_INVALID = 0x25;
    /**
     * 地址不能空
     */
    public static final int ADDRESS_INVALID = 0x26;
    /**
     * 性别不能空
     */
    public static final int SEX_INVALID = 0x27;
    /**
     * 接种不能<0
     */
    public static final int ISINOCULATION_INVALID =0x28 ;
    /**
     * 生日错误
     */
    public static final int BIRTH_INVALID = 0x29;
    /**
     * 没有这个id
     */
    public static final int ADMINID_INVALID = 0x30;
    /**
     * 验证码错误
     */
    public static final int CODE_ERROR = 0x31;
    /**
     * 账号密码错误
     */
    public static final int LOGIN_ERROR = 0x32;
    /**
     * 提交的宠物列表状态错误
     */
    public static final int PET_TYPE_ERROR = 0X33;
    /**
     * 用户没有发布任务
     */
    public static final int USER_LIST_IS_NULL = 0x34;
    /**
     * 添加数据库错误
     */
    public static final int RESULT_ERROR = 0X35;
    /**
     * 用户消息列表中没有这个宠物信息
     */
    public static final int LIST_IS_NULL = 0x36;
    /**
     * 钱不能<0
     */
    public static final int MONEY_ERROR = 0x37;
    /**
     * 修改数据库失败
     */
    public static final int LISTING_ERROR = 0x38;
    /**
     * 店铺还没上架宠物
     */
    public static final int SHOP_IS_NULL = 0X39;
    /**
     * 店铺上架宠物失败
     */
    public static final int PETSHOP_ADD_ERROR = 0x40;
    /**
     * 已上架的宠物不能再次上架
     */
    public static final int PETSTATE_ERROR = 0x41;
    /**
     * 宠物商店没有这个宠物信息
     */
    public static final int PETSHOP_ERROR = 0x42;
    /**
     * 宠物已经被买走了
     */
    public static final int PETSHOP_IS_BUY = 0x43;
    /**
     * 购买失败
     */
    public static final int BUY_ERROR = 0x44;
    /**
     * 传入参数错误
     */
    public static final int TYPE_ERROR = 0x45;
    /**
     * 传入的数据不是0 或 1
     */
    public static final int STATE_ERROR = 0x46;
    /**
     * 用户寻主添加数据库失败
     */
    public static final int ADD_ERROR = 0X47;
    /**
     * 没有店铺
     */
    public static final int SHOP_LIST_IS_NULL = 0x48;
}
