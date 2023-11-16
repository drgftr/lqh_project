package org.lqh.home.service.impl;

import org.lqh.home.entity.UserMsg;
import org.lqh.home.mapper.UserMsgMapper;
import org.lqh.home.service.IUserMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/
@Service
public class UserMsgService implements IUserMsgService {

    private UserMsgMapper userMsgMapper;

    @Autowired
    public UserMsgService(UserMsgMapper userMsgMapper){
        this.userMsgMapper = userMsgMapper;
    }
    @Override
    public int add(UserMsg userMsg) {
        return userMsgMapper.add(userMsg);
    }

    @Override
    public int addTask(long shopId, long employeeId, long petId, long user_id, long userMsgId) {
        return userMsgMapper.addTask(shopId, employeeId, petId, user_id, userMsgId);
    }

    @Override
    public List<UserMsg> getPetListByState(int state,long adminId) {
        return userMsgMapper.getPetListByState(state,adminId);
    }

    @Override
    public List<UserMsg> getUserList(long userId) {
        return userMsgMapper.getUserList(userId);
    }

    @Override
    public List<Long> getAllId() {
        return userMsgMapper.getAllId();
    }

    @Override
    public int listings(long id) {
        return userMsgMapper.listings(id);
    }

    @Override
    public List<UserMsg> getShopList(long shopId) {
        return userMsgMapper.getShopList(shopId);
    }

    @Override
    public List<Long> getAllShopId() {
        return userMsgMapper.getAllShopId();
    }

    @Override
    public UserMsg findById(long id) {
        return userMsgMapper.findById(id);
    }

    @Override
    public UserMsg findByIdAndAdminId(long id, long adminId) {
        return userMsgMapper.findByIdAndAdminId(id, adminId);
    }
}
