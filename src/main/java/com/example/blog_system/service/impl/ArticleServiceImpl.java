package com.example.blog_system.service.impl;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Statistic;
import com.example.blog_system.mapper.ArticleMapper;
import com.example.blog_system.mapper.CommentMapper;
import com.example.blog_system.mapper.StatisticMapper;
import com.example.blog_system.service.IArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.15
 */
@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Resource
    private RedisTemplate<String, Article> redisTemplate;

    @Override
    public PageInfo<Article> getArticles(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticles();
//目前查到的文章，只含有静态数据，没有动态的统计信息，因此要将统计信息封装到Article中
        for (Article article : articleList) {
//获取每篇文章的统计数据
            Statistic statistic = statisticMapper.selectByPrimaryKey(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return new PageInfo<>(articleList);
    }

    @Override
    public List<Article> getHeatArticle() {
        List<Statistic> list = statisticMapper.getStatistic();
        ArrayList<Article> arrayList = new ArrayList<>();
        int i = 0;
        for (Statistic statistic : list) {
//1.根据统计数据对应的文章id查询出其对应的文章
            Article article = articleMapper.selectById(statistic.getArticleId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
            arrayList.add(article);
            i = i + 1;
            if (i > 9) {
                break;
            }
        }
        return arrayList;

    }

    @Override
    public Article selectArticleById(Integer id) {
        Article article = null;
        Object o = redisTemplate.opsForValue().get("article" + id);
        if (o != null){
            article = (Article) o;
        } else {
            article = articleMapper.selectById(id);
            redisTemplate.opsForValue().set("article"+id, article);
        }
        return article;
    }

    @Override
    public void publishArticle(Article article) {
        //去除表情
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setCreated(new Date());
        article.setHits(0);
        article.setCommentsNum(0);

        articleMapper.insert(article);
        statisticMapper.addStatistic(article);
    }

    @Override
    public PageInfo<Article> selectArticlesByPage(int page, int count) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticles();
//目前查到的文章，只含有静态数据，没有动态的统计信息，因此要将统计信息封装到Article中
        for (Article article : articleList) {
//获取每篇文章的统计数据
            Statistic statistic = statisticMapper.selectByPrimaryKey(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return new PageInfo<>(articleList);
    }

    @Override
    public void updateArticleById(Article article) {
        article.setModified(new Date()) ;
        articleMapper.updateByPrimaryKeySelective(article);
        redisTemplate.delete("article"+article.getId());

    }

    @Override
    public void deleteArticleById(int id) {
    //1.删除文章
        articleMapper.deleteByPrimaryKey(id) ;//2.册除缓存
        redisTemplate.delete(" article"+id) ;//3.删除统计数据
        statisticMapper.deleteByPrimaryKey(id) ;//4.册除评论数据
        commentMapper.deleteByAid(id) ;

    }
}
