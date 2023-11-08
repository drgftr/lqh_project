package org.lqh.home.controller;

import org.lqh.home.common.Constants;
import org.lqh.home.entity.Department;
import org.lqh.home.entity.Employee;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.service.IDepartmentService;
import org.lqh.home.service.IEmployeeService;
import org.lqh.home.utils.MD5Util;
import org.lqh.home.utils.ResultGenerator;
import org.lqh.home.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 雇员控制器
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private IDepartmentService iDepartmentService;
    private IEmployeeService iEmployeeService;
    private RedisTemplate redisTemplate;

    @Autowired
    public EmployeeController(IDepartmentService iDepartmentService,IEmployeeService iEmployeeService,RedisTemplate redisTemplate){
        this.iDepartmentService = iDepartmentService;
        this.iEmployeeService = iEmployeeService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/add")
    public NetResult add(@RequestBody Employee employee){
        Employee employee1 = iEmployeeService.findByUsername(employee.getUsername());
        if(employee1!=null){
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID,"已有该名,不能继续添加");
        }
        if (StringUtils.isEmpty(employee.getPhone())){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if (StringUtils.isEmpty(employee.getUsername())){
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID,"用户名不能为空");
        }
        //没有密码默认123456，进行md5加密
        if (StringUtils.isEmpty(employee.getPassword())){
            employee.setPassword(MD5Util.MD5Encode("123456","utf-8"));
        }else {
            employee.setPassword(MD5Util.MD5Encode(employee.getPassword(),"utf-8"));
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

    @PostMapping("/delete")
    public NetResult delete(Long id){

        System.out.println("---------");
        System.out.println(id);
        int result = iEmployeeService.delete(id);
        if (result==1){
            return ResultGenerator.genSuccessResult(id+"删除成功");
        }
        return ResultGenerator.genSuccessResult(id+"删除失败");
    }

    @PostMapping("/edit")
    public NetResult edit(@RequestBody Employee employee){
        System.out.println("-------------");
        System.out.println(employee);
        try {
//            Employee employee1 = iEmployeeService.findById(employee.getId());
//            //密码要md5加密
            if (!StringUtils.isEmpty(employee.getPassword())){
                employee.setPassword(MD5Util.MD5Encode(employee.getPassword(),"utf-8"));
            }
//            if(StringUtils.isEmpty(employee.getUsername())){
//                employee.setUsername(employee1.getUsername());
//            }
//            if(StringUtils.isEmpty(employee.getEmail())){
//                employee.setEmail(employee1.getEmail());
//            }
//            if(StringUtils.isEmpty(employee.getPhone())){
//                employee.setPhone(employee1.getPhone());
//            }
//            if(employee.getAge()!=employee1.getAge()){
//                employee.setAge(employee1.getAge());
//            }
            iEmployeeService.update(employee);
            return ResultGenerator.genSuccessResult("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_INVALID_ERROR,"修改信息失败！"+e.getMessage());
        }
    }

    @GetMapping("/find")
    public NetResult find(Long id){
        Employee employee = iEmployeeService.findById(id);
        return  ResultGenerator.genSuccessResult(employee);
    }

    @GetMapping("/findall")
    public NetResult findAll(){
        List<Employee> employees = iEmployeeService.findAll();
        return ResultGenerator.genSuccessResult(employees);
    }




}
