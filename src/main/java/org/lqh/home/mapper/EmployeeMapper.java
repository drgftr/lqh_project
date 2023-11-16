package org.lqh.home.mapper;

import org.apache.ibatis.annotations.*;
import org.lqh.home.entity.Employee;
import org.lqh.home.net.param.LoginParam;
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
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(Employee d);

    @Delete("delete from t_employee where id=#{id}")
    int delete(Long id);

    @Select("select * from t_employee where username=#{username}")
    Employee findByUsername(String username);

    /**
     * 找在职人员的
     *
     * @param id
     * @return
     */
    @Select("select * from t_employee where id=#{id} and state=0")
    Employee findIncumbency(Long id);

    @Update("update t_employee set " +
            " username=#{username},email=#{email},phone=#{phone},password=#{password},age=#{age},state=#{state} " +
            " where id=#{id}")
    void update(Employee employee);

    @Select("select * from t_employee where id=#{id}")
    Employee findById(Long id);

    @Select("select * from t_employee")
    List<Employee> findAll();

    @Select("select * from t_employee where username=#{username} and password=#{password}")
    Employee login(LoginParam loginParam);

    @Select("select * from t_employee where phone=#{phone} and password=#{password}")
    Employee select(String phone, String password);

    @Select("select * from t_employee where phone=#{phone} and password=#{password}")
    Employee getAdmin(String phone, String password);
}
