package com.example.blog_system.mapper;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Statistic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatisticMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Statistic record);

    int insertSelective(Statistic record);

    Statistic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Statistic record);

    int updateByPrimaryKey(Statistic record);

    //添加：统计文章总访问量
    long getTotalHit();
    //添加：统计文章总评论量
    long getTotalComment();
    //添加：统计文章热度信息（排序规则：先按点击量；再按评论数）
    List<Statistic> getStatistic();

    //更新文章点击量
    int updateArticleHits(Statistic statistic);

    int updateArticleComments(Statistic statistic);

    int addStatistic(Article article);


}