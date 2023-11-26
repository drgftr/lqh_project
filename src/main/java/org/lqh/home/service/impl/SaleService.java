package org.lqh.home.service.impl;

import org.lqh.home.entity.Sale;
import org.lqh.home.mapper.SaleMapper;
import org.lqh.home.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/26
 **/
@Service
public class SaleService implements ISaleService {

    private SaleMapper saleMapper;

    @Autowired
    public SaleService(SaleMapper saleMapper){
        this.saleMapper = saleMapper;
    }

    @Override
    public int offGoods(long id, long offSaleTime) {
        return saleMapper.offGoods(id, offSaleTime);
    }

    @Override
    public int onGoods(long id, long onSaleTime) {
        return saleMapper.onGoods(id, onSaleTime);
    }

    @Override
    public Sale selectById(long id) {
        return saleMapper.selectById(id);
    }

    @Override
    public int selectCount() {
        return saleMapper.selectCount();
    }

    @Override
    public List<Sale> selectSaleByArray(int currentPage, int pageSize) {
        List<Sale> list = saleMapper.selectSaleFindAll();
        int count = selectCount();
        int startCurrentPage = (currentPage-1)*pageSize; //开启的数据
        int endCurrentPage = currentPage*pageSize; //结束的数据
        int totalPage=(int)Math.ceil((double) count/pageSize); //总页数 做了进一
        //System.out.println(totalPage);
        //如果页面大于总页面 或<=0 返回null
        if (currentPage > totalPage || currentPage<=0){
            return null;
        }else if(currentPage < totalPage){ //如果查看的页面小于最大页面
            return list.subList(startCurrentPage,endCurrentPage);
        }else { //这是最后一页 返回最后几条数据 因为可能数据不够
            return list.subList(startCurrentPage,list.size());
        }
    }

    @Override
    public int settlement(int count, long id) {
        return saleMapper.settlement(count, id);
    }
}
