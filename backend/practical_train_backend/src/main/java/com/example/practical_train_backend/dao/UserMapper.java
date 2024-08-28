package com.example.practical_train_backend.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practical_train_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user")
    List<User> getAll();

}
