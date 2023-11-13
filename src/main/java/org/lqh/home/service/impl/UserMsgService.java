package org.lqh.home.service.impl;

import org.lqh.home.entity.UserMsg;
import org.lqh.home.mapper.UserMsgMapper;
import org.lqh.home.service.IUserMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int addTask(long shopId, long employeeId, long petId, long userId, long userMsgId) {
        return userMsgMapper.addTask(shopId, employeeId, petId, userId, userMsgId);
    }
}
