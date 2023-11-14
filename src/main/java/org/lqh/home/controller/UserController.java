package org.lqh.home.controller;

import org.lqh.home.common.Constants;
import org.lqh.home.entity.*;
import org.lqh.home.net.NetCode;
import org.lqh.home.net.NetResult;
import org.lqh.home.net.param.AddPetParam;
import org.lqh.home.service.*;
import org.lqh.home.utils.AddressDistanceComparator;
import org.lqh.home.utils.GaoDeMapUtil;
import org.lqh.home.utils.ResultGenerator;
import org.lqh.home.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    private IUserMsgService iUserMsgService;

    private IEmployeeService iEmployeeService;

    private IShopService iShopService;

    private IUsersService iUsersService;

    private IPetService iPetService;

    private RedisTemplate redisTemplate;

    @Autowired
    public UserController (IUserMsgService iUserMsgService,IEmployeeService iEmployeeService,IShopService iShopService
            ,IUsersService iUsersService,IPetService iPetService,RedisTemplate redisTemplate){
        this.iUserMsgService = iUserMsgService;
        this.iEmployeeService = iEmployeeService;
        this.iShopService = iShopService;
        this.iUsersService = iUsersService;
        this.iPetService = iPetService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/publish")
    public NetResult Publish(@RequestBody AddPetParam addPetParam, HttpServletRequest request){
        String token = request.getHeader("token");
        //判断token有没有
        if (StringUtil.isEmpty(token)){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
        }

        UserMsg userMsg = addPetParam.userMsg;
        Users user = (Users) redisTemplate.opsForValue().get(token);
        //System.out.println(user);
        //判断token过期没
        if (user==null){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //System.out.println(user);
        ///int user_id = addPetParam.user_id;
        //名字不能空
        if (StringUtil.isEmpty(userMsg.getName())){
            return ResultGenerator.genErrorResult(NetCode.PET_NAME_INVALID, Constants.PETNAME_IS_NULL);
        }

        //地址不能空
        if (StringUtil.isEmpty(userMsg.getAddress())){
            return ResultGenerator.genErrorResult(NetCode.ADDRESS_INVALID, Constants.ADDRESS_IS_NULL);
        }

        //判断性别的
        if (StringUtil.isEmpty(userMsg.getSex())){
            return ResultGenerator.genErrorResult(NetCode.SEX_INVALID, Constants.SEX_ERROR);
        }

        if (userMsg.getIsinoculation()!=0&&userMsg.getIsinoculation()!=1){
            return ResultGenerator.genErrorResult(NetCode.ISINOCULATION_INVALID, Constants.INOCULATION_ERROR);
        }

        if (userMsg.getBirth()<0){
            return  ResultGenerator.genErrorResult(NetCode.BIRTH_INVALID, Constants.BIRTH_ERROR);
        }

        List<Shop> shopList = iShopService.list();
        List<Location> locations = new ArrayList<>();

        try {
            //把店铺地址加到list
            for (Shop value : shopList) {
                locations.add(GaoDeMapUtil.getLngAndLag(value.getAddress()));
            }
            //获取用户发信息的地址
            Location userLocation = GaoDeMapUtil.getLngAndLag(userMsg.getAddress());
            //获取离用户最近的店铺的地址
            Location latest = AddressDistanceComparator.findNearestAddress(userLocation,locations);
            //找到这个店铺并绑定
            Shop shop = iShopService.findByAddress(latest.getFormattedAddress());
            userMsg.setShop(shop);
            //添加时间
            userMsg.setCreatetime(System.currentTimeMillis());
            //绑定店铺 admin 账号
            System.out.println(shop.getAdmin_id());
            Employee admin = iEmployeeService.findById(shop.getAdmin_id());
            userMsg.setAdmin(admin);
            //绑定宠物类型
            Pet pet = iPetService.findById(userMsg.getPet_id());
            userMsg.setPet(pet);
            //绑定user
            //Users users = iUsersService.findById(user_id);
            userMsg.setUsers(user);

            int result = iUserMsgService.add(userMsg);
            if (result!=1){
                return ResultGenerator.genFailResult("添加失败");
            }
//            System.out.println(shop.getId());
//            System.out.println(admin.getId());
//            System.out.println(pet.getId());
//            System.out.println(user.getId());
//            System.out.println(userMsg.getId());
            int result1 = iUserMsgService.addTask(shop.getId(),admin.getId(),pet.getId(),user.getId(),userMsg.getId());
            if (result1!=1){
                return ResultGenerator.genErrorResult(NetCode.RESULT_ERROR,Constants.RESULE_ERROR);
            }
            return ResultGenerator.genSuccessResult(pet);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("添加失败");
    }

    //店铺处理
    @PostMapping("/dispose")
    public NetResult DisposePet(double price,long id,HttpServletRequest request){
        String token = request.getHeader("token");
        //判断token有没有
        if (StringUtil.isEmpty(token)){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (employee==null){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID,Constants.INVALID_TOKEN);
        }

        List<Long> allId = iUserMsgService.getAllId();
        //判断有没有这个宠物信息
        if(!allId.contains(id)){
            return ResultGenerator.genErrorResult(NetCode.LIST_IS_NULL,Constants.LIST_IS_NULL);
        }

        //判断价格 不能小于0 ，不知道要不要
        if (price<0){
            return ResultGenerator.genErrorResult(NetCode.MONEY_ERROR,Constants.MONEY_ERROR);
        }

        int result = iUserMsgService.listings(price, id);
        if (result!=0){
            return  ResultGenerator.genSuccessResult(Constants.LISTING_SUCCESS);
        }
        return ResultGenerator.genErrorResult(NetCode.LISTING_ERROR,Constants.LISTING_ERROR);
    }
    //寻主列表（商铺后台的） state 0是未处理的 1是已处理的
    @GetMapping("/pettype")
    public NetResult PetList(int state,HttpServletRequest request){
        String token = request.getHeader("token");
        //判断token有没有
        if (StringUtil.isEmpty(token)){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (employee==null){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID,Constants.INVALID_TOKEN);
        }
        // 0是未处理的
        if (state == 0){
            List<UserMsg> list = iUserMsgService.getPetListByState(state);
            try {
                //设置宠物信息 商店名 主人名
                for (UserMsg userMsg : list) {
                    userMsg.setPet(iPetService.findById(userMsg.getPet_id()));
                    userMsg.setMasterName(iUsersService.findById(userMsg.getUser_id()).getUsername());
                    userMsg.setShopName(iShopService.findById(userMsg.getShop_id()).getName());
                }
            }catch (Exception e){
                return ResultGenerator.genSuccessResult(Constants.LIST_IS_NULL);
            }
            return ResultGenerator.genSuccessResult(list);
        }else if (state == 1){
            List<UserMsg> list = iUserMsgService.getPetListByState(state);
            try {
                //设置宠物信息 商店名 主人名
                for (UserMsg userMsg : list) {
                    userMsg.setPet(iPetService.findById(userMsg.getPet_id()));
                    userMsg.setMasterName(iUsersService.findById(userMsg.getUser_id()).getUsername());
                    userMsg.setShopName(iShopService.findById(userMsg.getShop_id()).getName());
                }
            }catch (Exception e){
                return ResultGenerator.genSuccessResult(Constants.LIST_IS_NULL);
            }
            return ResultGenerator.genSuccessResult(list);
        }else {
            return ResultGenerator.genErrorResult(NetCode.PET_TYPE_ERROR,Constants.PET_TYPE_ERROR);
        }
    }

    @GetMapping("/userlist")
    public NetResult UserList( HttpServletRequest request){
        String token = request.getHeader("token");
        //判断token有没有
        if (StringUtil.isEmpty(token)){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
        }

        Users user = (Users) redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (user==null){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        List<UserMsg> list = iUserMsgService.getUserList(user.getId());
        try {
            //设置宠物信息 商店名 主人名
            for (UserMsg userMsg : list) {
                userMsg.setPet(iPetService.findById(userMsg.getPet_id()));
                userMsg.setMasterName(iUsersService.findById(userMsg.getUser_id()).getUsername());
                userMsg.setShopName(iShopService.findById(userMsg.getShop_id()).getName());
            }
            return ResultGenerator.genSuccessResult(list);
        }catch (Exception e){
            return ResultGenerator.genErrorResult(NetCode.USER_LIST_IS_NULL,Constants.USER_LIST_IS_NULL);
        }
    }

    //商铺列表
    @GetMapping("/shoplist")
    public NetResult ShopList(HttpServletRequest request){
        String token = request.getHeader("token");
        //判断token有没有
        if (StringUtil.isEmpty(token)){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
        }

        Users user = (Users) redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (user==null){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        List<Shop> list = iShopService.list();

        return  ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/shoppetlist")
    public NetResult ShopPetList(long shopId,HttpServletRequest request){
        String token = request.getHeader("token");
        //判断token有没有
        if (StringUtil.isEmpty(token)){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
        }

        Users user = (Users) redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (user==null){
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }

        List<Long> allUserMsgShop = iUserMsgService.getAllShopId();
        //去重
        List<Long> newList = allUserMsgShop.stream().distinct().collect(Collectors.toList());
        allUserMsgShop.clear();
        allUserMsgShop.addAll(newList);
        //判断 店铺有无上架宠物
        if(!allUserMsgShop.contains(shopId)){
            return ResultGenerator.genErrorResult(NetCode.SHOP_IS_NULL,Constants.SHOP_IS_NULL);
        }
        List<UserMsg> shopList = iUserMsgService.getShopList(shopId);
        return ResultGenerator.genSuccessResult();
    }

}
