package com.example.practical_train_backend.collector;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.practical_train_backend.entity.Experiment;
import com.example.practical_train_backend.entity.MTS;
import com.example.practical_train_backend.entity.ResponseVO;
import com.example.practical_train_backend.service.AnomalyDataService;
import com.example.practical_train_backend.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.alibaba.fastjson.JSONObject;
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
    public ResponseVO<JSONObject> t(){
        JsonUtil jsonUtil = new JsonUtil();
        String myPath = "templateData/normal_1.json";
        String path = JsonUtil.class.getClassLoader().getResource(myPath).getPath();
        String s = jsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);

        JSONObject experiment = jobj.getJSONObject("normal_1");
        return ResponseVO.success(experiment);
    }

    @GetMapping("/inject")
    public ResponseVO<MTS> inject(){
        JsonUtil jsonUtil = new JsonUtil();
        String myPath = "templateData/normal_1.json";
        String path = JsonUtil.class.getClassLoader().getResource(myPath).getPath();
        String s = jsonUtil.readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);
        JSONObject experiment = jobj.getJSONObject("normal_1");
//        JSONObject data = experiment.getJSONObject("data");
        JSONArray jsonArray = experiment.getJSONArray("data");
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
//        anomalyDataService.MultivariateDataGenerator(doubleArray);
//        anomalyDataService.pointGlobalOutliers(mts, 0, 0.01, 3.5, 5);
//        anomalyDataService.pointContextualOutliers(mts, 1, 0.01, 5, 5);
//        anomalyDataService.collectiveGlobalOutliers(mts, 2, 0.1, 10, 20, "square", 1.5, 0.03, 20, 0.04, 0.0, new double[1]);

        anomalyDataService.collectiveTrendOutliers(mts, 4, 0.3, 0.5, 20);
//        anomalyDataService.collectiveSeasonalOutliers(mts, 3, 0.2, 10, 100);
        return ResponseVO.success(mts);
    }

}
