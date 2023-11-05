package org.lqh.home.mapper;

import org.apache.ibatis.annotations.*;
import org.lqh.home.entity.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/3
 **/
@Mapper
@Repository
public interface ShopMapper {
    @Insert("insert into t_shop(name,tel,registerTime,state,address,logo,admin_id)" +
            "values(#{name},#{tel},#{registerTime},#{state},#{address},#{logo},#{admin.id})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Shop shop);

    @Select("select * from t_shop")
    List<Shop> list();

    @Update("update t_shop set state=-1 where id=#{id}")
    void updateAdminId(Long id);

    @Delete("delete from t_shop where id=#{id}")
    int delete(Long id);

    @Update("update t_shop set name=#{name},tel=#{tel},state=#{state},address=#{address} where id=#{id}")
    void update(Shop shop);
}
