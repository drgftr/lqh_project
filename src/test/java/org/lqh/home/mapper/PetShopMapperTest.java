package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.Employee;
import org.lqh.home.entity.PetShop;
import org.lqh.home.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/3
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PetShopMapperTest {

    @Autowired
    private PetShopMapper petShopMapper;

    @Test
    public void testAdd(){
        PetShop petShop = new PetShop();
        petShop.setAdmin_id(1);
        petShop.setCostPrice(111);
        petShop.setSellPrice(222);
        petShop.setShop_id(11);
        petShop.setName("小蓝");
        petShop.setUsermsg_id(1);
        petShop.setSellTime(System.currentTimeMillis());
        petShopMapper.add(petShop);
    }

    @Test
    public void testGetAllShopId(){
        System.out.println(petShopMapper.getAllShopId());
    }

    @Test
    public void testBuy(){
        System.out.println(petShopMapper.buy(1,11));
    }

}
