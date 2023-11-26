package org.lqh.home.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import org.lqh.home.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/20
 **/
@Mapper
@Repository
public interface OrderMapper {
    @Insert("insert into t_order(user_id,orderNo,status,createTime)" +
            "values(#{user_id},#{orderNo},#{status},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(Order d);

    @Update("update t_order set order cancelTime=#{cancelTime} where orderNo=#{orderNo}")
    int pay(String orderNo, Date cancelTime);
}
