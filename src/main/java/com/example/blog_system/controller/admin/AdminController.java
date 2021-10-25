package com.example.blog_system.controller.admin;

import com.example.blog_system.entity.*;
import com.example.blog_system.service.*;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.19
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ISiteService siteService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private ITagsService iTagsService;
    @Autowired
    private IUserService iUserService;

    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        //获取后台所需数据
        List<Article> articles = siteService.recentArticles(5);
        List<Comment> comments = siteService.recentComments(5);

        StaticticsBo staticticsBo = siteService.getStatistics();

        request.setAttribute("articles", articles);
        request.setAttribute("comments", comments);
        request.setAttribute("statistics", staticticsBo);

        return "/back/index";
    }

    //跳转到文章发布
    @GetMapping("/article/toEditPage")
    public String toEditPage(HttpServletRequest request) {
        List<String> tags = iTagsService.getTags();
        request.setAttribute("catoegoriess", tags);
        return "back/article_edit";
    }

    //发布文章
    @PostMapping("/article/publish")
    @ResponseBody
    public ResponseData publishArticle(Article article) {
        if (StringUtils.isBlank(article.getCategories())) {
            article.setCategories("Java");
        }
        try {
            articleService.publishArticle(article);
            return ResponseData.ok();
        } catch (Exception e) {
            return ResponseData.fail();
        }
    }

    //跳转到后台文章列表页面
    @GetMapping("/article")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "count", defaultValue = "10") int count, HttpServletRequest request) {
        PageInfo<Article> pageInfo = articleService.selectArticlesByPage(page, count);
        request.setAttribute("articles", pageInfo);
        return "back/article_list";
    }

    @GetMapping("/comments")
    public String commentsManage(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "count", defaultValue = "10") int count, HttpServletRequest request) {

        PageInfo<Comment> comments = commentService.getAllComments(page, count);
        request.setAttribute("comments", comments);

        return "back/comments_list";
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public ResponseData deleteComment(@RequestParam int id) {
        try {
            commentService.deleteComment(id);
            return ResponseData.ok();
        } catch (Exception e) {
            return ResponseData.fail();
        }
    }

    @GetMapping("/tags")
    public String toTags(HttpServletRequest request, Model model) {

        List<Tag> tagList = iTagsService.getTagsCount();
        model.addAttribute("tagList", tagList);

        return "back/tags";
    }

    @GetMapping("tags/{tag}")
    public String showTagsArticles(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "count", defaultValue = "10") int count,
                                   @PathVariable("tag") String tag, HttpServletRequest request) {

        PageInfo<Article> pageInfo = iTagsService.getArticleByTag(page, count, tag);
        request.setAttribute("tagTitle", tag);
        request.setAttribute("articles", pageInfo);

        return "back/tag_article_list";
    }

    //跳转到文章修改页面
    @GetMapping("/article/{id}")
    public String editArticle(@PathVariable("id") Integer id, HttpServletRequest request) {
        Article article = articleService.selectArticleById(id);

        List<String> tags = iTagsService.getTags();
        request.setAttribute("catoegoriess", tags);
        request.setAttribute("contents", article);
        request.setAttribute("catoegories", article.getCategories());
        return "back/article_edit";
    }

    //修改文章
    @PostMapping("/article/modify")
    @ResponseBody
    public ResponseData modifyArticle(Article article) {
        try {
            articleService.updateArticleById(article);
            return ResponseData.ok();
        } catch (Exception e) {
            return ResponseData.fail();
        }
    }

    //册除文章
    @PostMapping("/article/delete")
    @ResponseBody
    public ResponseData deleteArticle(@RequestParam int id) {
        try {
            articleService.deleteArticleById(id);
            return ResponseData.ok();
        } catch (Exception e) {
            return ResponseData.fail();
        }
    }

    //系统设置
    //用户管理
    @GetMapping("/userSetting")
    public String toSetting(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "count", defaultValue = "10") int count,
                            HttpServletRequest request) {

        PageInfo<User> userPageInfo = iUserService.getAllUser(page, count);
        request.setAttribute("users", userPageInfo);

        return "back/user_setting";
    }
    @PostMapping("/modUser")
    @ResponseBody
    public ResponseData modUser(@RequestParam(value = "id") String id, @RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "authority") String authority) {
//    public ResponseData modUser(@RequestParam String username) {
        int auth = 2;
        if ("admin".equals(authority)) {
            auth = 1;
        }
        try {
            iUserService.updateUser(Integer.parseInt(id), username, password, email, auth);
            return ResponseData.ok();
        } catch (Exception e) {

            return ResponseData.fail();
        }
    }

    @PostMapping("/addUser")
    @ResponseBody
    public ResponseData addUser(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "authority") String authority) {

        try {
            iUserService.addUser(username, password, email, authority);
            return ResponseData.ok();
        } catch (Exception e) {
            return ResponseData.fail();
        }

    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public ResponseData delUser(@RequestParam int id) {
        try {
            iUserService.delUser(id);
            return ResponseData.ok();
        } catch (Exception e) {
            return ResponseData.fail();
        }
    }
    //修改网站信息

}
