package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.UserMsg;
import org.lqh.home.utils.GaoDeMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserMsgMapperTest {

    @Autowired
    private UserMsgMapper userMsgMapper;

    @Test
    public void testAdd() throws UnsupportedEncodingException {
        UserMsg userMsg = new UserMsg();
        userMsg.setAddress(Objects.requireNonNull(GaoDeMapUtil.getLngAndLag("湖北省荆州市沙市区荆州吾悦广场")).getFormattedAddress());
        userMsg.setAdminId(316L);
        userMsg.setShopId(30L);
        userMsg.setName("小白");
        userMsg.setPetId(4L);
        userMsg.setPrice(888.88);
        userMsg.setUserId(3L);
        userMsg.setBirth(System.currentTimeMillis());
        userMsg.setIsinoculation(1);
        userMsg.setSex("雌");
        userMsg.setState(1);
        userMsg.setCreatetime(System.currentTimeMillis());
        userMsgMapper.add(userMsg);
    }
}
