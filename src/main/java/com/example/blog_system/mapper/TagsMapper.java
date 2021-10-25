package com.example.blog_system.mapper;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.19
 */
@Mapper
public interface TagsMapper {

    //获取全部分类详细信息
    List<Tag> selectCounts();
    //获取某一分类下的文章
    List<Article> selectArticleByTag(String categories);
    //获取全部分类
    public List<String> getTags();
}
