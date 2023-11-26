package org.lqh.home.service;

import org.junit.Test;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.runner.RunWith;
import org.lqh.home.service.impl.SaleService;
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
public class SaleServiceTest {
    @Autowired
    private SaleService saleService;

    @Test
    public void testSelectSaleByArray(){

        System.out.println(saleService.selectSaleByArray(5,3));
    }

}
