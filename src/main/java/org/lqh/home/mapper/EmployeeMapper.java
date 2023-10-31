package org.lqh.home.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.lqh.home.entity.Employee;
import org.springframework.stereotype.Repository;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/31
 **/
@Mapper
@Repository
public interface EmployeeMapper {
    @Insert("insert into t_employee(did,username,email,phone,password,age,state)" +
            "values(#{did},#{username},#{email},#{phone},#{password},#{age},#{state})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Employee d);
}
