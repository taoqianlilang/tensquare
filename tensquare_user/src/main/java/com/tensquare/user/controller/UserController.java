package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 22:02 2020/5/24
 * @Modified By:
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public Result findByUserId(@PathVariable String  userId){

        User user = userService.findByUserId(userId);
        if (user==null){
            return new Result(true, StatusCode.ERROR, "查询失败", user);
        }

        return new Result(true, StatusCode.OK, "查询成功", user);
    }
}
