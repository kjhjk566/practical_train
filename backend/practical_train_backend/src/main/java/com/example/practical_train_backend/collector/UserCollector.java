package com.example.practical_train_backend.collector;

import com.example.practical_train_backend.entity.ResponseVO;
import com.example.practical_train_backend.entity.User;
import com.example.practical_train_backend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserCollector {
    @Autowired
    private UserService userService;

    @GetMapping("/get_all")
    public ResponseVO<List<User>> getAllUser(){
        List<User> arr= userService.getAllUser();

        return ResponseVO.success(arr);
    }

    @GetMapping("/test")
    public String t(){
        return "kkkkk";
    }
}
