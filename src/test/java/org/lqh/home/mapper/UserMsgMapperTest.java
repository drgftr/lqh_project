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
        userMsg.setAddress(Objects.requireNonNull(GaoDeMapUtil.getLngAndLag("湖北省武汉市亿达云山湖")).toString());
        userMsg.setAdminId(316L);
        userMsg.setShopId(30L);
        userMsg.setName("小白");
        userMsg.setPrice(200.12);
        userMsg.setUserId(3L);
        userMsg.setBirth(System.currentTimeMillis());
        userMsg.setIsinoculation(0);
        userMsg.setSex("雄");
        userMsg.setState(1);
        userMsg.setCreatetime(System.currentTimeMillis());
        userMsgMapper.add(userMsg);
    }
}
