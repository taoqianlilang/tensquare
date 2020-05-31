package com.tensquare.notice.controller;

import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import com.tensquare.notice.service.NoticeService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 16:41 2020/5/26
 * @Modified By:
 */
@RestController
@RequestMapping("notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

//    根据id查询消息通知
//    根据条件分页查询消息通知
//    新增通知
//    修改通知
//    根据用户id查询该用户的待推送消息（新消息）
//    删除待推送消息（新消息）

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public Result selectById(@PathVariable String id){
        Notice notice = noticeService.selectById(id);
        return new Result(true, StatusCode.OK, "查询成功", notice);
    }

    @RequestMapping(value = "search/{page}/{size}",method = RequestMethod.POST)
    public Result selectByList(@PathVariable Integer page,
                               @PathVariable Integer size,
                               @RequestBody Map<String,Object> map){

        List<Notice> list = noticeService.selectByList(page, size, map);

        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Notice notice){
        noticeService.save(notice);
        return new Result(true, StatusCode.OK, "新添加成功");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Result updateById(@RequestBody Notice notice){
        noticeService.updateById(notice);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @RequestMapping(value = "fresh/{page}/{size}/{userId}",method = RequestMethod.GET)
    public Result selectByUserId(@PathVariable String userId,
                                 @PathVariable Integer page,
                                 @PathVariable Integer size

    ){
        List<Notice> notices = noticeService.selectByUserId(userId,page,size);
        return new Result(true, StatusCode.OK, "查询成功", notices);
    }

    @RequestMapping(value = "fresh",method = RequestMethod.DELETE)
    public Result delete(@RequestBody NoticeFresh noticeFresh){
        noticeService.delete(noticeFresh);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
