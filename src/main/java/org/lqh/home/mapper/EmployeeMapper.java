package org.lqh.home.mapper;

import org.apache.ibatis.annotations.*;
import org.lqh.home.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Update("update t_employee set state=1 where id=#{id}")
    int resign(Long id);

    /**
     * 找在职人员的
     * @param id
     * @return
     */
    @Select("select * from t_employee where id=#{id} and state=0")
    Employee findIncumbency(Long id);

    @Update("update t_employee set " +
            " username=#{username},email=#{email},phone=#{phone},password=#{password},age=#{age} " +
            " where id=#{id}")
    void update(Employee employee);

    @Select("select * from t_employee where id=#{id}")
    Employee findById(Long id);

    @Select("select * from t_employee")
    List<Employee> findAll();
}
