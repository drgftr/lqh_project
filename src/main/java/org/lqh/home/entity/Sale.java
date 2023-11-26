package org.lqh.home.entity;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/26
 **/
@Data
public class Sale {
    private long id;
    private String name; //商品名字
    private String resources; //商品来源
    private double salePrice; //销售价格
    private long offSaleTime; //下架时间
    private long onSaleTime; //上架时间
    private int state; //状态 0下架 1上架
    private long costPrice; //成本价
    private int saleCount; //销售量
}
