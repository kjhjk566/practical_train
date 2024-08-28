package com.example.practical_train_backend.entity;

import lombok.Data;

@Data
public class MTS {
    private double[][] data;
    private int dim;
    private int STREAM_LENGTH;
    private int[] label;
    private double[][] dataOrigin;

    public MTS(double[][] data) {
        this.data = data;
        this.dim = data.length;
        this.STREAM_LENGTH = data[0].length;
        this.label = new int[STREAM_LENGTH];
        this.dataOrigin = new double[dim][STREAM_LENGTH];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(data[i], 0, dataOrigin[i], 0, STREAM_LENGTH);
        }
    }

}
