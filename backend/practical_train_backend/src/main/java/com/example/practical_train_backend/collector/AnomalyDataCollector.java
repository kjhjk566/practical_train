package com.example.practical_train_backend.collector;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.practical_train_backend.dao.ExperimentMapper;
import com.example.practical_train_backend.entity.AnomalyInfo;
import com.example.practical_train_backend.entity.Experiment;
import com.example.practical_train_backend.entity.MTS;
import com.example.practical_train_backend.entity.ResponseVO;
import com.example.practical_train_backend.service.AnomalyDataService;
import com.example.practical_train_backend.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/anomaly")
public class AnomalyDataCollector {
    @Autowired
    private AnomalyDataService anomalyDataService;

    @Autowired
    private ExperimentMapper experimentMapper;

    @GetMapping("/get_all")
    public ResponseVO<List<Experiment>> getFile(){
        List<Experiment> arr= anomalyDataService.getAll();

        return ResponseVO.success(arr);
    }

    @GetMapping("/test")
    public ResponseVO<JSONObject> t(){
        JsonUtil jsonUtil = new JsonUtil();
        String myPath = "templateData/normal_1.json";
        String path = JsonUtil.class.getClassLoader().getResource(myPath).getPath();
        String s = jsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);

        JSONObject experiment = jobj.getJSONObject("normal_1");
        return ResponseVO.success(experiment);
    }

    @PostMapping("/inject")
    public ResponseVO<double [] []> inject(@RequestBody AnomalyInfo anomalyInfo){
//        JsonUtil jsonUtil = new JsonUtil();
//        String myPath = "templateData/normal_1.json";
//        String path = JsonUtil.class.getClassLoader().getResource(myPath).getPath();
        if (experimentMapper.ifExists(anomalyInfo.getUsername(),anomalyInfo.getLabName())){
            return ResponseVO.error("data exist");
        }
        Experiment e = experimentMapper.getByUsernameAndExperimentName(anomalyInfo.getUsername(),anomalyInfo.getSourceName());


        String s = e.getTimeSeriesData();
        JSONObject jobj = JSON.parseObject(s);
        JSONObject experimentJson = jobj.getJSONObject(anomalyInfo.getSourceName());
//        JSONObject data = experiment.getJSONObject("data");
        JSONArray jsonArray = experimentJson.getJSONArray("data");
        // 遍历JSON数组，提取二维数组中的数据
        double[][] doubleArray = new double[jsonArray.size()][];

        for (int i = 0; i < jsonArray.size(); i++) {
            // 获取当前的子数组
            JSONArray subArray = jsonArray.getJSONArray(i);
            int subLength = subArray.size();
            doubleArray[i] = new double[subLength];

            // 遍历子数组并填充到double类型的数组中
            for (int j = 0; j < subLength; j++) {
                doubleArray[i][j] = subArray.getDouble(j);
            }
        }


        MTS mts = new MTS(doubleArray);

        if (anomalyInfo.getGlobalIndex() != null) {
            for (Integer index : anomalyInfo.getGlobalIndex()) {
                anomalyDataService.pointGlobalOutliers(mts, index, anomalyInfo.getGlobalRatio(), anomalyInfo.getGlobalFactor(), anomalyInfo.getGlobalRadius());
            }
        }

        if (anomalyInfo.getContextIndex() != null) {
            for (Integer index : anomalyInfo.getContextIndex()) {
                anomalyDataService.pointContextualOutliers(mts, index, anomalyInfo.getContextRatio(), anomalyInfo.getContextFactor(), anomalyInfo.getContextRadius());
            }
        }

        if (anomalyInfo.getPatternIndex() != null) {
            String shape = anomalyInfo.getPatterns().size()==1 ? "square" : "triangle";
            for (Integer index : anomalyInfo.getPatternIndex()) {
                anomalyDataService.collectiveGlobalOutliers(mts, index, anomalyInfo.getPatternRatio(), anomalyInfo.getPatternFactor(), anomalyInfo.getPatternRadius(), shape, 1.5, 0.03, 20, 0.04, 0.0, new double[1]);
            }
        }

        if (anomalyInfo.getTrendIndex() != null) {
            for (Integer index : anomalyInfo.getTrendIndex()) {
                anomalyDataService.collectiveTrendOutliers(mts, index, anomalyInfo.getTrendRatio(), anomalyInfo.getTrendFactor(), anomalyInfo.getTrendRadius());
            }
        }

        if (anomalyInfo.getSeasonalIndex() != null) {
            for (Integer index : anomalyInfo.getSeasonalIndex()) {
                anomalyDataService.collectiveSeasonalOutliers(mts, index, anomalyInfo.getSeasonalRatio(), anomalyInfo.getSeasonalFactor(), anomalyInfo.getSeasonalRadius());
            }
        };

        JSONArray outerArray = new JSONArray();
        double[][] dataArray = mts.getData();
        // 遍历二维数组的每一行
        for (double[] row : dataArray) {
            // 创建内层的JSONArray
            JSONArray innerArray = new JSONArray();

            // 遍历行中的每个元素
            for (double value : row) {
                // 将元素添加到内层的JSONArray
                innerArray.add(value);
            }

            // 将内层的JSONArray添加到外层的JSONArray
            outerArray.add(innerArray);
        }

        JSONArray labelArray = new JSONArray();

        // 遍历一维数组，将每个元素添加到JSONArray中
        for (int value : mts.getLabel()) {
            labelArray.add(value);
        }

        experimentJson.put("data",outerArray);
        experimentJson.put("label", labelArray);
        jobj.put(anomalyInfo.getLabName(),experimentJson);
        jobj.remove(anomalyInfo.getSourceName());
        Experiment experiment = new Experiment();
        experiment.setUsername(anomalyInfo.getUsername());
        experiment.setExperimentName(anomalyInfo.getLabName());
        experiment.setSmoothness(((Number) experimentJson.get("smoothness")).doubleValue());
        experiment.setPeriodicity(((Number) experimentJson.get("periodicity")).doubleValue());
        experiment.setCorrelation(((Number) experimentJson.get("correlation")).doubleValue());
        System.out.println(experimentJson.toString());
        experiment.setMetricNum(((Number) experimentJson.get("metricsNum")).intValue());
        experiment.setTimeSeriesData(jobj.toString());
        experimentMapper.insertExperiment(experiment);


        return ResponseVO.success(experiment.getTimeSeriesData());
    }

}
