package org.lqh.home.service.impl;

import org.lqh.home.entity.Shop;
import org.lqh.home.mapper.ShopMapper;
import org.lqh.home.service.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/3
 **/
@Service
public class ShopService implements IShopService {

    private ShopMapper shopMapper;

    @Autowired
    public ShopService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    @Override
    public int add(Shop shop) {
        return shopMapper.add(shop);
    }

    @Override
    public List<Shop> list() {
        return shopMapper.list();
    }

    @Override
    public void updateAdminId(Long id) {
        shopMapper.updateAdminId(id);
    }

    @Override
    public int delete(Long id) {
        return shopMapper.delete(id);
    }

    @Override
    public void update(Shop shop) {
        shopMapper.update(shop);
    }

    @Override
    public List<Shop> paginationList(int offset, int pageSize) {
        return shopMapper.paginationList(offset, pageSize);
    }

    @Override
    public int count() {
        return shopMapper.count();
    }

    @Override
    public Shop findById(long id) {
        return shopMapper.findById(id);
    }
}
