package org.lqh.home.service.impl;

import org.lqh.home.entity.PetShop;
import org.lqh.home.mapper.PetShopMapper;
import org.lqh.home.service.IPetShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/15
 **/
@Service
public class PetShopService implements IPetShopService {

    private PetShopMapper petShopMapper;

    @Autowired
    public PetShopService(PetShopMapper petShopMapper){
        this.petShopMapper = petShopMapper;
    }
    @Override
    public int add(PetShop petShop) {
        return petShopMapper.add(petShop);
    }

    @Override
    public List<Long> getAllShopId() {
        return petShopMapper.getAllShopId();
    }

    @Override
    public List<PetShop> getShopList(long shopId) {
        return petShopMapper.getShopList(shopId);
    }

    @Override
    public List<Long> getAllId() {
        return petShopMapper.getAllId();
    }

    @Override
    public PetShop getPetById(long id) {
        return petShopMapper.getPetById(id);
    }

    @Override
    public int buy(long id, long userId) {
        return petShopMapper.buy(id, userId);
    }
}
