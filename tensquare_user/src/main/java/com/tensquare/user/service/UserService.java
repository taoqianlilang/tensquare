package com.tensquare.user.service;

import com.tensquare.user.dao.UserMapper;
import com.tensquare.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 22:01 2020/5/24
 * @Modified By:
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findByUserId(String userId) {
        User user = userMapper.selectById(userId);

        return user;
    }
}
