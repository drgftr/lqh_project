package org.lqh.home.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.lqh.home.entity.Shop;
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
    @Insert("insert into user_msg(user_id,pet_id,admin_id,shop_id,name,createtime,address,birth,price,isinoculation,sex,state) " +
            "values(#{userId},#{petId},#{adminId},#{shopId},#{name},#{createtime},#{address},#{birth},#{price},#{isinoculation},#{sex},#{state})")
    int add(UserMsg userMsg);

    @Update("update user_msg set shop_id=#{shopId},admin_id=#{employeeId},pet_id=#{petId},user_id=#{userId} " +
            "where id=#{userMsgId}")
    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("userId")long userId, @Param("userMsgId")long userMsgId);
}
