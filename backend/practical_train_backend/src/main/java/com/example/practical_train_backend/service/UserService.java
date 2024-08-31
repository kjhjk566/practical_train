package com.example.practical_train_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.practical_train_backend.dao.UserMapper;
import com.example.practical_train_backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> getAllUser() {
        try {
            return userMapper.getAll();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    // 检查用户名是否已存在
    public boolean isUserExists(String username) {
        try {
            User user = userMapper.findByUsername(username); // 假设有一个 findByUsername 方法
            return user != null;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    // 注册新用户
    public boolean registerUser(String username, String password) {
        try {
            // 对密码进行加密（示例中使用简单的MD5加密，实际应用中建议使用BCrypt等更安全的加密方式）
            String encryptedPassword = encryptPassword(password);
            System.out.println("Received request data: " + username);
            System.out.println("Received request data: " + password);
            // 创建用户对象
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(encryptedPassword);
            newUser.setEmail("123@qq.com");
            newUser.setDepartment("AIOps");
            System.out.println("Received request data: " + newUser);
            // 插入用户到数据库
            userMapper.insertUser(newUser); // 假设有一个 insert 方法
            return true;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    // 示例的简单密码加密方法，实际应使用更安全的加密方式
    private String encryptPassword(String password) {
        // 简单的MD5加密示例，实际应用中建议使用更强的加密算法
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashedText = number.toString(16);

            // 可能需要填充至32位
            while (hashedText.length() < 32) {
                hashedText = "0" + hashedText;
            }

            return hashedText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateUser(String username, String password) {
        try {
            // 查找用户
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
            if (user == null) {
                return false;
            }

            // 对输入的密码进行加密
            String encryptedPassword = encryptPassword(password);

            // 验证加密后的密码是否与数据库中的密码匹配
            return user.getPassword().equals(encryptedPassword);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
