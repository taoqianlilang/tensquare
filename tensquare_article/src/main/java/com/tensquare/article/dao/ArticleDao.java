package com.tensquare.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tensquare.article.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ArticleDao extends BaseMapper<Article> {
}
