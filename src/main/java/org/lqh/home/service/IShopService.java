package org.lqh.home.service;

import org.apache.ibatis.annotations.Param;
import org.lqh.home.entity.Shop;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/3
 **/
public interface IShopService {
    int add(Shop shop);

    List<Shop> list();

    void updateAdminId(Long id);

    int delete(Long id);

    void update(Shop shop);

    List<Shop> paginationList(@Param("offset") int offset, @Param("pageSize") int pageSize);

    int count();
}
