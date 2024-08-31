package com.example.practical_train_backend.entity;

import lombok.Data;

@Data
public class InsertInfo {
    private String username;
    private String labName;
    private double smoothness;
    private double periodicity;
    private double correlation;
    private int metricNum;
}
