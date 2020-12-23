package com.example.timerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Repository {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public Repository(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public Repository open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void drop()
    {
        this.open();
        db.execSQL("DROP TABLE IF EXISTS timers");
        this.close();
    }

    public void addTimer(String title, String warm_up, String workout, String rest, String cooldown,
                         String cycle, String set_t, String r_t, String g_t, String b_t)
    {
        this.open();
        db.execSQL("INSERT INTO "+dbHelper.TABLE+" ("+dbHelper.COLUMN_TITLE+", "+dbHelper.COLUMN_WARM_UP+", "+dbHelper.COLUMN_WORKOUT+", "
                + dbHelper.COLUMN_REST+ ", "+dbHelper.COLUMN_COOLDOWN+", "+dbHelper.COLUMN_CYCLE+", "+dbHelper.COLUMN_SET_T+", "
                + dbHelper.COLUMN_R_T+", "+dbHelper.COLUMN_G_T+", "+dbHelper.COLUMN_B_T+") "
                + "VALUES ('" + title + "', " + warm_up + ", " + workout + ", " + rest + ", " + cooldown + ", " + cycle + ", "
                + set_t + ", " + r_t + ", " + g_t + ", " + b_t +" );");
        this.close();
    }

    public void editTimer(String Id,
                          String title, String warm_up, String workout, String rest, String cooldown,
                          String cycle, String set_t)
    {
        this.open();
        db.execSQL("UPDATE " + dbHelper.TABLE + "  SET "
                + dbHelper.COLUMN_TITLE + " = '" + title + "', "
                + dbHelper.COLUMN_WARM_UP + " = " + warm_up + ", "
                + dbHelper.COLUMN_WORKOUT + " = " + workout + ", "
                + dbHelper.COLUMN_REST + " = " + rest + ", "
                + dbHelper.COLUMN_COOLDOWN + " = " + cooldown + ", "
                + dbHelper.COLUMN_CYCLE + " = " + cycle + ", "
                + dbHelper.COLUMN_SET_T + " = " + set_t + " "
                + "WHERE id = " + Id + ";");
        this.close();
    }

    public ArrayList<TimerSet> setTimers()
    {
        ArrayList<TimerSet> timers = new ArrayList<>();

        this.open();
        Cursor query = db.rawQuery("SELECT * FROM timers;", null);
        if(query.moveToFirst()){
            do{
                int id = query.getInt(0);
                String name = query.getString(1);
                int warm_up = query.getInt(2);
                int workout = query.getInt(3);
                int rest = query.getInt(4);
                int cooldown = query.getInt(5);
                int cycle = query.getInt(6);
                int set_t = query.getInt(7);
                int r_t = query.getInt(8);
                int g_t = query.getInt(9);
                int b_t = query.getInt(10);
                TimerSet timer = new TimerSet(id, warm_up, workout, rest, cooldown, cycle, set_t);
                timer.setColor(new int[] {r_t, g_t, b_t});
                timer.setTitle(name);
                timers.add(timer);
            }
            while(query.moveToNext());
        }
        query.close();
        this.close();
        return timers;
    }

    public void deleteTimer(String id)
    {
        this.open();
        db.execSQL("DELETE FROM timers WHERE id = " + id + ";");
        this.close();
    }

    public void deleteAllTimers()
    {
        this.open();
        db.execSQL("DELETE FROM timers");
        this.close();
    }




}
