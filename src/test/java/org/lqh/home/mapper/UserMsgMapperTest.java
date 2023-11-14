package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.UserMsg;
import org.lqh.home.utils.GaoDeMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMsgMapperTest {

    @Autowired
    private UserMsgMapper userMsgMapper;

    @Test
    public void testAdd() throws UnsupportedEncodingException {
        UserMsg userMsg = new UserMsg();
        userMsg.setAddress(Objects.requireNonNull(GaoDeMapUtil.getLngAndLag("湖北省荆州市沙市区荆州吾悦广场")).getFormattedAddress());
        userMsg.setAdmin_id(316L);
        userMsg.setShop_id(30L);
        userMsg.setName("小白");
        userMsg.setPet_id(4L);
        userMsg.setPrice(888.88);
        userMsg.setUser_id(3L);
        userMsg.setBirth(System.currentTimeMillis());
        userMsg.setIsinoculation(1);
        userMsg.setSex("雌");
        userMsg.setState(1);
        userMsg.setCreatetime(System.currentTimeMillis());
        userMsgMapper.add(userMsg);
    }

    @Test
    public void testGetPetListByState(){
        System.out.println(userMsgMapper.getPetListByState(1));
        System.out.println("----");
        System.out.println(userMsgMapper.getPetListByState(0));
    }

    @Test
    public void testGetAllId(){
        System.out.println(userMsgMapper.getAllId());
    }

    @Test
    public void testListings(){
        userMsgMapper.listings(777.77,5);
    }

    @Test
    public void testGetShopList(){
        System.out.println(userMsgMapper.getShopList(311));
    }

    @Test
    public void testGetAllShopId(){
        List<Long> list = userMsgMapper.getAllShopId();
        List<Long> newList = list.stream().distinct().collect(Collectors.toList());
        list.clear();
        list.addAll(newList);
        System.out.println(newList);
        System.out.println("----");
        System.out.println(list);
    }
}
