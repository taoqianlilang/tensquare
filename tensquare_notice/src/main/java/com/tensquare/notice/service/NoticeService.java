package com.tensquare.notice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensquare.notice.client.ArticeClient;
import com.tensquare.notice.client.UserClient;
import com.tensquare.notice.dao.NoticeFreshMapper;
import com.tensquare.notice.dao.NoticeMapper;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.*;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 16:41 2020/5/26
 * @Modified By:
 */
@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private NoticeFreshMapper noticeFreshMapper;
    @Autowired
    private ArticeClient articeClient;
    @Autowired
    private UserClient userClient;

    private void info(Notice notice){

        //获取用户信息
        Result userResult = userClient.findByUserId(notice.getOperatorid());
        HashMap userMap = (HashMap) userResult.getData();
        notice.setOperatorname(userMap.get("nickname").toString());

        //获取文章信息
        if ("article".equals(notice.getTargettype())) {
            Result articleResult = articeClient.findById(notice.getTargetid());
            HashMap articleMap = (HashMap) articleResult.getData();
            notice.setTargetname(articleMap.get("title").toString());
        }
//        //设置用户名字
//        String s = notice.getOperatorid();
//        Result byUserId = userClient.findByUserId(s);
//        Map<String,Object> userMap= (Map<String, Object>) byUserId.getData();
//        notice.setOperatorname(userMap.get("nickname").toString());
//
//        //设置文章名字
//        String targetid = notice.getTargetid();
//        Result clientById = articeClient.findById(targetid);
//        Map<String,Object> articeMap= (Map<String, Object>) clientById.getData();
//        notice.setTargetname(articeMap.get("title").toString());
    }

    public Notice selectById(String id) {
        QueryWrapper<Notice> noticeQueryWrapper=new QueryWrapper<>();
        noticeQueryWrapper.eq("id", id);
        Notice notice = noticeMapper.selectList(noticeQueryWrapper).get(0);
        info(notice);
        return notice;
    }

    public List<Notice> selectByList(Integer page, Integer size, Map<String,Object> map) {
        Page<Notice> page1=new Page<>(page,size,false);

        QueryWrapper<Notice> noticeQueryWrapper=new QueryWrapper<>();

        for (Map.Entry<String,Object> entry:map.entrySet()){
            noticeQueryWrapper.eq(entry.getKey(), entry.getValue());
        }
        Page<Notice> noticePage = noticeMapper.selectPage(page1, noticeQueryWrapper);
        List<Notice> records = noticePage.getRecords();

        for (Notice record : records) {
            info(record);
        }
        return records;
    }

    public void save(Notice notice) {
        //0未读消息 1已经读消息
        notice.setCreatetime(new Date());
        notice.setState("0");

        //通知消息入库
        notice.setId(idWorker.nextId() + "");
        noticeMapper.insert(notice);

        //新的通知提醒消息入库
        //NoticeFresh noticeFresh = new NoticeFresh();
        //noticeFresh.setNoticeId(notice.getId());
        //noticeFresh.setUserId(notice.getReceiverId());
        //noticeFreshDao.insert(noticeFresh);
    }

    public void updateById(Notice notice) {
        noticeMapper.updateById(notice);
    }

    public List<Notice> selectByUserId(String userId, Integer page, Integer size) {

        Page<NoticeFresh> noticePage=new Page<>(page, size,false );
        QueryWrapper<NoticeFresh> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        Page<NoticeFresh> page1 = noticeFreshMapper.selectPage(noticePage, queryWrapper);

        List<NoticeFresh> records = page1.getRecords();

        List<Notice> notices=new ArrayList<>();

        for (NoticeFresh fresh:records){
            notices.add(selectById(fresh.getNoticeid()));
        }

        return notices;

    }
    public void delete(NoticeFresh noticeFresh) {
        Map<String,Object> map=new HashMap<>();
        map.put("userId",noticeFresh.getUserid());
        map.put("noticeId",noticeFresh.getNoticeid());
        noticeFreshMapper.deleteByMap(map);
    }
}
