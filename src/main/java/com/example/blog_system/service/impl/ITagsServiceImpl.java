package com.example.blog_system.service.impl;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Tag;
import com.example.blog_system.mapper.TagsMapper;
import com.example.blog_system.service.ITagsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.19
 */
@Service
public class ITagsServiceImpl implements ITagsService {
    @Autowired
    private TagsMapper tagsMapper;

    @Override
    public List<Tag> getTagsCount() {
        List<Tag> tags = tagsMapper.selectCounts();
        tags.replaceAll(tag -> {
            tag.setLink("tags/" + tag.getText());
            return tag;
        });
        return tags;
    }

    @Override
    public PageInfo<Article> getArticleByTag(Integer page, Integer count, String categories) {

        List<Article> articles = tagsMapper.selectArticleByTag(categories);
        PageHelper.startPage(page, count);

        return new PageInfo<>(articles);
    }

    @Override
    public List<String> getTags() {
        return tagsMapper.getTags();
    }

}
