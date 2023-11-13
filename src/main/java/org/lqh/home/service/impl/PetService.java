package org.lqh.home.service.impl;

import org.lqh.home.entity.Pet;
import org.lqh.home.mapper.PetMapper;
import org.lqh.home.service.IPetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/13
 **/
@Service
public class PetService implements IPetService {

    private PetMapper petMapper;

    @Autowired
    public PetService(PetMapper petMapper){
        this.petMapper = petMapper;
    }
    @Override
    public int add(Pet pet) {
        return petMapper.add(pet);
    }

    @Override
    public List<Pet> list() {
        return petMapper.list();
    }

    @Override
    public Pet findById(long id) {
        return petMapper.findById(id);
    }
}
