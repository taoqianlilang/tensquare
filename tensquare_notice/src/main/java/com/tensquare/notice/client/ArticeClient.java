package com.tensquare.notice.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 21:01 2020/5/26
 * @Modified By:
 */
@FeignClient(value = "tensquare-article")
public interface ArticeClient {
    /**
     * @param articleId
     * @return
     *  GET /article/{articleId} 根据ID查询文章
     */
    @RequestMapping(value = "article/{articleId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("articleId") String articleId) ;
}
