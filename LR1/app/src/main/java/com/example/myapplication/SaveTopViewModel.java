package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SaveTopViewModel extends ViewModel {
    public final MutableLiveData<Integer> selected = new MutableLiveData<Integer>();

    public void select(Integer s) {
        selected.setValue(s);
    }

    public LiveData<Integer> getSelected() {
        return selected;
    }
}
