package org.lqh.home.mapper;

import org.apache.ibatis.annotations.*;
import org.lqh.home.entity.Shop;
import org.lqh.home.entity.UserMsg;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@Mapper
@Repository
public interface UserMsgMapper {
    @Insert("insert into user_msg(user_id,pet_id,admin_id,shop_id,name,createtime,address,birth,price,isinoculation,sex,state) " +
            "values(#{user_id},#{pet_id},#{admin_id},#{shop_id},#{name},#{createtime},#{address},#{birth},#{price},#{isinoculation},#{sex},#{state})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(UserMsg userMsg);

    @Update("update user_msg set shop_id=#{shopId},admin_id=#{employeeId},pet_id=#{petId},user_id=#{user_id} " +
            "where id=#{userMsgId}")
    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("user_id")long user_id, @Param("userMsgId")long userMsgId);

    @Select("select * from user_msg where state=#{state} and admin_id=#{adminId}")
    List<UserMsg> getPetListByState(@Param("state") int state,@Param("adminId") long adminId);

    @Select("select * from user_msg where user_id=#{userId}")
    List<UserMsg> getUserList(long userId);

    @Select("select id from user_msg")
    List<Long> getAllId();

    @Update("update user_msg set state=1 where id=#{id}")
    int listings(long id);

    @Select("select * from user_msg where shop_id=#{shopId}")
    List<UserMsg> getShopList(long shopId);

    @Select("select shop_id from user_msg")
    List<Long> getAllShopId();

    @Select("select * from user_msg where id=#{id}")
    UserMsg findById(long id);
}
