# Spring boot (扩展)博客系统

## 主要扩展功能

` 在老师已有基础上扩展 `

![image-20211019211243949](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211019211243949.png)

## 0x00评论管理
- 效果图（前端页面由文章管理页面更改部分参数而成）

![效果图](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211019211832076.png)

分析效果图，至少需要实现以下两功能

- 点击`评论管理`进入评论管理页面，并显示全部评论
- 可以对单一评论进行操作，并且点击评论可以跳转对应文章

分析效果图，分析得到的功能由Controller层承担的，由此开始向下分析

![image-20211019213430107](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211019213430107.png)

### 显示全部评论

#### Mapper层

- CommentMapper.java

```java
//获取所有评论，按照新发布进行排序
List<Comment> selectComments() ;
```
- CommentMapper.xml

```xml
  <select id="selectComments" resultType="com.example.blog_system.entity.Comment">
    select * from t_comment order by id desc
  </select>
```

#### Service层

- CommentServiceImpl
```java
    @Override
    public PageInfo<Comment> getAllComments( int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> comments = commentMapper.selectComments();
        return new PageInfo<>(comments);
    }
```

#### Controller层

- AdminController 

```java
@GetMapping("/comments")
public String commentsManage(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "count", defaultValue = "10") int count, HttpServletRequest request) {

    PageInfo<Comment> comments = commentService.getAllComments(page, count);
    request.setAttribute("comments", comments);

    return "back/comments_list";
}
```

### 对评论进行操作

#### Mapper层

- CommentMapper.java

```java
int deleteByAid(Integer aid);
```

- CommentMapper.xml

```xml
<delete id="deleteByAid">
  delete from t_comment
  where article_id = #{aid, jdbcType=INTEGER}
</delete>
```

#### Service层

- CommentServiceImpl

```java
@Override
public void deleteComment(Integer id) {
    commentMapper.deleteByPrimaryKey(id);
}
```

#### Controller层

- AdminController

```java
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
```

## 0x01标签/分类

- 效果图

![image-20211019214913227](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211019214913227.png)

分析该效果图，可知主要元素为中间展示的`词云`部分，该`词云`以分类权重为标识，权重越大，字符越大，在此处我们使用外部js库实现。

#### JQcloud库

我们借助外部库来实现`词云`效果，这里简单介绍一下`JQcloud`库，他是一个基于`Jquery`的简单易用的词云生成库，通过阅读他的[官方文档](https://github.com/lucaong/jQCloud)我们可以得知它接受对象数组类型的参数,主要参数有

| 参数   | 含义                     |
| ------ | ------------------------ |
| text   | 主要参数，这里用分类名   |
| weight | 权重，只用分类下的文章数 |
| link   | 跳转链接                 |

使用其内置方法添加词云

```javascript
$("#example").jQCloud(word_array);
```

根据以上数据，我们分析得到的信息

- 后台需要传过来分类的类别名，权重，链接等
- 前端接收数据并通过`JQcloud`渲染词云

通过分析，我们新建一个`Tag`实体类，并以此延申其的`Service层`、`Mapper层`等

- Tag类

```java
package com.example.blog_system.entity;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.19
 */
public class Tag {
    private String text;
    private int weight;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
```

### 显示所有分类

#### Mapper层

- TagsMapper.java

```java
//获取全部分类详细信息
List<Tag> selectCounts();
```

- TagsMapper.xml

```xml
<select id="selectCounts" resultType="com.example.blog_system.entity.Tag">
    SELECT categories as text, COUNT(categories) as weight
    FROM `t_article` GROUP BY categories
</select>
```

#### Service层

- tagsService

```
@Override
public List<Tag> getTagsCount() {
    List<Tag> tags = tagsMapper.selectCounts();
    tags.replaceAll(tag -> {
        tag.setLink("tags/" + tag.getText());
        return tag;
    });
    return tags;
}
```



#### Controller层

- Admincontroller

```
@GetMapping("/tags")
public String toTags(HttpServletRequest request, Model model) {

    List<Tag> tagList = iTagsService.getTagsCount();
    model.addAttribute("tagList", tagList);

    return "back/tags";
}
```

#### 前端页面

- 从官方git主页下载`JQcloud`库并放入`static/back/js(css)/`下

- 引入必备库

```javascript
<script th:src="@{http://apps.bdimg.com/libs/jquery/1.9.1/jquery.min.js}"></script>
<link th:href="@{/back/css/jqcloud.css}" rel="stylesheet"/>
<script th:src="@{/back/js/jqcloud-1.0.4.min.js}"></script>
```

- 页面布局

```html
<div id="wrapper">
    <div th:replace="back/header::header-body"></div>
    <div class="content-page">
        <div class="content">
            <div id="example" class="container">

            </div>
        </div>
        <div>
            <div th:replace=" back/footer :: footer-content"></div>
        </div>
    </div>
</div>
<div th:replace="back/footer :: footer"></div>
```

- JQcloud渲染数据

```javascript
<script th:inline="javascript" type="text/javascript">
    var tagList = [[${tagList}]];
    console.log(tagList)

    var word_array = tagList
    $(function () {
        $("#example").jQCloud(word_array, {
            height: 850 //设置生成词云的高度
        });
    });
</script>
```

### 单独显示某一分类下文章

用户点击某一分类后跳转到该分类下所有文章列表

- 效果图

![image-20211020143758198](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211020143758198.png)

分析该效果图，可得知需向后台传递分类参数，后台返回分类下所有文章列表

#### Mapper层

- TagsMapper.java

```java
//获取某一分类下的文章
List<Article> selectArticleByTag(String categories);
```

- TagsMapper.xml

```xml
<select id="selectArticleByTag" resultType="com.example.blog_system.entity.Article"  parameterType="java.lang.String" >
    SELECT *
    FROM `t_article`
    where categories = #{categories,jdbcType=VARCHAR}
</select>
```

#### Service层

- TagService

```java
@Override
public PageInfo<Article> getArticleByTag(Integer page, Integer count, String categories) {

    List<Article> articles = tagsMapper.selectArticleByTag(categories);
    PageHelper.startPage(page, count);

    return new PageInfo<>(articles);
}
```

#### Controller层

- AdminController

```java
@GetMapping("tags/{tag}")
public String showTagsArticles(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "count", defaultValue = "10") int count,
                               @PathVariable("tag") String tag, HttpServletRequest request) {

    PageInfo<Article> pageInfo = iTagsService.getArticleByTag(page, count, tag);
    request.setAttribute("tagTitle", tag);
    request.setAttribute("articles", pageInfo);

    return "back/tag_article_list";
}
```

#### 前端页面

由文章管理更改而来，不再赘述

#### 优化发布文章

由于之前发布文章时没有设置文章分类，导致所有文章分类都是默认分类，所以将其优化。

- 效果图

![image-20211020145017008](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211020145017008.png)

![image-20211020145041439](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211020145041439.png)

通过分析上图可得，需要在前端页面添加一个可以输入并且可以选择分类的输入框，选择框可以动态的获取所有分类，输入框可以添加新分类。

这种可以输入并且可以选择的`input`输入框初看很新颖，但是我们细细一想就可以得到方案，将输入框定位到选择框上，并且在选择框中内容发生改变时同步到输入框中。于是得到以下代码

##### 优化方案

- article_edit.html

```html
<th:block th:unless="${null != contents}">
    <select class="form-control" id="spec_num" name="spec_num"
            onchange="document.getElementById('categories').value=$('#spec_num option:selected').text()">
    </select>
    <input name="categories" id="categories" type="text" class="form-control selectInput"
           placeholder="请选择/新建文章分类"/>
</th:block>
```

- css部分

```css
.selectInput {
    position: absolute;
    width: 85%;
    height: 34px;
    left: 0px;
    top: 0px;
    border-bottom: 0px;
    border-right: 0px;
    border-left: 0px;
    border-top: 0px;
}
```

- js部分，获取后台数据

```javascript
var catoegoriess = [[${catoegoriess}]];
//根据id查找对象，
var obj = document.getElementById('spec_num');

catoegoriess.forEach(function (tag) {
    //添加一个选项
    obj.options.add(new Option(tag, tag)); //这个兼容IE与firefox
})
```

- 修改发布文章article.js的提交语句

```javascript
var catoegories = $('#articleForm input[name=categories]').val()
...
$("#articleForm #categories").val(catoegories);
```

#### 后台优化分类数据

- Mapper层

```java
//获取全部分类
public List<String> getTags();
```

- Service层

```java
@Override
public List<String> getTags() {
    return tagsMapper.getTags();
}
```

- Controller层

在文章发布api下添加

```java
List<String> tags = iTagsService.getTags();
request.setAttribute("catoegoriess", tags);
```

## 0x02系统设置

### 用户管理

- 效果图

![image-20211021165708369](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211021165708369.png)

![image-20211021165744593](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211021165744593.png)

![image-20211021165805221](https://cdn.jsdelivr.net/gh/mnmnmssd/hexoBlogimg/blog/2020/image-20211021165805221.png)

#### 遇到的错误及问题

在开发中遇到了`405`错误，后端接口一直返回`Request method 'POST' not supported`错误，而在controller中已经声明了前端请求方法为`POST`，在查询资料与debug中发现，原来fetch在请求后端时并没有设置请求头`hearder`，于是在fetch设置中向请求头中加入

```json
headers: {
    "Content-Type":"application/x-www-form-urlencoded"
}
```

但是仍报`405`错误，分析请求头信息，发现没有设置用户`token`导致后台的安全机制认为有用户发起`CSRF`攻击，所以采取反制措施。于是再次添加请求头信息

```javascript
// 获取<meta>标签中封装的_csrf信息
var token = $("meta[name='_csrf']").attr("content");
 ...
headers: {
    "Content-Type":"application/x-www-form-urlencoded",
    "x-csrf-token":token
}
```

这里简单了解一下 跨站请求伪造`Cross Site Request Forgery（**CSRF**）`，正是如同他的名字一样，当黑客获取用户cookie后，利用此cookie在本地或服务器伪造原网站向后端发起恶意请求。于是为了防范此种行为，`token`就诞生了，首先他由后端随机生成一串数字并发送给前端，当前端向后端发起请求时，在携带cookie的同时，还要将之前的随机数字串即`token`一并发送给后端。

第一步：后端随机产生一个token，把这个token保存在SESSION状态中；同时，后端把这个token交给前端页面；

第二步：下次前端需要发起请求（比如发帖）的时候把这个token加入到请求数据或者头信息中，一起传给后端；

第三步：后端校验前端请求带过来的token和SESSION里的token是否一致；

#### 添加用户

#### 删除/修改用户

### 个人信息管理

#### 修改个人信息

### 网站信息管理

#### 修改网站信息





