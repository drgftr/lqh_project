package org.lqh.home;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.lqh.home.entity.Location;
import org.lqh.home.entity.Users;
import org.lqh.home.net.param.RegisterParam;
import org.lqh.home.utils.GaoDeMapUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapTest {
    @Test
    public void TestMap() throws UnsupportedEncodingException {
        try {
            Location addressInfo = GaoDeMapUtil.getLngAndLag("湖北省武汉市武汉大学");
            Location location = GaoDeMapUtil.getLngAndLag("湖北省荆州市吾悦广场");
            Location location1= GaoDeMapUtil.getLngAndLag("湖北省武汉市光谷广场");
            Location location2 = GaoDeMapUtil.getLngAndLag("湖北省武汉市洪山区亿达云山湖");
            System.out.println(location2);
            System.out.println(location);
            System.out.println(addressInfo);
            System.out.println(location1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test11(){
        RegisterParam registerParam = new RegisterParam();
        Users users = new Users();
        String code ="123456";
        users.setUsername("王成亮");
        users.setPassword("123456");
        users.setToken("qweewq");
        users.setAge(19);
        users.setPhone("15527158865");
        users.setState(1);
        //registerParam.setUsers(users);
        registerParam.setCode(code);
        JSONObject object = JSONObject.parseObject(registerParam.toString());
        System.out.println(object);
    }
}
