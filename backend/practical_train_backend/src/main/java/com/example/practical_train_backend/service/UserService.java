package com.example.practical_train_backend.service;

import com.example.practical_train_backend.dao.UserMapper;
import com.example.practical_train_backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> getAllUser(){
        try {
            return userMapper.getAll();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }

}
