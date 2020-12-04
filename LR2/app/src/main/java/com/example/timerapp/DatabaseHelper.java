package com.example.timerapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "timers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_WARM_UP = "warm_up";
    public static final String COLUMN_WORKOUT = "workout";
    public static final String COLUMN_REST = "rest";
    public static final String COLUMN_COOLDOWN = "cooldown";
    public static final String COLUMN_CYCLE = "cycle";
    public static final String COLUMN_SET_T = "set_t";
    public static final String COLUMN_R_T = "r_t";
    public static final String COLUMN_G_T = "g_t";
    public static final String COLUMN_B_T = "b_t";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE + "TEXT,"
                + COLUMN_TITLE + " INTEGER,"
                + COLUMN_WARM_UP + " INTEGER,"
                + COLUMN_WORKOUT + " INTEGER,"
                + COLUMN_REST + " INTEGER,"
                + COLUMN_COOLDOWN + " INTEGER,"
                + COLUMN_CYCLE + " INTEGER,"
                + COLUMN_SET_T + " INTEGER,"
                + COLUMN_R_T + " INTEGER,"
                + COLUMN_G_T + " INTEGER,"
                + COLUMN_B_T + " INTEGER"+ ");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);
        onCreate(db);
    }

}
