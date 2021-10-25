package com.example.blog_system.service;

import com.example.blog_system.entity.Article;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IArticleService {
    //分页查询文章列表
    public PageInfo<Article> getArticles(Integer page, Integer count) ;
    //获取热度排名前十的文章列表
    public List<Article> getHeatArticle();
    public Article selectArticleById(Integer id);
    //发布文章
    public void publishArticle(Article article);

    PageInfo<Article> selectArticlesByPage(int page, int count);

    //修改文章
    public void updateArticleById(Article article);
    //册除文章
    public void deleteArticleById(int id) ;

}
