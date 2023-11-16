package org.lqh.home.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.lqh.home.entity.User;
import org.lqh.home.net.param.RegisterParam;
import org.springframework.stereotype.Repository;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/9
 **/
@Mapper
@Repository
public interface UserMapper {
    @Insert("insert into t_user(username,phone,password,state,age,registertime)  " +
            "values(#{username},#{phone},#{password},#{state},#{age},#{registertime})")
    int add(RegisterParam registerParam);

    @Select("select * from t_user where phone=#{phone} and password=#{password}")
    User getUser(@Param("phone") String phone, @Param("password") String password);

    @Select("select * from t_user where phone=#{phone}")
    User selectPhone(String phone);

    @Select("select * from t_user where id=#{id}")
    User findById(long id);
}
