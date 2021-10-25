package com.example.blog_system.mapper;

import com.example.blog_system.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);

    //查询文章列表
    public List<Article> selectArticles();
    //文章数量统计
    public Integer countArticle();

    Article selectById(Integer articleId);

    //新增文章对应的统计信息
    int addStatistic(Article article) ;

}