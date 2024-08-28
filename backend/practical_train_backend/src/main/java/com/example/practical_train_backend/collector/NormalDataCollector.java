package com.example.practical_train_backend.collector;

import com.example.practical_train_backend.entity.ResponseVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/data")
public class NormalDataCollector {

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
}
