package com.example.practical_train_backend.collector;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.practical_train_backend.dao.ExperimentMapper;
import com.example.practical_train_backend.entity.Experiment;
import com.example.practical_train_backend.entity.ResponseVO;
import com.example.practical_train_backend.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/data")
public class NormalDataCollector {

    @Autowired
    private ExperimentMapper experimentMapper;

    @PostMapping("/feature")
    public ResponseVO<List<Double>> changeFeatureValues(
            @RequestParam("smoothness") double featureValue1,
            @RequestParam("periodicity") double featureValue2,
            @RequestParam("correlation") double featureValue3,
            @RequestParam("metricNum") int metricNum) {

        // Calculate smoothness number
        int smoothnessNum;
        try {
            smoothnessNum = ThreadLocalRandom.current().nextInt(
                    (int) Math.ceil(featureValue1 * metricNum),
                    (int) Math.floor((featureValue1 + 0.1) * 10 / 10 * metricNum)
            );
        } catch (IllegalArgumentException e) {
            smoothnessNum = (int) Math.floor((featureValue1 + 0.1) * 10 / 10 * metricNum);
        }

        // Update smoothness value
        featureValue1 = (double) smoothnessNum / metricNum;

        // Check conditions and calculate periodicity
        if (featureValue2 < 0.1 && featureValue3 > 0.9) {
            featureValue2 = 0;
            featureValue3 = 0.9;
            return ResponseVO.success(Arrays.asList(featureValue1, featureValue2, featureValue3));
        }

        int periodicityNum;
        try {
            periodicityNum = ThreadLocalRandom.current().nextInt(
                    (int) Math.ceil(featureValue2 * metricNum),
                    (int) Math.floor((featureValue2 + 0.1) * 10 / 10 * metricNum)
            );
        } catch (IllegalArgumentException e) {
            periodicityNum = (int) Math.floor((featureValue2 + 0.1) * 10 / 10 * metricNum);
        }

        featureValue2 = (double) periodicityNum / metricNum;

        // Calculate correlation number and update
        int correlationNum;
        try {
            correlationNum = ThreadLocalRandom.current().nextInt(
                    (int) Math.ceil((1 - featureValue3) * metricNum),
                    (int) Math.floor((1 - featureValue3 + 0.1) * 10 / 10 * metricNum)
            );
        } catch (IllegalArgumentException e) {
            correlationNum = (int) Math.floor((1 - featureValue3 + 0.1) * 10 / 10 * metricNum);
        }

        featureValue3 = (double) (metricNum - correlationNum) / metricNum;

        return ResponseVO.success(Arrays.asList(featureValue1, featureValue2, featureValue3));
    }
    @PostMapping("/insert")
    public ResponseVO<Object> insertData(
            @RequestParam("labName") String labName,
            @RequestParam("username") String username,
            @RequestParam("smoothness") double smoothness,
            @RequestParam("periodicity") double periodicity,
            @RequestParam("correlation") double correlation,
            @RequestParam("metricNum") int metricNum) {
        JsonUtil jsonUtil = new JsonUtil();
        String myPath = "templateData/" + username;
        String path = JsonUtil.class.getClassLoader().getResource(myPath).getPath();
        String s = jsonUtil.readJsonFile(path);
        JSONObject jsonObject = JSON.parseObject(s);


        Experiment e = new Experiment();
        e.setCorrelation(correlation);
        e.setSmoothness(smoothness);
        e.setPeriodicity(periodicity);
        e.setUsername(username);
        e.setExperimentName(labName);
        e.setMetricNum(metricNum);
        e.setTimeSeriesData(s);
        return ResponseVO.success(experimentMapper.insertExperiment(e));
    }
    @GetMapping("/get_data")
    public ResponseVO<Object> getData(
            @RequestParam("labName") String labName,
            @RequestParam("username") String username) {
        if (experimentMapper.ifExists(username,labName)){
            return ResponseVO.success(experimentMapper.getByUsernameAndExperimentName(username,labName));
        }
        return ResponseVO.error("not get data");
    }

}
