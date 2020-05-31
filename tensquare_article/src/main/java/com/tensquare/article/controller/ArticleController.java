package com.tensquare.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisTemplate redisTemplate;

    //测试公共异常处理
    @RequestMapping(value = "exception", method = RequestMethod.GET)
    public Result test() {
        int a = 1 / 0;

        return null;
    }

    @RequestMapping(value = "thumbup/{articleId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String articleId){

        //没jwt先设置一个user
        String userId="123";
        String key="thumbup_"+userId+"_"+articleId;

        Object flag = redisTemplate.opsForValue().get(key);

        if (flag==null){
            //为空那代表可以点赞
            articleService.thumbup(articleId,userId);
            //在redis中保存点赞信息
            redisTemplate.opsForValue().set(key, 1);
            //返回信息
            return new Result(true, StatusCode.OK, "点赞成功");
        }else {
            return new Result(false, StatusCode.REPERROR, "不能重复点赞");
        }
    }



    @RequestMapping(value = "subscription",method = RequestMethod.POST)
    public Result subscription(@RequestBody Map map){

        Boolean flage = articleService.subscription(map.get("userId").toString(),
                map.get("articleId").toString());
        if (flage){
            return new Result(true, StatusCode.OK, "订阅成功");
        }else {
            return new Result(true, StatusCode.OK, "取消订阅");
        }

    }

    // POST /article/search/{page}/{size} 文章分页
    @RequestMapping(value = "search/{page}/{size}", method = RequestMethod.POST)
    public Result findByPage(@PathVariable Integer page,
                             @PathVariable Integer size,
                             @RequestBody Map<String, Object> map) {

        Page<Article> articlePage = articleService.findByPage(map, page, size);

        List<Article> list = articlePage.getRecords();

        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    // DELETE /article/{articleId} 根据ID删除文章
    @RequestMapping(value = "{articleId}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String articleId) {
        articleService.deleteById(articleId);

        return new Result(true, StatusCode.OK, "删除成功");
    }


    // PUT /article/{articleId} 修改文章
    @RequestMapping(value = "{articleId}", method = RequestMethod.PUT)
    public Result updateById(@PathVariable String articleId,
                             @RequestBody Article article) {
        //设置id
        article.setId(articleId);
        // 执行修改
        articleService.updateById(article);

        return new Result(true, StatusCode.OK, "修改成功");
    }

    // POST /article 增加文章
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article) {
        articleService.save(article);
        return new Result(true, StatusCode.OK, "新增成功");
    }


    // GET /article/{articleId} 根据ID查询文章
    @RequestMapping(value = "{articleId}", method = RequestMethod.GET)
    public Result findById(@PathVariable String articleId) {
        Article article = articleService.findById(articleId);
        return new Result(true, StatusCode.OK, "查询成功", article);
    }

    // GET /article 文章全部列表
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Article> list = articleService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }
}
