package com.example.blog_system.service.impl;

import com.example.blog_system.entity.Comment;
import com.example.blog_system.entity.Statistic;
import com.example.blog_system.mapper.CommentMapper;
import com.example.blog_system.mapper.StatisticMapper;
import com.example.blog_system.service.ICommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.17
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> comments = commentMapper.selectByAid(aid);
        return new PageInfo<>(comments);
    }

    @Override
    public void pushComment(Comment comment) {
        //1.新增一条评论
        commentMapper.insert(comment);
        //2.更新文章统计数据
        Statistic statistic = statisticMapper.selectByPrimaryKey(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleComments(statistic);

    }

    @Override
    public PageInfo<Comment> getAllComments( int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> comments = commentMapper.selectComments();
        return new PageInfo<>(comments);
    }

    @Override
    public void deleteComment(Integer id) {
        commentMapper.deleteByPrimaryKey(id);
    }
}
