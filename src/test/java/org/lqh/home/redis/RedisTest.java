package org.lqh.home.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/2
 **/
public class RedisTest {
    JedisPool pool = new JedisPool("localhost",6379);
    

    @Test
    public void test(){
        try (Jedis jedis = pool.getResource()){
            jedis.set("fox","666");
            System.out.println(jedis.get("fox"));
        }
    }

    @Test
    public void testHash(){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        List<User> userList = getUserList();
        for (User user : userList){
            Map<String,String> map = new HashMap<>();
            map.put("id",user.getId());
            map.put("name",user.getName());
            jedis.hmset(user.getId(),map);
            Map userObj =jedis.hgetAll(user.getId());
            System.out.println(userObj);
            String name =jedis.hget(user.getId(),"name");
            System.out.println("按id和name取值："+name);
        }
        jedis.close();
    }

    private static List<User> getUserList(){
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        user1.setName("王八1");

        User user2 = new User();
        user2.setId("2");
        user2.setName("王八2");
        userList.add(user1);
        userList.add(user2);
        return userList;
    }

    @Test
    public void testList(){
        try (Jedis jedis = pool.getResource()) {
            jedis.lpush("namelist", "xiaoyi", "xiaoer", "xiaosan");
            System.out.println(jedis.llen("namelist"));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testSet(){

        try (Jedis jedis = pool.getResource()) {
            jedis.set("11","app");
            System.out.println(jedis.get("11"));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
