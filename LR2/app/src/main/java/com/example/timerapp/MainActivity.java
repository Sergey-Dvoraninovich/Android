package com.example.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Repository _repository;

    ArrayList<TimerSet> timers = new ArrayList();
    TimersListAdapter adapter;
    ListView timersList;

    TextView text;
    ImageView image_add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _repository = new Repository(this);

        text = (TextView) findViewById(R.id.test_text);
        timersList = (ListView) findViewById(R.id.list_timer_sets);
        image_add = (ImageView) findViewById(R.id.image_add_timer);
        text.setText("");

        timers = _repository.setTimers();

        adapter = new TimersListAdapter(this, R.layout.item_timer, timers);
        timersList.setAdapter(adapter);
        timersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Intent i = new Intent(MainActivity.this, TimerActivity.class);
                TimerSet local_timerSet = timers.get(position);
                i.putExtra(TimerSet.class.getSimpleName(), local_timerSet);
                startActivity(i);
            }
        });

        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateUpdateActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //timers.removeAll(timers);
        //initialTimersSet();
        timers = _repository.setTimers();
        TimersListAdapter new_adapter = new TimersListAdapter(this, R.layout.item_timer, timers);
        timersList.setAdapter(new_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_settings) {
            Intent i = new Intent(MainActivity.this, ActivitySettings.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialTimersSet()
    {
        TextView textView = (TextView) findViewById(R.id.test_text);
        textView.setText("");
        timers = _repository.setTimers();
    }

}