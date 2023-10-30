package org.lqh.home.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.Department;
import org.lqh.home.service.impl.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/29
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
  public void testTreeJson() throws JsonProcessingException {
        List<Department> departments = departmentService.getDepartmentTreeData();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(departments);
        System.out.println(json);
  }













}
