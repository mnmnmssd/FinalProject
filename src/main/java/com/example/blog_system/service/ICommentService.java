package com.example.blog_system.service;

import com.example.blog_system.entity.Comment;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ICommentService {
    public PageInfo<Comment> getComments(Integer aid, int page, int count);
    //用户发表评论
    public void pushComment(Comment comment);

    //获取所有评论
    public PageInfo<Comment> getAllComments(int page, int count);
    //删除评论
    public void deleteComment(Integer id);

}
