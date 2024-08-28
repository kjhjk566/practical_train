package com.example.practical_train_backend.collector;


import com.example.practical_train_backend.entity.Experiment;
import com.example.practical_train_backend.entity.ResponseVO;
import com.example.practical_train_backend.service.AnomalyDataService;
import com.example.practical_train_backend.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/anomaly")
public class AnomalyDataCollector {
    @Autowired
    private AnomalyDataService anomalyDataService;

    @GetMapping("/get_all")
    public ResponseVO<List<Experiment>> getFile(){
        List<Experiment> arr= anomalyDataService.getAll();

        return ResponseVO.success(arr);
    }

    @GetMapping("/test")
    public String t(){
        JsonUtil jsonUtil = new JsonUtil();
        jsonUtil.main();
        return "kkkkk";
    }

}
