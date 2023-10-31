package org.lqh.home.service.impl;

import org.lqh.home.entity.Department;
import org.lqh.home.entity.Employee;
import org.lqh.home.mapper.DepartmentMapper;
import org.lqh.home.mapper.EmployeeMapper;
import org.lqh.home.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/31
 **/
@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeMapper employeeMapper;
    private DepartmentMapper departmentMapper;

    @Autowired
    public  EmployeeService(EmployeeMapper employeeMapper,DepartmentMapper departmentMapper){
        this.employeeMapper =employeeMapper;
        this.departmentMapper = departmentMapper;
    }
    @Override
    public boolean add(Employee employee) {
        int row = employeeMapper.add(employee);
        if (row==0){
           return false;
        }else {
            Department department = this.departmentMapper.find(employee.getDid());
            employee.setDepartment(department);
            return true;
        }
    }
}
