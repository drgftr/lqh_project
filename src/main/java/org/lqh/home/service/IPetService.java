package org.lqh.home.service;

import org.lqh.home.entity.Pet;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/13
 **/
public interface IPetService {
    int add(Pet pet);
    List<Pet> list();
    Pet findById(long id);
}
