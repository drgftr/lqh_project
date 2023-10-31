package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/31
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void  testAdd(){
        Employee employee = new Employee();
        employee.setAge(12);
        employee.setDid(0l);
        employee.setEmail("123@123");
        employee.setPassword("666666");
        employee.setPhone("12312341234");
        employee.setState(0);
        employee.setUsername("丁真");
        employeeMapper.add(employee);
        System.out.println(employee);
    }
}
