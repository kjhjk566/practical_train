package com.example.practical_train_backend.service;

import com.example.practical_train_backend.dao.ExperimentMapper;
import com.example.practical_train_backend.entity.Experiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.Arrays;

@Service
public class AnomalyDataService {

    private double[][] data;
    private int dim;
    private int STREAM_LENGTH;
    private int[] label;
    private double[][] dataOrigin;



    @Autowired
    private ExperimentMapper experimentMapper;

    public List<Experiment> getAll(){
        try {
            return experimentMapper.getAll();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void MultivariateDataGenerator(double[][] data) {
        this.data = data;
        this.dim = data.length;
        this.STREAM_LENGTH = data[0].length;
        this.label = new int[STREAM_LENGTH];
        this.dataOrigin = new double[dim][STREAM_LENGTH];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(data[i], 0, dataOrigin[i], 0, STREAM_LENGTH);
        }
    }

    private double std(double[] array) {
        double mean = Arrays.stream(array).average().orElse(0.0);
        double variance = Arrays.stream(array).map(v -> (v - mean) * (v - mean)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

    // 生成点全球异常值
    public void pointGlobalOutliers(int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int[] position = rand.ints((int) (STREAM_LENGTH * ratio), 0, STREAM_LENGTH).distinct().toArray();
        double max = Arrays.stream(data[dimNo]).max().orElse(Double.MAX_VALUE);
        double min = Arrays.stream(data[dimNo]).min().orElse(Double.MIN_VALUE);

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(STREAM_LENGTH, i + radius);
            double[] localWindow = Arrays.copyOfRange(dataOrigin[dimNo], start, end);
            double localStd = std(localWindow);
            data[dimNo][i] = dataOrigin[dimNo][i] * factor * localStd;

            if (data[dimNo][i] > max) data[dimNo][i] = max;
            if (data[dimNo][i] < min) data[dimNo][i] = min;

            label[i] = 1;
        }
    }

    // 生成点上下文异常值
    public void pointContextualOutliers(int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int[] position = rand.ints((int) (STREAM_LENGTH * ratio), 0, STREAM_LENGTH).distinct().toArray();
        double max = Arrays.stream(data[dimNo]).max().orElse(Double.MAX_VALUE);
        double min = Arrays.stream(data[dimNo]).min().orElse(Double.MIN_VALUE);

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(STREAM_LENGTH, i + radius);
            double[] localWindow = Arrays.copyOfRange(dataOrigin[dimNo], start, end);
            double localStd = std(localWindow);
            data[dimNo][i] = dataOrigin[dimNo][i] * factor * localStd;

            if (data[dimNo][i] > max) data[dimNo][i] = max * Math.min(0.95, Math.abs(rand.nextGaussian()));
            if (data[dimNo][i] < min) data[dimNo][i] = min * Math.min(0.95, Math.abs(rand.nextGaussian()));

            label[i] = 1;
        }
    }

    // 生成集体全球异常值
    public void collectiveGlobalOutliers(int dimNo, double ratio, double factor, int radius, String option, double coef, double noiseAmp, int level, double freq, double offset, double[] base) {
        Random rand = new Random();
        int[] position = rand.ints((int) (STREAM_LENGTH * ratio / (2 * radius)), 0, STREAM_LENGTH).distinct().toArray();

        if (!option.equals("square") && !option.equals("triangle") && !option.equals("other")) {
            throw new IllegalArgumentException("'option' must be one of 'square', 'triangle', or 'other'.");
        }

        double[] subData;
        if (option.equals("square")) {
            subData = squareSine(level, STREAM_LENGTH, freq, coef, offset, noiseAmp);
        } else if (option.equals("triangle")) {
            subData = squareTriangle(level, STREAM_LENGTH, freq, coef, offset, noiseAmp);
        } else {
            subData = collectiveGlobalSynthetic(STREAM_LENGTH, base, coef, noiseAmp);
        }

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(STREAM_LENGTH, i + radius);
            for (int j = start; j < end; j++) {
                data[dimNo][j] = (subData[j] * factor + data[dimNo][j]) / (1 + factor);
            }
            Arrays.fill(label, start, end, 1);
        }
    }

    // 生成集体趋势异常值
    public void collectiveTrendOutliers(int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int T = 300;
        int[] position = rand.ints((int) (STREAM_LENGTH * ratio / (2 * radius + T)), 0, STREAM_LENGTH).distinct().toArray();

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(STREAM_LENGTH, i + radius);
            double[] slope = new double[end - start];
            for (int j = 0; j < slope.length; j++) {
                slope[j] = (rand.nextBoolean() ? 1 : -1) * factor;
            }
            for (int j = start; j < end; j++) {
                data[dimNo][j] = dataOrigin[dimNo][j] + slope[j - start];
            }
            for (int j = end; j < STREAM_LENGTH; j++) {
                data[dimNo][j] += slope[slope.length - 1];
            }
            Arrays.fill(label, start, Math.min(end + T, STREAM_LENGTH), 1);
        }
    }

    // 分割数组
    private double[][] split(double[] array, int n) {
        int len = array.length;
        int chunkSize = (len + n - 1) / n;
        double[][] result = new double[n][];
        for (int i = 0; i < n; i++) {
            int start = i * chunkSize;
            int end = Math.min(len, start + chunkSize);
            result[i] = Arrays.copyOfRange(array, start, end);
        }
        return result;
    }

    // 生成集体季节性异常值
    public void collectiveSeasonalOutliers(int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int[] position = rand.ints((int) (STREAM_LENGTH * ratio / (2 * radius)), 0, STREAM_LENGTH).distinct().toArray();

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(STREAM_LENGTH, i + radius);
            int length = end - start;
            double[] tmp = Arrays.copyOfRange(data[dimNo], start, end);
            double[][] splitData = split(tmp, length / (int) factor + 1);
            double[] result = new double[tmp.length];
            int idx = 0;
            for (double[] chunk : splitData) {
                for (double v : chunk) {
                    if (idx < result.length) {
                        result[idx++] = v;
                    }
                }
            }
            int[] indices = rand.ints(result.length, 0, result.length).distinct().toArray();
            Arrays.sort(indices);
            for (int j = indices.length - 1; j >= 0; j--) {
                int index = indices[j];
                result[index] = result[result.length - 1];
                result[result.length - 1] = 0;
            }
            System.arraycopy(result, 0, data[dimNo], start, length);
            Arrays.fill(label, start, end, 1);
        }
    }

    // 辅助方法：生成集体全球合成数据
    private double[] collectiveGlobalSynthetic(int length, double[] base, double coef, double noiseAmp) {
        double norm = std(base);
        double[] value = new double[length];
        int num = length / base.length;
        for (int i = 0; i < num; i++) {
            System.arraycopy(base, 0, value, i * base.length, base.length);
        }
        int residual = length - (num * base.length);
        System.arraycopy(base, 0, value, num * base.length, residual);

        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            double noise = rand.nextGaussian();
            value[i] = coef * value[i] + noiseAmp * noise;
        }
        return value;
    }

    // 方法用于进行序列分割
    public static List<int[]> seriesSegmentation(int[] data, int stepsize) {
        List<int[]> result = new ArrayList<>();
        int start = 0;

        for (int i = 1; i < data.length; i++) {
            if (data[i] - data[i - 1] != stepsize) {
                result.add(Arrays.copyOfRange(data, start, i));
                start = i;
            }
        }
        result.add(Arrays.copyOfRange(data, start, data.length));
        return result;
    }

    // 方法用于生成三角波形
    public static double[] triangle(int length, double freq, double coef, double offset, double noiseAmp) {
        int[] timestamp = IntStream.range(0, length).toArray();
        int periodNum = (int) (length * freq);
        int periodLen = length / periodNum;
        periodLen = Math.max(periodLen, 1);

        double[] value = new double[length];
        for (int i = 0; i < length; i++) {
            if ((int) (i * freq * 2) % 2 == 0) {
                value[i] = i - (i / periodLen) * periodLen;
            } else {
                value[i] = (i / periodLen + 1) * periodLen - i;
            }
        }

        // 添加噪声
        if (noiseAmp != 0) {
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                value[i] += noiseAmp * random.nextGaussian();
            }
        }

        // 应用系数和偏移
        for (int i = 0; i < length; i++) {
            value[i] = coef * value[i] + offset;
        }
        return value;
    }

    // 生成带有噪声的正弦波
    public static double[] sine(int length, double freq, double coef, double offset, double noiseAmp) {
        double[] value = new double[length];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            value[i] = Math.sin(2 * Math.PI * freq * i);
            if (noiseAmp != 0) {
                value[i] += noiseAmp * random.nextGaussian();
            }
            value[i] = coef * value[i] + offset;
        }

        return value;
    }

    // 生成带有噪声的余弦波
    public static double[] cosine(int length, double freq, double coef, double offset, double noiseAmp) {
        double[] value = new double[length];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            value[i] = Math.cos(2 * Math.PI * freq * i);
            if (noiseAmp != 0) {
                value[i] += noiseAmp * random.nextGaussian();
            }
            value[i] = coef * value[i] + offset;
        }

        return value;
    }

    // 生成方形正弦波
    public static double[] squareSine(int level, int length, double freq, double coef, double offset, double noiseAmp) {
        double[] value = new double[length];
        Arrays.fill(value, 0.0);

        for (int i = 0; i < level; i++) {
            double factor = 1.0 / (2 * i + 1);
            double[] sineWave = sine(length, freq * (2 * i + 1), coef, offset, noiseAmp);
            for (int j = 0; j < length; j++) {
                value[j] += factor * sineWave[j];
            }
        }
        return value;
    }

    // 生成方形三角波
    public static double[] squareTriangle(int level, int length, double freq, double coef, double offset, double noiseAmp) {
        double[] value = new double[length];
        Arrays.fill(value, 0.0);

        for (int i = 0; i < level; i++) {
            double factor = 1.0 / (2 * i + 1);
            double[] triangleWave = triangle(length, freq * (2 * i + 1), coef, offset, noiseAmp);
            for (int j = 0; j < length; j++) {
                value[j] += factor * triangleWave[j];
            }
        }
        return value;
    }

    // 计算数组的欧几里得范数
    private static double norm(double[] array) {
        double sum = 0.0;
        for (double v : array) {
            sum += v * v;
        }
        return Math.sqrt(sum);
    }
}
