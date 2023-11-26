package org.lqh.home.controller;

import org.lqh.home.common.Constants;
import org.lqh.home.entity.Employee;
import org.lqh.home.entity.Sale;
import org.lqh.home.entity.User;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.service.IEmployeeService;
import org.lqh.home.service.ISaleService;
import org.lqh.home.service.IUserService;
import org.lqh.home.utils.RedisKeyUtil;
import org.lqh.home.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/26
 **/
@RestController
@RequestMapping("/sale")
public class SaleController {

    private ISaleService iSaleService;
    private RedisTemplate redisTemplate;
    private IEmployeeService iEmployeeService;
    private IUserService iUserService;

    @Autowired
    public SaleController(ISaleService iSaleService,RedisTemplate redisTemplate
            ,IEmployeeService iEmployeeService,IUserService iUserService){
        this.iSaleService = iSaleService;
        this.redisTemplate = redisTemplate;
        this.iEmployeeService = iEmployeeService;
        this.iUserService = iUserService;
    }

    //上下架
    @PostMapping("/offOrOn")
    public NetResult offOrOnSaleGoods(long id,int state, HttpServletRequest request){
        //判断token过期没
        String token = request.getHeader("token");
        Employee employee = (Employee) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (employee == null){
            //如果过期返回token过期
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        //判断商品是否存在
        Sale sale = iSaleService.selectById(id);
        if (sale == null){
            //不存在返回没有此商品
            return ResultGenerator.genErrorResult(NetCode.SALE_IS_NULL,Constants.SALE_IS_NULL);
        }

        //判断是不是上架
        if (state == 1){
            //判断数据库加入成功没
            int result = iSaleService.onGoods(id,System.currentTimeMillis());
            if (result==0){
                //没成功返回上架失败
                return ResultGenerator.genErrorResult(NetCode.ONSALE_ERROR,Constants.ONSALE_ERROR);
            }
            return ResultGenerator.genSuccessResult("上架成功");
        }else if (state == 0){ //判断是不是下架
            //判断数据库加入成功没
            int result = iSaleService.offGoods(id,System.currentTimeMillis());
            if (result==0){
                //没成功返回下架失败
                return ResultGenerator.genErrorResult(NetCode.OFFSALE_ERROR,Constants.OFFSALE_ERROR);
            }
            return ResultGenerator.genSuccessResult("下架成功");
        }else{//都不是返回非法数据
            return ResultGenerator.genErrorResult(NetCode.STATE_ERROR,Constants.STATE_ERROR);
        }

    }


    /**
     *上下架商品列表 管理员查询的
     * @param currentPage 查的页数
     * @param request
     * @return
     */
    @GetMapping("/adminpagination")
    public NetResult paginationEmployee(int currentPage,HttpServletRequest request){
        //判断token过期没
        String token = request.getHeader("token");
        System.out.println(1);
        Employee employee =  (Employee) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (employee == null){
            //如果过期返回token过期
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        //页数<=0返回没有此页面
        if (currentPage<=0){
            return ResultGenerator.genErrorResult(NetCode.PAGE_ERROR,Constants.PAGE_ERROR);
        }

        //分页查询的数据 自己设置的一页3条
        List<Sale> list = iSaleService.selectSaleByArray(currentPage,3);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     *商品列表 用户查询的
     * @param currentPage 查的页数
     * @param request
     * @return
     */
    @GetMapping("/userpagination")
    public NetResult paginationUser(int currentPage,HttpServletRequest request){
        //判断token过期没
        String token = request.getHeader("token");
        User user =  (User) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (user == null){
            //如果过期返回token过期
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        //页数<=0返回没有此页面
        if (currentPage<=0){
            return ResultGenerator.genErrorResult(NetCode.PAGE_ERROR,Constants.PAGE_ERROR);
        }

        //分页查询的数据 自己设置的一页3条
        List<Sale> list = iSaleService.selectSaleByArray(currentPage,3);

        //用户查询的话要把成本价格隐藏
        for (Sale sale :list){
            sale.setCostPrice(0);
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     *
     * @param id 商品id
     * @param request
     * @return
     */
    @GetMapping("/findsale")
    public NetResult findOnSale(long id,HttpServletRequest request){
        //判断token过期没
        String token = request.getHeader("token");
        User user =  (User) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (user == null){
            //如果过期返回token过期
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //在数据库中通过id查商品
        Sale sale = iSaleService.selectById(id);
        if (sale == null){
            //没找到返回没有此商品
            return ResultGenerator.genErrorResult(NetCode.SALE_IS_NULL,Constants.SALE_IS_NULL);
        }
        //把成本价设置0
        sale.setCostPrice(0);
        return ResultGenerator.genSuccessResult(sale);
    }

    /**
     *
     * @param id 商品id
     * @param amount 买几件
     * @param request
     * @return
     */
    @PostMapping("/buy")
    public NetResult buy(long id,int amount,HttpServletRequest request){
        //判断token过期没
        String token = request.getHeader("token");
        User user =  (User) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (user == null){
            //如果过期返回token过期
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        //在数据库中通过id查商品
        Sale sale = iSaleService.selectById(id);
        //看商品存不存在
        if (sale == null){
            //没找到返回没有此商品
            return ResultGenerator.genErrorResult(NetCode.SALE_IS_NULL,Constants.SALE_IS_NULL);
        }
        //看商品上架没
        if (sale.getState() == 0){
            return ResultGenerator.genErrorResult(NetCode.STATE_ERROR,Constants.SALE_ERROR);
        }
        //最少要买1件
        if (amount<=0){
            return ResultGenerator.genErrorResult(NetCode.AMOUNT_ERROR,Constants.AMOUNT_ERROR);
        }

        //用户余额
        double userMoney = user.getMoney();
        //商品总价格
        double priceCount = amount*sale.getSalePrice();
        //看看钱够不够
        if (userMoney<priceCount){
            return ResultGenerator.genErrorResult(NetCode.MONEY_ERROR,Constants.MONEY_IACK);
        }
        userMoney -= priceCount;
        //修改用户余额 根据id
        int result = iUserService.settlementById(userMoney,user.getId());
        if (result == 0){
            return ResultGenerator.genErrorResult(NetCode.SETTLEMENT_ERROR,Constants.SETTLEMENT_ERROR);
        }

        //购买后的商品总数量
        int count = sale.getSaleCount()+amount;
        //商品结算 把购买后的商品总数量加到数据库
        iSaleService.settlement(count,sale.getId());

        return ResultGenerator.genSuccessResult("购买成功");
    }
}
