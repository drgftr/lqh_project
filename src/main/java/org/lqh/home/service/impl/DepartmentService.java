package org.lqh.home.service.impl;

import org.apache.ibatis.annotations.Many;
import org.lqh.home.common.DepartmentQuery;
import org.lqh.home.entity.Department;
import org.lqh.home.mapper.DepartmentMapper;
import org.lqh.home.service.IDepartmentService;
import org.lqh.home.utils.DepartmentTreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/26
 **/
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
@Service
public class DepartmentService implements IDepartmentService {


    private DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentService(DepartmentMapper departmentMapper){
        this.departmentMapper = departmentMapper;
    }

    @Transactional
    @Override
    public void add(Department d) {
        departmentMapper.add(d);
    }


    @Override
    public void remove(Long id) {
        departmentMapper.remove(id);
    }

    @Transactional
    @Override
    public void update(Department d) {
        departmentMapper.update(d);
    }

    @Transactional
    @Override
    public Department find(Long id) {
        return departmentMapper.find(id);
    }

    @Transactional
    @Override
    public List<Department> findAll() {
        return departmentMapper.findAll();
    }

    @Transactional
    @Override
    public Long queryCount() {
        return departmentMapper.queryCount();
    }

    @Transactional
    @Override
    public List<Department> findDepartmentsByPage(DepartmentQuery query) {
        return departmentMapper.findDepartmentsByPage(query);
    }

    @Override
    public List<Department> getDepartmentTreeData() {
        List<Department> departments = departmentMapper.findAll();
        return DepartmentTreeUtil.listToTree(departments);
    }

}












