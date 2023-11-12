package org.lqh.home.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lqh.home.entity.UserMsg;
import org.springframework.stereotype.Repository;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@Mapper
@Repository
public interface UserMsgMapper {
    @Insert("insert into user_msg(user_id,admin_id,shop_id,name,createtime,address,birth,price,isinoculation,sex,state) " +
            "values(#{userId},#{adminId},#{shopId},#{name},#{createtime},#{address},#{birth},#{price},#{isinoculation},#{sex},#{state})")
    int add(UserMsg userMsg);
}
