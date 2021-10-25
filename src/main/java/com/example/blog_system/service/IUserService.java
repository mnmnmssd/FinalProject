package com.example.blog_system.service;

import com.example.blog_system.entity.Article;
import com.example.blog_system.entity.User;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IUserService {
    //获取全部用户数据
    public PageInfo<User> getAllUser(int page, int count);
    //更新用户信息
    public void updateUser(int uId, String username, String password, String email, int auth);
    //删除用户
    public void delUser(int id);
    //添加用户
    public void addUser(String username, String password, String email, String authority);
}
