package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConversionViewModel extends ViewModel {
    public final MutableLiveData<Conversion> selected = new MutableLiveData<Conversion>();

    public void select(Conversion conv) {
        selected.setValue(conv);
    }

    public LiveData<Conversion> getSelected() {
        return selected;
    }
}
