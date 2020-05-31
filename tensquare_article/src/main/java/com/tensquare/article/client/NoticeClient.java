package com.tensquare.article.client;

import com.tensquare.article.pojo.Notice;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 10:59 2020/5/27
 * @Modified By:
 */
@FeignClient(value = "tensquare-notice")
public interface NoticeClient {
    @RequestMapping(value = "notice",method = RequestMethod.POST)
    public Result save(@RequestBody Notice notice);
}
