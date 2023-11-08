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

    @Test
    public void testFindByUsername(){
        System.out.println(employeeMapper.findByUsername("丁真"));
    }

    @Test
    public void testResign(){
        employeeMapper.delete(316l);
    }

    @Test
    public void testFind(){
        System.out.println(employeeMapper.findIncumbency(317l)==null);
    }

    @Test
    public void testUpdate(){
        Employee employee = employeeMapper.findIncumbency(318l);
        employee.setPassword("123123");
        employee.setAge(123);
        employeeMapper.update(employee);
    }

    @Test
    public void testLogin(){
        Employee employee = new Employee();
        employee.setUsername("丁真1");
        employee.setPassword("666666");
//        System.out.println(employeeMapper.login(employee));
    }

    @Test
    public void testSelect(){
        System.out.println(employeeMapper.select("123","123123"));
    }
}
