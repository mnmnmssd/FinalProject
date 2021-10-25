package com.example.blog_system.service;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Comment;
import com.example.blog_system.entity.StaticticsBo;

import java.util.List;

public interface ISiteService {
    //更新某个文章的统计数据
    public void updateStatistics(Article article);

    //获取最新文章
    public List<Article> recentArticles(int limit);
    //获取最新评论
    public List<Comment> recentComments(int limit) ;
    //获取统计信息的总数
    public StaticticsBo getStatistics();

}
