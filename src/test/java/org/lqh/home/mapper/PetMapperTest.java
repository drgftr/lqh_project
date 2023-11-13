package org.lqh.home.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lqh.home.entity.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/13
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PetMapperTest {

    @Autowired
    private PetMapper petMapper;

    @Test
    public void testAdd(){
        Pet pet = new Pet("鸡","鸡你太美");
        petMapper.add(pet);
    }

    @Test
    public void testList(){
        System.out.println(petMapper.list());
    }

    @Test
    public void testFindById(){
        System.out.println(petMapper.findById(6));
    }
}
