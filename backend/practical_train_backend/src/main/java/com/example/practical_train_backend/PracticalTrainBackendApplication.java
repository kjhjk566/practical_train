package com.example.practical_train_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@MapperScan(basePackages = "com.example.practical_train_backend.dao")
public class PracticalTrainBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticalTrainBackendApplication.class, args);
    }


}
