package org.lqh.home;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.lqh.home.entity.Location;
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
            Location addressInfo = GaoDeMapUtil.getLngAndLag("湖北省武汉市亿达云山湖");
            System.out.println(Objects.requireNonNull(GaoDeMapUtil.getLngAndLag("湖北省武汉市华中科技大学")).toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
