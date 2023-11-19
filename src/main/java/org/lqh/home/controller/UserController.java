package org.lqh.home.controller;

import org.lqh.home.common.Constants;
import org.lqh.home.entity.*;
import org.lqh.home.net.LoggerMsg;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.service.*;
import org.lqh.home.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    private IUserMsgService iUserMsgService;

    private IShopService iShopService;

    private IPetService iPetService;

    private RedisTemplate redisTemplate;

    private IPetShopService iPetShopService;

    private IEmployeeService iEmployeeService;

    @Autowired
    public UserController(IUserMsgService iUserMsgService, IShopService iShopService,IEmployeeService iEmployeeService
            , IPetService iPetService, RedisTemplate redisTemplate
            , IPetShopService iPetShopService) {
        this.iEmployeeService = iEmployeeService;
        this.iUserMsgService = iUserMsgService;
        this.iShopService = iShopService;
        this.iPetService = iPetService;
        this.redisTemplate = redisTemplate;
        this.iPetShopService = iPetShopService;
    }

    @PostMapping("/publish")
    public NetResult publishTask(@RequestBody UserMsg userMsg, HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        User user = (User) object;

        //名字不能空
        if (StringUtil.isEmpty(userMsg.getName())) {
            return ResultGenerator.genErrorResult(NetCode.PET_NAME_INVALID, Constants.PETNAME_IS_NULL);
        }

        //地址不能空
        if (StringUtil.isEmpty(userMsg.getAddress())) {
            return ResultGenerator.genErrorResult(NetCode.ADDRESS_INVALID, Constants.ADDRESS_IS_NULL);
        }

        //判断性别是不是空
        if (StringUtil.isEmpty(userMsg.getSex())) {
            return ResultGenerator.genErrorResult(NetCode.SEX_INVALID, Constants.SEX_ERROR);
        }

        //判断性别是不是雄 雌
        if (!userMsg.getSex().equals("雄") && !userMsg.getSex().equals("雌")) {
            return ResultGenerator.genErrorResult(NetCode.SEX_INVALID, Constants.SEX_ERROR);
        }

        //判断接种信息 是不是 1或0
        if (userMsg.getIsinoculation() != 0 && userMsg.getIsinoculation() != 1) {
            return ResultGenerator.genErrorResult(NetCode.ISINOCULATION_INVALID, Constants.INOCULATION_ERROR);
        }

        //判断生日不能<0
        if (userMsg.getBirth() < 0) {
            return ResultGenerator.genErrorResult(NetCode.BIRTH_INVALID, Constants.BIRTH_ERROR);
        }

        //获取所有店铺
        List<Shop> shopList = iShopService.list();
        List<Location> locations = new ArrayList<>();
        //把店铺地址加到list
        for (Shop value : shopList) {
            try {
                locations.add(GaoDeMapUtil.getLngAndLag(value.getAddress()));
            } catch (UnsupportedEncodingException e) {
                LoggerMsg.errorMsg(e);
            }
        }

        Location userLocation = null;
        //获取用户发信息的地址 如果是解析不了的地址就返回非法地址
        try {
            userLocation = GaoDeMapUtil.getLngAndLag(userMsg.getAddress());
        } catch (UnsupportedEncodingException e) {
            return  ResultGenerator.genErrorResult(NetCode.ADD_ERROR,Constants.ADD_ERROR);
        }
        //获取离用户最近的店铺的地址
        userLocation = AddressDistanceComparator.findNearestAddress(userLocation, locations);

        //找到这个店铺并绑定
        Shop shop = iShopService.findByAddress(userLocation.getFormattedAddress());
        //没找到店铺 返回附近暂无店铺
        if (shop==null){
            return ResultGenerator.genErrorResult(NetCode.SHOP_IS_NULL,Constants.SHOP_ERROR);
        }
        //设置店铺id
        userMsg.setShop_id(shop.getId());
        //添加时间
        userMsg.setCreatetime(System.currentTimeMillis());
        //Employee employee = iEmployeeService.findById(shop.getAdmin_id());
        //设置adminid
        userMsg.setAdmin_id(shop.getAdmin_id());
        Pet pet = iPetService.findById(userMsg.getPet_id());
        //判断有没有这个宠物信息 ，没有返回非法数据
        if (pet==null){
            return ResultGenerator.genErrorResult(NetCode.PET_TYPE_ERROR,Constants.STATE_ERROR);
        }
        //设置宠物类型
        userMsg.setPet_id(userMsg.getPet_id());
        //设置用户（发布者）id
        userMsg.setUser_id(user.getId());

        //把信息添加到数据库
        int result = iUserMsgService.add(userMsg);
        //判断数据插入到数据库没
        if (result != 1) {
            return ResultGenerator.genErrorResult(NetCode.ADD_ERROR,Constants.ADD_ERROR);
        }
        return ResultGenerator.genSuccessResult(Constants.ADD_SUCCESS);
    }

    //店铺处理宠物并上架
    @PostMapping("/dispose")
    public NetResult disposePet(double price, long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        Employee employee = (Employee) object;
        //根据id 和管理员id找寻主信息
        UserMsg userMsg = iUserMsgService.findByIdAndAdminId(id, employee.getId());
        //判断数据库有无这个信息
        if (userMsg == null) {
            return ResultGenerator.genErrorResult(NetCode.LIST_IS_NULL, Constants.LIST_IS_NULL);
        }
        //判断宠物上架没 如果已经上架 就不能继续上架
        if (userMsg.getState() == 1) {
            return ResultGenerator.genErrorResult(NetCode.PETSTATE_ERROR, Constants.PETSTATE_ERROR);
        }
        //判断价格 不能小于0 ，不知道要不要
        if (price < 0) {
            return ResultGenerator.genErrorResult(NetCode.MONEY_ERROR, Constants.MONEY_ERROR);
        }

        //处理宠物 把他的状态改成1
        int result = iUserMsgService.listings(id);
        //判断数据库插入成功没
        if (result == 0) {
            return ResultGenerator.genSuccessResult(Constants.LISTING_ERROR);
        }

        //获取参数的
        PetShop petShop = getPetShop(userMsg, price);

        //把数据插入到商品宠物表中
        int result1 = iPetShopService.add(petShop);
        //判断插入成功没
        if (result1 == 0) {
            return ResultGenerator.genErrorResult(NetCode.PETSHOP_ADD_ERROR, Constants.PETSHOP_ADD_ERROR);
        }
        return ResultGenerator.genSuccessResult(Constants.LISTING_SUCCESS);
    }


    //买宠物
    @PostMapping("/buy")
    public NetResult buyPet(long id, HttpServletRequest request) {
        String token = request.getHeader("token");

        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        User user = (User) object;

        //在数据库找这个宠物信息
        PetShop petShop = iPetShopService.getPetById(id);
        //判断有无这个宠物信息
        if (petShop == null) {
            return ResultGenerator.genErrorResult(NetCode.PETSHOP_ERROR, Constants.PETSHOP_ERROR);
        }

        //判断宠物被买走没
        if (petShop.getState() == 1) {
            return ResultGenerator.genErrorResult(NetCode.PETSHOP_IS_BUY, Constants.PETSHOP_IS_BUY);
        }

        //把状态改为1 1是已出售 再把user_id改成被买家的id
        int result = iPetShopService.buy(id, user.getId());
        //判断数据库插入成功没
        if (result == 0) {
            return ResultGenerator.genErrorResult(NetCode.BUY_ERROR, Constants.BUY_ERROR);
        }
        return ResultGenerator.genSuccessResult(Constants.BUY_SUCCESS);
    }

    //设置参数用的
    public PetShop getPetShop(UserMsg userMsg, double sellPrice) {
        PetShop petShop = new PetShop();
        petShop.setShop_id(userMsg.getShop_id());
        petShop.setSellTime(System.currentTimeMillis());
        petShop.setUsermsg_id(userMsg.getId());
        petShop.setName(userMsg.getName());
        petShop.setAdmin_id(userMsg.getAdmin_id());
        petShop.setCostPrice(userMsg.getPrice());
        petShop.setSellPrice(sellPrice);
        return petShop;
    }

    //寻主列表（商铺后台的） state 0是未处理的 1是已处理的
    @GetMapping("/pettype")
    public NetResult petList(int state, HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        //只能传1或0进来 传别的就是非法参数
        if (state != 1 && state != 0) {
            return ResultGenerator.genErrorResult(NetCode.STATE_ERROR, Constants.STATE_ERROR);
        }

        Employee employee = (Employee) object;

        //在数据库找到未处理的数据 根据管理员id和state
        List<UserMsg> list = iUserMsgService.getPetListByState(state, employee.getId());
        //没有数据的话就返回暂无此数据
        if (list.size() == 0) {
            return ResultGenerator.genSuccessResult(Constants.LIST_IS_NULL);
        }

        //设置宠物信息 商店名 主人名
        for (UserMsg userMsg : list) {
            //System.out.println(userMsg);
            userMsg.setPet(iPetService.findById(userMsg.getPet_id()));
        }
        return ResultGenerator.genSuccessResult(list);
    }

    //用户查询自己发布的寻主
    @GetMapping("/userpetlist")
    public NetResult userList(HttpServletRequest request) {
        String token = request.getHeader("token");

        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        User user = (User) object;

        //根据用户id查询自己发布的消息
        List<UserMsg> list = iUserMsgService.getUserList(user.getId());
        //没有数据会返回暂无此数据
        if (list.size() == 0) {
            return ResultGenerator.genErrorResult(NetCode.USER_LIST_IS_NULL, Constants.USER_LIST_IS_NULL);
        }
        //设置宠物信息 商店名 主人名
        for (UserMsg userMsg : list) {
            userMsg.setPet(iPetService.findById(userMsg.getPet_id()));

        }
        return ResultGenerator.genSuccessResult(list);
    }

    //商铺列表
    @GetMapping("/shoplist")
    public NetResult shopList(HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        //找到所有店铺
        List<Shop> list = iShopService.list();
        //没有的话 返回附近暂无店铺
        if (list.size()==0){
            return ResultGenerator.genErrorResult(NetCode.SHOP_LIST_IS_NULL, Constants.SHOP_ERROR);
        }

        return ResultGenerator.genSuccessResult(list);
    }

    //店铺宠物列表
    @GetMapping("/shoppetlist")
    public NetResult shopPetList(long shopId, HttpServletRequest request) {
        String token = request.getHeader("token");

        Object object = redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        User user = (User) object;

        //判断有无这个店铺
        Shop shop = iShopService.findById(shopId);
        //没有的话 返回暂无该店铺信息
        if (shop == null) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_IS_NULL, Constants.SHOP_IS_NULL);
        }
        //从数据库获取这个店铺上架的商品
        List<PetShop> shopList = iPetShopService.getShopList(shopId);
        //没有的话 返回这个店铺还没上架宝贝哦
        if (shopList.size()==0){
            return ResultGenerator.genErrorResult(NetCode.LIST_IS_NULL,Constants.SHOPPET_IS_NULL);
        }

        UserMsg userMsg = null;
        for (PetShop petShop : shopList) {
            userMsg = iUserMsgService.findById(petShop.getUsermsg_id());
            //把成本价设置成0 不能给用户看
            userMsg.setPrice(0);
            //设置宠物的信息
            petShop.setUserMsg(userMsg);
            //把成本价设置成0 不能给用户看
            petShop.setCostPrice(0);
            //设置宠物种类
            petShop.setPet(iPetService.findById(userMsg.getPet_id()));
        }

        return ResultGenerator.genSuccessResult(shopList);
    }

    //修改宠物价格
//    @PostMapping("/revise")
//    public NetResult revisePetPrice(double price,long id,HttpServletRequest request){
//        String token = request.getHeader("token");
//        //判断token有没有
//        if (StringUtil.isEmpty(token)){
//            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
//        }
//        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
//        //判断token过期没
//        if (employee==null){
//            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID,Constants.INVALID_TOKEN);
//        }
//        //判断价格 不能小于0 ，不知道要不要
//        if (price<0){
//            return ResultGenerator.genErrorResult(NetCode.MONEY_ERROR,Constants.MONEY_ERROR);
//        }
//
//        return null;
//    }

}
