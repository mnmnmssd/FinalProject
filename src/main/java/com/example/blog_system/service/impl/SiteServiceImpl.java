package com.example.blog_system.service.impl;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Comment;
import com.example.blog_system.entity.StaticticsBo;
import com.example.blog_system.entity.Statistic;
import com.example.blog_system.mapper.ArticleMapper;
import com.example.blog_system.mapper.CommentMapper;
import com.example.blog_system.mapper.StatisticMapper;
import com.example.blog_system.service.ISiteService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.17
 */
@Service
public class SiteServiceImpl implements ISiteService {

    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void updateStatistics(Article article) {
        Statistic statistic = statisticMapper.selectByPrimaryKey(article.getId());
        statistic.setHits(statistic.getHits() + 1);
        statisticMapper.updateArticleHits(statistic);
    }

    @Override
    public List<Article> recentArticles(int limit) {
        PageHelper.startPage(1,limit>10||limit<1?10:limit);
        List<Article> articles = articleMapper.selectArticles();

        //封装文章
        for (Article article: articles) {
            Statistic statistic = statisticMapper.selectByPrimaryKey(article.getId());
            article.setCommentsNum(statistic.getCommentsNum());
        }

        return articles;
    }

    @Override
    public List<Comment> recentComments(int limit) {
        PageHelper.startPage(1,limit>10||limit<1?10:limit);
        return commentMapper.selectComments();
    }

    @Override
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo = new StaticticsBo();
        Integer articles = articleMapper.countArticle();
        Integer comments = commentMapper.countComment();
        staticticsBo.setArticles(articles);
        staticticsBo.setComments(comments);
        return staticticsBo;
    }
}
