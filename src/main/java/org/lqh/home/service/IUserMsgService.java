package org.lqh.home.service;

import org.apache.ibatis.annotations.Param;
import org.lqh.home.entity.UserMsg;
import org.springframework.stereotype.Service;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/12
 **/

public interface IUserMsgService {
    int add(UserMsg userMsg);

    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("userId")long userId, @Param("userMsgId")long userMsgId);



}
