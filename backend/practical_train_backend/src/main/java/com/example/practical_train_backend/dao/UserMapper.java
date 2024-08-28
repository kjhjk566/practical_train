package com.example.practical_train_backend.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practical_train_backend.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user")
    List<User> getAll();

    // 根据用户名查找用户
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    // 插入新用户
    @Insert("INSERT INTO user (username, password,email,department) VALUES (#{username}, #{password}, #{email}, #{department})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 如果你的数据库主键是自增的，自动生成ID
    int insertUser(User user);
}

