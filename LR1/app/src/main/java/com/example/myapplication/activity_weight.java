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
            ConvViewModel _model = new ViewModelProvider(this).get(ConvViewModel.class);
            if (_model.conv == null)
            {
                _model.conv = this.conv;
                _model.selectValue("");
                _model.selectSpinnerTop(0);
                _model.selectSpinnerDown(1);
            }
        }

        setContentView(R.layout.activity_weight);
    }

}