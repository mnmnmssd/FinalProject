package com.example.blog_system.controller.client;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.Comment;
import com.example.blog_system.service.impl.ArticleServiceImpl;
import com.example.blog_system.service.impl.CommentServiceImpl;
import com.example.blog_system.service.impl.SiteServiceImpl;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.15
 */
@Controller
public class IndexController {
    @Autowired
    private ArticleServiceImpl articleService;
    @Autowired
    private SiteServiceImpl siteService;
    @Autowired
    private CommentServiceImpl commentService;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        return this.index(request, 1, 5);
    }

    @GetMapping("/page/{p}")
    public String index(HttpServletRequest request, @PathVariable("p") int page, @RequestParam(value = "count", defaultValue = "5") int count) {
        PageInfo<Article> pageInfo = articleService.getArticles(page, count);
        List<Article> heatArticle = articleService.getHeatArticle();
        request.setAttribute("articles", pageInfo);
        request.setAttribute("articleList", heatArticle);
        return "client/index";
    }

    @GetMapping(value = "/article/{id}")
    public String getAArticleById(@PathVariable("id") Integer id, HttpServletRequest request) {
        //1.获取点击文章对象
        Article article = articleService.selectArticleById(id);
        //当点击文章链接后，文章的点击量就改变了，统计数据就改变了
        if (article != null) {
            // 查询封装评论相关数据
            getArticleComments(request, article);
            //更新点击量
            siteService.updateStatistics(article);
            //获取文章对应评论
            getCommentById(request, article);
            request.setAttribute("article", article);
            return "client/articleDetails";
        } else {
            return "comm/error_404";
        }
    }

    private void getArticleComments(HttpServletRequest request, Article article) {
        if (article.getAllowComment()) {
            // cp 表示评论页码，commentPage
            String cp = request.getParameter("cp");
            cp = StringUtils.isBlank(cp) ? "1" : cp;
            request.setAttribute("cp", cp);
            PageInfo<Comment> comments = commentService.getComments(article.getId(),Integer.parseInt(cp),3);
            request.setAttribute("cp", cp);
            request.setAttribute("comments", comments); //页面变量名与"comments"保持一致
        }
    }

    private void getCommentById(HttpServletRequest request, Article article) {
        if (article.getAllowComment()) {
            //cp表示评论页码,从页面传来的参数
            String cp = request.getParameter("cp");
            cp= StringUtils.isBlank(cp)?"1":cp;
            request.setAttribute("cp", cp);
            PageInfo<Comment> comments = commentService.getComments(article.getId(), Integer.parseInt(cp), 3);
            request.setAttribute("camments", comments) ;

        }
    }

}
