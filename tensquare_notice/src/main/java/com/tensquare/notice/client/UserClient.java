package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 21:05 2020/5/26
 * @Modified By:
 */

@FeignClient(value = "tensquare-user")
public interface UserClient {

    /**
     * @param userId
     * @return
     * 根据用户id查用户
     */
    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
    public Result findByUserId(@PathVariable("userId") String userId);

}
