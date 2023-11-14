package org.lqh.home.service;

import org.apache.ibatis.annotations.Param;
import org.lqh.home.entity.UserMsg;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/

public interface IUserMsgService {
    int add(UserMsg userMsg);

    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("userId")long user_id, @Param("userMsgId")long userMsgId);

    List<UserMsg> getPetListByState(int state);

    List<UserMsg> getUserList(long userId);

    List<Long> getAllId();

    int listings(@Param("price") double price, @Param("id") long id);

    List<UserMsg> getShopList(long shopId);

    List<Long> getAllShopId();
}
