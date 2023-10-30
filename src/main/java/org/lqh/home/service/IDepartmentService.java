package org.lqh.home.service;

import org.lqh.home.common.DepartmentQuery;
import org.lqh.home.entity.Department;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/26
 **/
public interface IDepartmentService {
    void add(Department d);
    void remove(Long id);
    void update(Department d);
    Department find(Long id);
    List<Department> findAll();
    Long queryCount();
    List<Department> findDepartmentsByPage(DepartmentQuery query);
    List<Department> getDepartmentTreeData();
}









