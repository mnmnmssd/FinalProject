package com.example.blog_system.service;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Tag;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ITagsService {

    //获取分类数量
    public List<Tag> getTagsCount();

    //获取某一分类下的文章
    public PageInfo<Article> getArticleByTag(Integer page, Integer count, String categories);

    //获取全部分类
    public List<String> getTags();
}
