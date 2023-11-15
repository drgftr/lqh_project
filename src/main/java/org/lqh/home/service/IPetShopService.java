package org.lqh.home.service;

import org.apache.ibatis.annotations.Param;
import org.lqh.home.entity.PetShop;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/15
 **/
public interface IPetShopService {
    int add(PetShop petShop);
    List<Long> getAllShopId();
    List<PetShop> getShopList(long shopId);
    List<Long> getAllId();
    PetShop getPetById(long id);
    int buy(@Param("id") long id, @Param("userId") long userId);
}
