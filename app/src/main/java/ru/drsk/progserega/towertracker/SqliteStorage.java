package ru.drsk.progserega.towertracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by serega on 31.01.17.
 */

public class SqliteStorage {

    private static Context context = null;
    private static StationDbHelper dbHelper = null;
    private static SQLiteDatabase db;
    private static SqliteStorage ss = null;

    public static SqliteStorage getInstance(Context applicationcontext)
    {
        if (ss == null)
        {
            ss = new SqliteStorage();
            ss.context = applicationcontext;
            // Gets the data repository in write mode
            try
            {
                ss.dbHelper = new StationDbHelper(context);
                ss.db = ss.dbHelper.getWritableDatabase();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return ss;
    }


    public ArrayList<Long> getIdsTowersOfLine(String line_name)
    {
        Cursor cur;
        Long id;
        String selection = "line_name = ?";
        String[] selectionArgs = {line_name};
        List<Long> ids = new ArrayList<Long>();

        try
        {
            cur = db.query("towers_tbl", new String[]{"id"},selection,selectionArgs,null,null,null);
            if (cur.getCount()==0) {
                Log.e("getSpIdbyName()", "select return 0 records!");
                cur.close();
                return null;
            }
            else
            {
                while(cur.moveToNext()) {
                    id=cur.getLong(cur.getColumnIndexOrThrow("id"));
                    Log.d("getSpIdbyName()", "get tower_id from db: " + id);
                    ids.add((long) id);
                }
            }
            cur.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return (ArrayList<Long>) ids;
    }

    public String getTowerTagById(Long id, String tag_key)
    {
        Cursor cur;
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};
        String tag_value = null;

        try
        {
            cur = db.query("towers_tbl", new String[]{tag_key},selection,selectionArgs,null,null,null);
            if (cur.getCount()==0) {
                Log.e("getTowerTagById()", "select return 0 records!");
                cur.close();
                return null;
            }
            else
            {
                while(cur.moveToNext()) {
                    tag_value=cur.getString(cur.getColumnIndexOrThrow(tag_key));
                    Log.d("getTowerTagById()", "id="+id+" "+tag_key+"=" + tag_value);
                }
            }
            cur.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        return tag_value;
    }

    protected boolean checkEmptyDb()
    {
        Cursor c = db.query("sp_tbl", new String[]{"id", "name"},null,null,null,null,null);
        int count = c.getCount();
        c.close();

        Log.i("checkEmptyDb()", String.format("select return %d records!", count));
        return count == 0;
    }

    public void clear_db()
    {
        dbHelper.onUpgrade(db,1,1);
    }

    public boolean add_tower(String line_name, String tower_ref, double lat, double lon, double ele, long time, String tower_material, int line_voltage, String tower_type)
    {
        // Заполняем поля:
        ContentValues values = new ContentValues();
        try
        {
            values.put("line_name", line_name);
            values.put("tower_ref", tower_ref);
            values.put("lat", lat);
            values.put("lon", lon);
            values.put("ele", ele);
            values.put("time", time);
            values.put("tower_material", tower_material);
            values.put("line_voltage", line_voltage);
            values.put("tower_type", tower_type);
            long newRowId = db.insert("tower_tbl", null, values);
            if (newRowId==-1)
            {
                Log.e("add_tower()", "error insert row: " + values.toString());
                return false;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
