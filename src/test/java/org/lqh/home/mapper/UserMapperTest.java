package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.Users;
import org.lqh.home.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/9
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UsersMapper userMapper;

    @Test
    public void testAdd(){
//        Users user = new Users();
//        user.setUsername("丁真1");
//        user.setAge(11);
//        user.setPassword(MD5Util.MD5Encode("123456","utf-8"));
//        user.setRegisterTime(System.currentTimeMillis());
//        user.setPhone("10000000001");
//        user.setState(1);
//        userMapper.add(user);
    }

    @Test
    public void testSelect(){
//        String name = "admin";
//        Users users = new Users();
//        users.setUsername("admin");
//        System.out.println(userMapper.getUser("admin",MD5Util.MD5Encode("123456","utf-8")));
//        System.out.println(userMapper.getAdmin("admin","123456"));
    }

    @Test
    public void testGetPhone(){
        System.out.println(userMapper.selectPhone("13329766770"));
    }
}
