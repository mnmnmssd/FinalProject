package com.example.blog_system.mapper;

import com.example.blog_system.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);

    //分页展示某个文章的评论
    List<Comment> selectByAid(Integer aid) ;

    //获取所有评论，按照新进行排序
    List<Comment> selectComments() ;
    //获取总评论数
    Integer countComment() ;

    int deleteByAid(Integer aid);

}