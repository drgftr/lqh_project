package org.lqh.home.mapper;

import org.apache.ibatis.annotations.*;
import org.lqh.home.entity.PetShop;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/15
 **/
@Mapper
@Repository
public interface PetShopMapper {
    @Insert("insert into pet_shop(name,usermsg_id,admin_id,shop_id,sellTime,costPrice,sellPrice)" +
            "values(#{name},#{usermsg_id},#{admin_id},#{shop_id},#{sellTime},#{costPrice},#{sellPrice})")
    int add(PetShop petShop);

    @Update("update pet_shop set sellPrice=#{price} where id=#{id}")
    int updatePrice(@Param("id") long id, @Param("price") double price);

    @Select("select shop_id from pet_shop")
    List<Long> getAllShopId();

    @Select("select * from pet_shop where shop_id=#{shopId}")
    List<PetShop> getShopList(long shopId);

    @Select("select id from pet_shop")
    List<Long> getAllId();

    @Select("select * from pet_shop where id=#{id}")
    PetShop getPetById(long id);

    @Update("update pet_shop set state=1 , user_id=#{userId} where id=#{id} ")
    int buy(@Param("id") long id,@Param("userId") long userId);

}
