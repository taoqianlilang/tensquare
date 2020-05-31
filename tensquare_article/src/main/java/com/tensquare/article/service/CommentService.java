package com.tensquare.article.service;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 14:38 2020/5/24
 * @Modified By:
 */
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Comment> findAll() {

        List<Comment> all = commentRepository.findAll();

        return all;
    }


    public Comment findById(String commentId) {

        Optional<Comment> id = commentRepository.findById(commentId);

        if (id.isPresent()){
            Comment comment = id.get();
            return comment;
        }
        return null;
    }

    public void save(Comment comment) {
        String id=idWorker.nextId()+"";
        comment.set_id(id);
        comment.setThumbup(0);
        comment.setPublishdate(new Date());
        commentRepository.save(comment);

    }

    public void update(Comment comment) {
        commentRepository.save(comment);
    }

    public void delete(String commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> findByArticleId(String articleId) {
        List<Comment> articleids = commentRepository.findByArticleid(articleId);
        return articleids;
    }

    public void thumbup(String commentId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(commentId));

        Update update = new Update();
        update.inc("thumbup", 1);

        mongoTemplate.updateFirst(query, update,"comment");

    }
}
