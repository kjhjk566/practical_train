package com.example.practical_train_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Experiment {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    private String username;
    private String experimentName;
    private Double smoothness;
    private Double periodicity;
    private Double correlation;
    private Integer metricNum;
    private String timeSeriesData;

}
