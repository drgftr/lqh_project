package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/26
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SaleMapperTest {
    @Autowired
    private SaleMapper saleMapper;

    @Test
    public void selectCount(){
        System.out.println(saleMapper.selectCount());
    }
}
