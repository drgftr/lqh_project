package org.lqh.home.entity;

import lombok.Data;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@Data
public class Location {
    private double longitude;//经度
    private double latitude;//维度
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
