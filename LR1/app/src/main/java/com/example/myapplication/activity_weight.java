package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class activity_weight extends AppCompatActivity{

    private Conversion conv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getIntent().getExtras();
        if(arguments != null){
            this.conv = (Conversion) arguments.getSerializable(Conversion.class.getSimpleName());
            ConversionViewModel model = new ViewModelProvider(this).get(ConversionViewModel.class);
            model.select(this.conv);
            SaveTopViewModel model_order = new ViewModelProvider(this).get(SaveTopViewModel.class);
            if (model_order.selected.getValue() == null)
               model_order.select(0);
            SaveDownViewModel model_order_down = new ViewModelProvider(this).get(SaveDownViewModel.class);
            if (model_order_down.selected.getValue() == null)
                model_order_down.select(1);
            SaveViewModel model_string = new ViewModelProvider(this).get(SaveViewModel.class);
            if (model_string.selected.getValue() == null)
                model_string.select("");
            SharedViewModel model_share = new ViewModelProvider(this).get(SharedViewModel.class);
            model_share.select(null);
        }

        setContentView(R.layout.activity_weight);
    }

}