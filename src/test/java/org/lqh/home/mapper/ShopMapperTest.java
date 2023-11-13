package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.Employee;
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
public class ShopMapperTest {

    @Autowired
    private ShopMapper shopMapper;

    @Test
    public void testAdd(){
        Shop shop = new Shop();
        shop.setName("丁真");
        shop.setTel("123");
        shop.setState(1);
        shop.setAddress("四川");
        shop.setRegisterTime(new Date().getTime());
        Employee employee = new Employee();
        employee.setId(0l);
        shop.setAdmin(employee);
        shopMapper.add(shop);
    }

    @Test
    public void testList(){
        System.out.println(shopMapper.list());
    }

    @Test
    public void testUpdateAdminId(){
        Shop shop = new Shop();
        shop.setId(30l);
        shopMapper.updateAdminId(30l);
    }

    @Test
    public void testDelete(){
        System.out.println(shopMapper.delete(33l));
    }

    @Test
    public void testFindByAddress(){
        String s = "湖北省武汉市武昌区武汉大学";
        System.out.println(shopMapper.findByAddress(s));
    }

    @Test
    public void testFindById(){
        System.out.println(shopMapper.findById(31));
        System.out.println(shopMapper.findById(31).getAdmin_id());
    }
}
