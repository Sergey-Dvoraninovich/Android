package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConvViewModel extends ViewModel {

    public Conversion conv;
    public void setConversionData(Conversion conv)
    {
        this.conv = conv;
    }
    private int _spinnerTop;
    private int _spinnerDown;

    public final MutableLiveData<String> val = new MutableLiveData<String>();

    public String selectValue(String s) {
        val.setValue(s);
        return makeConversion(s);
    }
    public LiveData<String> getSelectedValue() {
        return val;
    }

    public final MutableLiveData<Integer> spinnerTop = new MutableLiveData<Integer>();
    public void selectSpinnerTop(Integer s) {
        _spinnerTop = s;
        spinnerTop.setValue(s);
    }
    public LiveData<Integer> getSelectedSpinnerTop() {
        return spinnerTop;
    }

    public final MutableLiveData<Integer> spinnerDown = new MutableLiveData<Integer>();
    public void selectSpinnerDown(Integer s) {
        _spinnerDown = s;
        spinnerDown.setValue(s);
    }
    public LiveData<Integer> getSelectedSpinnerDown() {
        return spinnerDown;
    }

    public String makeConversion(String s)
    {
        String new_line_value = s;
        String ans_text = "";
        Double num;
        if (!new_line_value.equals("")) {
            try {
                num = Double.parseDouble(new_line_value);
                double ans = conv.Convert(_spinnerTop, _spinnerDown, num);
                double ans_double = (double)Math.round(ans * 1000000);
                ans_double /= 1000000;
                ans_text = Double.toString(ans_double);

            } catch (Exception e) {
                ans_text = "";
            }
        }
        return ans_text;
    }

}
