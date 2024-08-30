package com.example.practical_train_backend.entity;

import lombok.Data;

import java.util.List;

@Data
public class AnomalyInfo {
    private List<Integer> globalIndex;
    private double globalRatio;
    private double globalFactor;
    private int globalRadius;

    private List<Integer> contextIndex;
    private double contextRatio;
    private double contextFactor;
    private int contextRadius;

    private List<Integer> patternIndex;
    private double patternRatio;
    private double patternFactor;
    private int patternRadius;
    private List<Integer> patterns;

    private List<Integer> trendIndex;
    private double trendRatio;
    private double trendFactor;
    private int trendRadius;

    private List<Integer> seasonalIndex;
    private double seasonalRatio;
    private double seasonalFactor;
    private int seasonalRadius;


}
