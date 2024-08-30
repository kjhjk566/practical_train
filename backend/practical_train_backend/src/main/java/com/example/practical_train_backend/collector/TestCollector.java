package com.example.practical_train_backend.collector;

import com.example.practical_train_backend.dao.ExperimentMapper;
import com.example.practical_train_backend.entity.Experiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/test")
public class TestCollector {
    @Autowired
    public ExperimentMapper mapper;

    @GetMapping("/test")
    public int hello() {
        Experiment experiment = mapper.getByUsernameAndExperimentName("user1", "Test Experiment");
        System.out.println(mapper.ifExists(experiment.getUsername(),experiment.getExperimentName()));
        return 4;



    }
}
