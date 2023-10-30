package org.lqh.home.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/26
 **/
@ApiModel(value = "部门对象",description = "新增部门参数对象")
@Data
public class Department {
    @ApiModelProperty(value = "部门id",required = true,example = "1")
    private Long id;

    @ApiModelProperty(value = "部门sn",required = true,example = "008")
    private String sn;

    @ApiModelProperty(value = "部门name",required = true,example = "研发部")
    private String name;

    private String dirPath;

    @ApiModelProperty(value = "部门状态",required = true,example = "0")
    private  int state;

    private Employee manager;

    private Long parentId;

    @ApiModelProperty(value = "部门上级",example = "{\"id\":3}")
    private Department parent;

    private List<Department> children = new ArrayList<>();

}
