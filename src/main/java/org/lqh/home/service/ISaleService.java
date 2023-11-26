package org.lqh.home.service;

import org.apache.ibatis.annotations.Param;
import org.lqh.home.entity.Sale;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/26
 **/

public interface ISaleService {

    int offGoods(@Param("id") long id, @Param("offSaleTime") long offSaleTime);
    int onGoods(@Param("id") long id, @Param("onSaleTime") long onSaleTime);
    Sale selectById(long id);
    int selectCount();
    List<Sale> selectSaleByArray(int currentPage, int pageSize);
    int settlement(@Param("count") int count,@Param("id") long id);
}
