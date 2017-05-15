package ru.drsk.progserega.towertracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class addTower extends AppCompatActivity implements mLocation.callback {
    private String line_name=null;
    private mLocation location = null;
    double lat=0;
    double lon=0;
    double accuracy=100000;
    double ele=0;
    long time=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tower);
        Intent intent = getIntent();
        line_name = intent.getStringExtra(LineName.LINE_NAME);
        TextView tv = (TextView) findViewById(R.id.tower_line_name);
        tv.setText(line_name);
        location = mLocation.getInstance(getApplicationContext());
        lat=location.getLat();
        lon=location.getLon();
        accuracy=location.getAccuracy();
        updateLocation(lat,lon,accuracy,ele,time);
    }
      /**
     * Called when the user clicks the Send button
     */
    public void saveTower(View view) {
        Log.d("addTower.saveTower()", "start save data");

        // TODO добавить сохранение в базу
        SqliteStorage sqliteStorage = SqliteStorage.getInstance(getApplicationContext());
        if (sqliteStorage==null)
        {
            Log.e("addTower.saveTower()", "sqliteStorage.getInstance() error");
        }
        EditText editText = (EditText) findViewById(R.id.towerName);
        String towerName = editText.getText().toString();

        if(towerName.isEmpty())
        {
            Log.d("error","Имя опоры не должно быть пустым!");
            msbox("error","Имя опоры не должно быть пустым!");
        }
        else
        {

            lat=location.getLat();
            lon=location.getLon();
            accuracy=location.getAccuracy();
            ele=location.getEle();
            time=location.getTime();
            updateLocation(lat,lon,accuracy,ele,time);

            msbox("message","success save '"+towerName+"' ("+lat+","+lon+","+accuracy+") to line: '"+line_name+"'");
/*
        if (sqliteStorage.add_station_defect(1,1,towerName)==true)
        {
            msbox("message","success save '"+towerName+"' to line: '"+line_name+"'");
        }
        else
        {
            msbox("message","Error save to db!");
        }
*/

            // успешный выход из активности:
            setResult(Activity.RESULT_OK);
            Log.d("addTower.saveTower()", "close activity");
            //finish();
        }
    }


    @Override
    public void updateLocation(double lat, double lon, double accurate, double ele, long time)
    {
        // location:
        String label=lat+", "+lon;
        TextView tv = (TextView) findViewById(R.id.tower_current_location);
        tv.setText(label);
        if (lat==0 || lon==0)
        {
            tv.setTextColor(Color.rgb(200,0,0));
        }
        else
        {
            tv.setTextColor(Color.rgb(0,200,0));
        }
        // accuracy:
        label=accuracy+" метров";
        tv = (TextView) findViewById(R.id.tower_accuracy);
        tv.setText(label);
        if (accuracy<15)
        {
            tv.setTextColor(Color.rgb(0,200,0));
        }
        else if(accuracy<40)
        {
            tv.setTextColor(Color.rgb(200,200,0));
        }
        else
        {
            tv.setTextColor(Color.rgb(200,0,0));
        }
    }
    private void msbox(String str,String str2)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setTitle(str);
        dlgAlert.setMessage(str2);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // finish();
            }
       });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

}
