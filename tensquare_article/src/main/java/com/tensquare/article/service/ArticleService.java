package com.tensquare.article.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensquare.article.client.NoticeClient;
import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import com.tensquare.article.pojo.Notice;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NoticeClient noticeClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<Article> findAll() {
        Article article = articleDao.selectById(1);
        return articleDao.selectList(null);
    }

    public Article findById(String articleId) {
        return articleDao.selectById(articleId);
    }

    public void save(Article article) {
        //使用分布式id生成器
        String id = idWorker.nextId() + "";
        article.setId(id);

        //初始化数据
        article.setVisits(0);   //浏览量
        article.setThumbup(0);  //点赞数
        article.setComment(0);  //评论数

        //新增
        articleDao.insert(article);

        //TODO 使用jwt获取当前用户的userid，也就是文章作者的id
        String authorId = "3";

        //获取需要通知的读者
        String authorKey = "article_author_" + authorId;
        Set<String> set = redisTemplate.boundSetOps(authorKey).members();

        for (String uid : set) {
            //消息通知
            Notice notice = new Notice();
            notice.setReceiverid(uid);
            notice.setOperatorid(authorId);
            notice.setAction("publish");
            notice.setTargettype("article");
            notice.setTargetid(id);
            notice.setCreatetime(new Date());
            notice.setType("sys");
            notice.setState("0");

            noticeClient.save(notice);
        }
        rabbitTemplate.convertAndSend("article_subscribe", authorId, id);


    }

    public void updateById(Article article) {
        // 根据主键id修改
        articleDao.updateById(article);

        // 根据条件修改
        // 创建条件对象
        // EntityWrapper<Article> wrapper = new EntityWrapper<>();
        // 设置条件
        // wrapper.eq("id", article.getId());
        // articleDao.update(article, wrapper);
    }

    public void deleteById(String articleId) {
        articleDao.deleteById(articleId);
    }

    public Page<Article> findByPage(Map<String, Object> map, Integer page, Integer size) {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();

        for (Map.Entry<String,Object> entry : map.entrySet()){

           queryWrapper.eq(entry.getKey(), entry.getValue());

        }

        Page<Article> pageData = new Page<>(page, size,false);

        Page<Article> selectPage = articleDao.selectPage(pageData, queryWrapper);

        return selectPage;
    }

    public Boolean subscription(String userId, String articleId) {
        //根据文章id查出作者id
        String authorid = articleDao.selectById(articleId).getUserid();

        //创建rabbitmq管理器
        RabbitAdmin rabbitAdmin=new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        //direct 处理交换机
        DirectExchange directExchange=new DirectExchange("Article_subscription");
        rabbitAdmin.declareExchange(directExchange);
        //队列
        Queue queue = new Queue("article_subscribe_" + userId, true);

        //将队列绑定到交换机上
        Binding binding = BindingBuilder.bind(queue).to(directExchange).with(authorid);

        //用户订阅作者set集合
        String userKey="article_subscription"+userId;
        //作者看多少人订阅自己
        String authorKey="article_author_" + authorid;

        //查看用户是否订阅了该作者
        Boolean flage = redisTemplate.boundSetOps(userKey).isMember(authorid);

        if (flage){
            //已经订阅就取消订阅
            redisTemplate.boundSetOps(userKey).remove(authorid);
            redisTemplate.boundSetOps(authorKey).remove(userId);

            rabbitAdmin.removeBinding(binding);
            return false;
        }else {
            //没有订阅就添加订阅
            redisTemplate.boundSetOps(userKey).add(authorid);
            redisTemplate.boundSetOps(authorKey).add(userId);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(binding);
            return true;
        }

    }

    public void thumbup(String articleId,String userid) {
        //文章点赞
        Article article = articleDao.selectById(articleId);

        article.setThumbup(article.getThumbup()+1);

        articleDao.updateById(article);

        //消息通知
        Notice notice = new Notice();
        notice.setReceiverid(article.getUserid());
        notice.setOperatorid(userid);
        notice.setAction("thumbup");
        notice.setTargettype("article");
        notice.setTargetid(articleId);
        notice.setCreatetime(new Date());
        notice.setType("user");
        notice.setState("0");
        noticeClient.save(notice);
    }
}
