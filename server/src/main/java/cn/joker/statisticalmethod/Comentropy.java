package cn.joker.statisticalmethod;

import cn.joker.namespace.StdName;

/**
 * @author: pis
 * @description: good good study
 * @date: create in 10:22 2018/6/17
 */
public class Comentropy {
    Comentropy() {
        throw new IllegalStateException(StdName.UTILCLASS);
    }

    /**
    *@author:pis
    *@description: 熵权法
    *@date: 14:43 2018/6/17
    */
    public static Double[] getComentropy(Double[][] data) {
        Integer row = data.length;
        Integer colonm = data[0].length;
        Double[][] store = new Double[row][colonm];
        for (int j = 0; j < colonm; j++) {
            Double min = 10005.0;
            Double max = 0.0;
            for (int i = 0; i < row; i++) {
                store[i][j] = data[i][j];
                if (data[i][j] < min)
                    min = data[i][j];
                if (data[i][j] > max)
                    max = data[i][j];
            }
            for (int i = 0; i < row; i++) {
                data[i][j] = (data[i][j] - min) / (max - min);
            }
        }
        Double[][] p = new Double[row][colonm];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < colonm; j++) {
                Double sumX = 0.0;
                for (Double[] aData : data) {
                    sumX += aData[j];
                }
                p[i][j] = data[i][j] / sumX;
            }
        }
        Double[] e = new Double[colonm];
        Double K = -(1 / Math.log(row));
        for (int j = 0; j < colonm; j++) {
            Double sumE = 0.0;
            for (int i = 0; i < row; i++) {
                if (p[i][j] != 0.0)
                    sumE += p[i][j] * Math.log(p[i][j]);
                else
                    sumE = 0.0;
            }
            e[j] = K * sumE;
        }
        Double sum = 0.0;
        for (int j = 0; j < colonm; j++) {
            sum += e[j];
        }
        Double[] W = new Double[colonm];
        for (int j = 0; j < colonm; j++) {
            W[j] = (1 - e[j]) / (colonm - sum);
        }
        Double[] Z = new Double[row];
        for (int i = 0; i < row; i++) {
            Z[i] = 0.0;
            for (int j = 0; j < colonm; j++) {
                Z[i] += store[i][j] * W[j];
            }
        }
        return Z;
    }
}
