package org.lqh.home.controller;

import org.lqh.home.entity.Department;
import org.lqh.home.entity.Employee;
import org.lqh.home.mapper.DepartmentMapper;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.service.IDepartmentService;
import org.lqh.home.service.IEmployeeService;
import org.lqh.home.utils.MD5Util;
import org.lqh.home.utils.ResultGenerator;
import org.lqh.home.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 雇员控制器
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private IDepartmentService iDepartmentService;
    private IEmployeeService iEmployeeService;

    @Autowired
    public EmployeeController(IDepartmentService iDepartmentService,IEmployeeService iEmployeeService){
        this.iDepartmentService = iDepartmentService;
        this.iEmployeeService = iEmployeeService;
    }

    @PostMapping("/add")
    public NetResult add(@RequestBody Employee employee){
        if (StringUtils.isEmpty(employee.getPhone())){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不能为空");
        }
        if (StringUtils.isEmpty(employee.getUsername())){
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID,"用户名不能为空");
        }
        if (StringUtils.isEmpty(employee.getPassword())){
            employee.setPassword(MD5Util.MD5Encode("123456","utf-8"));
        }
        if (StringUtils.isEmpty(employee.getEmail())){
            return ResultGenerator.genErrorResult(NetCode.EMAIL_INVALID,"邮箱不能为空");
        }
        Department department = iDepartmentService.find(employee.getDid());
        if(department==null){
            return ResultGenerator.genErrorResult(NetCode.DID_INVALID,"非法部门id");
        }

        boolean result = iEmployeeService.add(employee);
        if (!result){
            return ResultGenerator.genFailResult("添加失败");
        }
        return ResultGenerator.genSuccessResult(employee);
    }

    public void  delete(){

    }

    public void edit(){}

    public void find(){

    }


}
