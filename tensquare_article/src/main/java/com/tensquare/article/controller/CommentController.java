package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 14:38 2020/5/24
 * @Modified By:
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @param commentId
     * @return
     * 增加点赞数(用redis保证不可重复点赞)
     */
    @RequestMapping(value = "/thumbup/{commentId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String commentId){

        String userid = "123";

        //在redis中查询用户是否已经点赞
        Object result = redisTemplate.opsForValue().get("thumbup_" + userid + "_" + commentId);

        //如果点赞不能重复点赞
        if (result != null) {
            return new Result(false, StatusCode.REMOTEERROR, "不能重复点赞");
        }

        //如果没有点赞，可以进行点赞操作
        commentService.thumbup(commentId);

        //保存点赞记录
        redisTemplate.opsForValue().set("thumbup_" + userid + "_" + commentId, 1);

        return new Result(true, StatusCode.OK, "点赞成功");
    }


    /**
     * @param articleId
     * @return
     * 通过文章id查评论
     */
    @RequestMapping(value = "/article/{articleId}",method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable String articleId){

        List<Comment> list = commentService.findByArticleId(articleId);

        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * @return
     * 查询所有的数据
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Comment> all=commentService.findAll();

        return new Result(true, StatusCode.OK, "查询所有成功", all);
    }

    /**
     * @param commentId
     * @return
     * 根据id查询数据
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String commentId){

        Comment comment = commentService.findById(commentId);

        String message;
        if (comment==null){
            message="查询为空";
        }else {
            message="查询成功";
        }
        return new Result(true, StatusCode.OK, message, comment);
    }

    /**
     * @param comment
     * @return
     * 新增数据
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment){
        commentService.save(comment);
        return new Result(true, StatusCode.OK, "新增成功");
    }

    /**
     * @param commentId
     * @param comment
     * @return
     * 更新数据
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String commentId,@RequestBody Comment comment){
        comment.set_id(commentId);
        commentService.update(comment);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * @param commentId
     * @return
     * 删除数据
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String commentId){
        commentService.delete(commentId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
