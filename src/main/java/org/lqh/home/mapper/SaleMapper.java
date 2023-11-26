package org.lqh.home.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.lqh.home.entity.Sale;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/26
 **/
@Mapper
@Repository
public interface SaleMapper {

    @Update("update sale set state=0 , offSaleTime=#{offSaleTime} where id=#{id}")
    int offGoods(@Param("id") long id, @Param("offSaleTime") long offSaleTime);

    @Update("update sale set state=1 , onSaleTime=#{onSaleTime} where id=#{id}")
    int onGoods(@Param("id") long id, @Param("onSaleTime") long onSaleTime);

    @Select("select * from sale where id=#{id}")
    Sale selectById(long id);

    @Select("select count(*) from sale")
    int selectCount();

    @Select("select * from sale")
    List<Sale> selectSaleFindAll();

    @Update("update sale set saleCount=#{count} where id=#{id}")
    int settlement(@Param("count") int count,@Param("id") long id);
}
