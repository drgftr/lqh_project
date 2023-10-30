package org.lqh.home.net.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.lqh.home.entity.Department;
import org.lqh.home.entity.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/27
 **/

@Data
public class DepartmentParam {


    private String sn;
    private String name;
    private String dirPath;
    private  int state;

    private long parentId;

}
