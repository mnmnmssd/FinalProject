package com.example.blog_system.service.impl;

import com.example.blog_system.entity.User;
import com.example.blog_system.mapper.UserMapper;
import com.example.blog_system.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.20
 */
@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public PageInfo<User> getAllUser(int page, int count) {
        List<User> users = userMapper.getAllUser();
        PageHelper.startPage(page, count);
        return new PageInfo<>(users);
    }

    @Override
    public void updateUser(int uId, String username, String password, String email, int auth) {
        password = encoder.encode(password);
        userMapper.updateUserById(uId, username, password, email);
        userMapper.updateUserAuth(uId, auth);
    }

    @Override
    public void delUser(int id) {
        userMapper.deleteByPrimaryKey(id);
        userMapper.delUserAuth(id);
    }

    @Override
    public void addUser(String username, String password, String email, String authority) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEmail(email);
        user.setCreated(new Date());
        user.setValid(true);

        int auth = 2;
        if ("admin".equals(authority)) {
            auth = 1;
        }

        userMapper.insert(user);
        userMapper.addUserAuth(userMapper.getUserId(username), auth);

    }
}
