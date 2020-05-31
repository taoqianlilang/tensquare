package com.tensquare.article.repository;
import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 14:34 2020/5/24
 * @Modified By:
 */
public interface CommentRepository extends MongoRepository<Comment,String> {

    /**
     * @param articleid
     * @return 多条数据
     * 通过文章id查询评论
     */
        List<Comment> findByArticleid(String articleid);

}
