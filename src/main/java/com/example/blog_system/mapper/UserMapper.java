package com.example.blog_system.mapper;

import com.example.blog_system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //获取全部用户
    List<User> getAllUser();
    //更新用户信息
    int updateUserById(@Param("id") Integer id, @Param("username") String username,
                       @Param("password") String password, @Param("email") String email);
    //更新用户权限
    int updateUserAuth(@Param("id") Integer id, @Param("auth") Integer auth);
    //添加用户权限
    int addUserAuth(@Param("id") Integer id, @Param("auth") Integer auth);
    //删除用户权限
    int delUserAuth(Integer id);
    //获取用户id
    int getUserId(String name);
}