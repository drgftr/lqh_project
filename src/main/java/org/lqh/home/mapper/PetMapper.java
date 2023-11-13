package org.lqh.home.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.lqh.home.entity.Pet;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/13
 **/
@Mapper
@Repository
public interface PetMapper {

    //添加宠物类别
    @Insert("insert into pet(type,description) values(#{type},#{description})" )
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Pet pet);

    @Select("select * from pet")
    List<Pet> list();

    @Select("select * from pet where id=#{id}")
    Pet findById(long id);
}
