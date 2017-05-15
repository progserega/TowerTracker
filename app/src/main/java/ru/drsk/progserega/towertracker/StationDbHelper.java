package ru.drsk.progserega.towertracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TowersCollectData.db";


    public StationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        String sql;
        // Таблица подстанций:
        sql = "CREATE TABLE towers_tbl (" +
                "id integer PRIMARY KEY NOT NULL," +
                "line_name VARCHAR(255)," +
                "tower_ref VARCHAR(255)," +
                "lat REAL," +
                "lon REAL," +
                "ele REAL," +
                "time integer," +
                "tower_material VARCHAR(255)," +
                "line_voltage VARCHAR(255)," +
                "tower_type VARCHAR(255));";
        db.execSQL(sql);
        // Таблица СП:
 /*       sql = "CREATE TABLE sp_tbl ("+
        "id integer PRIMARY KEY NOT NULL, "+
        "name VARCHAR(255) "+
        ");";
        db.execSQL(sql);*/

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        String sql;
        sql="drop table towers_tbl;";
        db.execSQL(sql);

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
