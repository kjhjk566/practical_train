package com.example.practical_train_backend.service;

import com.example.practical_train_backend.dao.ExperimentMapper;
import com.example.practical_train_backend.entity.Experiment;
import com.example.practical_train_backend.entity.MTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.util.stream.IntStream;

@Service
public class AnomalyDataService {




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

    private double std(double[] array) {
        double mean = Arrays.stream(array).average().orElse(0.0);
        double variance = Arrays.stream(array).map(v -> (v - mean) * (v - mean)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

    // 生成点全球异常值
    public void pointGlobalOutliers(MTS mts, int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int[] position = rand.ints((int) (mts.getSTREAM_LENGTH() * ratio), 0, mts.getSTREAM_LENGTH()).distinct().toArray();
        double max = Arrays.stream(mts.getData()[dimNo]).max().orElse(Double.MAX_VALUE);
        double min = Arrays.stream(mts.getData()[dimNo]).min().orElse(Double.MIN_VALUE);

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(mts.getSTREAM_LENGTH(), i + radius);
            double[] localWindow = Arrays.copyOfRange(mts.getDataOrigin()[dimNo], start, end);
            double localStd = std(localWindow);
            mts.getData()[dimNo][i] = mts.getDataOrigin()[dimNo][i] * factor * localStd;

            if (mts.getData()[dimNo][i] > max) mts.getData()[dimNo][i] = max;
            if (mts.getData()[dimNo][i] < min) mts.getData()[dimNo][i] = min;

            mts.getLabel()[i] = 1;
        }
    }

    // 生成点上下文异常值
    public void pointContextualOutliers(MTS mts, int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int[] position = rand.ints((int) (mts.getSTREAM_LENGTH() * ratio), 0, mts.getSTREAM_LENGTH()).distinct().toArray();
        double max = Arrays.stream(mts.getData()[dimNo]).max().orElse(Double.MAX_VALUE);
        double min = Arrays.stream(mts.getData()[dimNo]).min().orElse(Double.MIN_VALUE);

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(mts.getSTREAM_LENGTH(), i + radius);
            double[] localWindow = Arrays.copyOfRange(mts.getDataOrigin()[dimNo], start, end);
            double localStd = std(localWindow);
            mts.getData()[dimNo][i] = mts.getDataOrigin()[dimNo][i] * factor * localStd;

            if (mts.getData()[dimNo][i] > max) mts.getData()[dimNo][i] = max * Math.min(0.95, Math.abs(rand.nextGaussian()));
            if (mts.getData()[dimNo][i] < min) mts.getData()[dimNo][i] = min * Math.min(0.95, Math.abs(rand.nextGaussian()));

            mts.getLabel()[i] = 1;
        }
    }

    // 生成集体全球异常值
    public void collectiveGlobalOutliers(MTS mts, int dimNo, double ratio, double factor, int radius, String option, double coef, double noiseAmp, int level, double freq, double offset, double[] base) {
        Random rand = new Random();
        int[] position = rand.ints((int) (mts.getSTREAM_LENGTH() * ratio / (2 * radius)), 0, mts.getSTREAM_LENGTH()).distinct().toArray();

        if (!option.equals("square") && !option.equals("triangle") && !option.equals("other")) {
            throw new IllegalArgumentException("'option' must be one of 'square', 'triangle', or 'other'.");
        }

        double[] subData;
        if (option.equals("square")) {
            subData = squareSine(level, mts.getSTREAM_LENGTH(), freq, coef, offset, noiseAmp);
        } else if (option.equals("triangle")) {
            subData = squareTriangle(level, mts.getSTREAM_LENGTH(), freq, coef, offset, noiseAmp);
        } else {
            subData = collectiveGlobalSynthetic(mts.getSTREAM_LENGTH(), base, coef, noiseAmp);
        }

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(mts.getSTREAM_LENGTH(), i + radius);
            for (int j = start; j < end; j++) {
                mts.getData()[dimNo][j] = (subData[j] * factor + mts.getData()[dimNo][j]) / (1 + factor);
            }
            Arrays.fill(mts.getLabel(), start, end, 1);
        }
    }

    // 生成集体趋势异常值
//    public void collectiveTrendOutliers(MTS mts, int dimNo, double ratio, double factor, int radius) {
//        Random rand = new Random();
//        int T = 300;
//        int[] position = rand.ints((int) (mts.getSTREAM_LENGTH() * ratio / (2 * radius + T)), 0, mts.getSTREAM_LENGTH()).distinct().toArray();
//
//        for (int i : position) {
//            int start = Math.max(0, i - radius);
//            int end = Math.min(mts.getSTREAM_LENGTH(), i + radius);
//            double[] slope = new double[end - start];
//            for (int j = 0; j < slope.length; j++) {
//                slope[j] = (rand.nextBoolean() ? 1 : -1) * factor;
//            }
//            for (int j = start; j < end; j++) {
//                mts.getData()[dimNo][j] = mts.getDataOrigin()[dimNo][j] + slope[j - start];
//            }
//            for (int j = end; j < mts.getSTREAM_LENGTH(); j++) {
//                mts.getData()[dimNo][j] += slope[slope.length - 1];
//            }
//            Arrays.fill(mts.getLabel(), start, Math.min(end + T, mts.getSTREAM_LENGTH()), 1);
//        }
//    }

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
    public void collectiveSeasonalOutliers(MTS mts, int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int[] position = rand.ints((int) (mts.getSTREAM_LENGTH() * ratio / (2 * radius)), 0, mts.getSTREAM_LENGTH()).distinct().toArray();

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(mts.getSTREAM_LENGTH(), i + radius);
            int length = end - start;
            double[] tmp = Arrays.copyOfRange(mts.getData()[dimNo], start, end);
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
            System.arraycopy(result, 0, mts.getData()[dimNo], start, length);
            Arrays.fill(mts.getLabel(), start, end, 1);
        }
    }

    public void collectiveTrendOutliers(MTS mts, int dimNo, double ratio, double factor, int radius) {
        Random rand = new Random();
        int T = 100;
        // 生成位置数组，表示趋势异常的位置
        int numRandoms = (int) Math.round(mts.getSTREAM_LENGTH() * ratio / (2 * radius + T));

        // 生成随机数，范围在 [0, STREAM_LENGTH)
        int[] position = rand.ints(numRandoms, 0, mts.getSTREAM_LENGTH()).toArray();

        // 打印结果
//        for (int pos : position) {
//            System.out.println(pos);
//        }
        for (int i : position) {
            // 确定异常的起始和结束位置
            int start = Math.max(0, i - radius);
            int end = Math.min(mts.getSTREAM_LENGTH(), i + radius);


            // 创建趋势的斜率，随机选择正或负趋势
            double slopeSign = rand.nextBoolean() ? 1 : -1;
            double[] slope = new double[end - start];
            for (int j = 0; j < slope.length; j++) {
                slope[j] = slopeSign * factor * j;
            }

            // 将斜率应用于数据
            for (int j = start; j < end; j++) {
                mts.getData()[dimNo][j] = mts.getDataOrigin()[dimNo][j] + slope[j - start];
            }

            // 在异常的末尾继续应用趋势
            for (int j = end; j < mts.getSTREAM_LENGTH(); j++) {
                mts.getData()[dimNo][j] += slope[slope.length - 1];
            }

            // 设置标签，标记哪些位置是异常值
            int labelEnd = Math.min(end + T, mts.getSTREAM_LENGTH());
            System.out.println(start);
            System.out.println(labelEnd);
            Arrays.fill(mts.getLabel(), start, labelEnd, 1);
        }
    }

    public void collectiveSeasonalOutliers(MTS mts, int dimNo, double ratio, int factor, int radius) {
        Random rd = new Random();
        int positionSize = (int) Math.round(mts.getSTREAM_LENGTH() * ratio / (2 * radius));
        int[] position = new int[positionSize];

        for (int i = 0; i < positionSize; i++) {
            position[i] = (int) (Math.random() * mts.getSTREAM_LENGTH());
        }

        for (int i : position) {
            int start = Math.max(0, i - radius);
            int end = Math.min(mts.getSTREAM_LENGTH(), i + radius);
            int length = end - start;
            List<Double> tmp = new ArrayList<>();

            for (int j = start; j < end; j++) {
                tmp.add(mts.getData()[dimNo][j]);
            }

            List<Double> result = new ArrayList<>();
            for (int j = 0; j < factor; j++) {
                Collections.addAll(result, tmp.toArray(new Double[0]));
            }

            // Randomly select elements from the result list
            for (int j = 0; j < result.size(); j++) {
                double value = rd.nextDouble();
                int index = (int) (value * result.size());
                result.set(j, result.get(index));
            }

            // Randomly remove elements from the result list
            List<Integer> indices = new ArrayList<>();
            for (int j = 0; j < result.size(); j++) {
                indices.add(j);
            }
            Collections.shuffle(indices);
            for (int j = 0; j < result.size() - length; j++) {
                result.remove(indices.get(j));
            }

            // Update data and label
            for (int j = 0; j < length; j++) {
                mts.getData()[dimNo][start + j] = result.get(j);
                mts.getLabel()[start + j] = 1;
            }
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
