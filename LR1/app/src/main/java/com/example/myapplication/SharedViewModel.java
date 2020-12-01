package com.example.myapplication;

import android.content.ClipData;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    public final MutableLiveData<String> selected = new MutableLiveData<String>();

    public void select(String s) {
        selected.setValue(s);
    }

    public LiveData<String> getSelected() {
        return selected;
    }
}

