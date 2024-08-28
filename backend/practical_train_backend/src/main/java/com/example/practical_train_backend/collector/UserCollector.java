package com.example.practical_train_backend.collector;

import com.example.practical_train_backend.entity.ResponseVO;
import com.example.practical_train_backend.entity.User;
import com.example.practical_train_backend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseVO<String> register(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        String password = userInfo.get("password");
        // 检查用户名是否已存在
        if (userService.isUserExists(username)) {
            return ResponseVO.error("Username already exists");
        }

        // 创建新用户
        boolean isRegistered = userService.registerUser(username, password);
        if (isRegistered) {
            return ResponseVO.success("Registration successful");
        } else {
            return ResponseVO.error("Registration failed");
        }
    }

}
