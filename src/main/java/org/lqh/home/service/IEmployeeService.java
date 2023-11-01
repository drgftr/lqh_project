package org.lqh.home.service;

import org.lqh.home.entity.Employee;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/31
 **/
public interface IEmployeeService {
    boolean add(Employee employee);
    int resign(Long id);
    Employee findIncumbency(Long id);
    void update(Employee employee);
    Employee findById(Long id);
    List<Employee> findAll();
}
