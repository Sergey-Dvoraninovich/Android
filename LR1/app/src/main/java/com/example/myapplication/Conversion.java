package com.example.myapplication;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class Conversion implements Serializable {

    private String[] values;
    private double[][] conv_kf;
    private String s_top;
    private String s_down;

    public Conversion(String[] values, double[] conv_kf)
    {
        this.values = values;
        this.conv_kf = new double[values.length][values.length];
        for (int i = 0; i < values.length; i++)
            for (int j = 0; j < values.length; j++)
                this.conv_kf[i][j] = 1.0;
        int k = 0;
        for (int i = 0; i < values.length; i++)
            for (int j = i + 1; j < values.length; j++) {
                if (k < conv_kf.length) {
                    this.conv_kf[i][j] = conv_kf[k];
                    this.conv_kf[j][i] = 1.0 / conv_kf[k];
                    k++;
                }
            }
        this.s_top = values[0];
        this.s_down = values[1];
    }

    public Conversion(String[] values, double[][] conv_kf)
    {
        this.values = values;
        this.conv_kf = conv_kf;
        this.s_top = values[0];
        this.s_down = values[1];
    }

    public double Convert(String val_1, String val_2, double value)
    {
        int i = 0;
        for (int k = 0; k < values.length; k++)
            if (values[k].equals(val_1))
                i = k;
        int j = 0;
        for (int k = 0; k < values.length; k++)
            if (values[k].equals(val_2))
                j = k;
        return this.conv_kf[i][j] * value;
    }

    public String[] getValues()
    {
        return this.values;
    }

    public void setPosition(String s_top, String s_down)
    {
        this.s_top = s_top;
        this.s_down = s_down;
    }
}

