package ru.drsk.progserega.towertracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;
import java.util.Date;

public class addTower extends AppCompatActivity implements mLocation.callback {
    private String line_name=null;
    private mLocation location = null;
    double lat=0;
    double lon=0;
    double accuracy=100000;
    double ele=0;
    long time=0;
    boolean save_button_state=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tower);
        Intent intent = getIntent();
        line_name = intent.getStringExtra(LineName.LINE_NAME);
        TextView tv = (TextView) findViewById(R.id.tower_line_name);
        tv.setText(line_name);
        Button save = (Button) findViewById(R.id.tower_save_button);
        save.setEnabled(false);

        location = mLocation.getInstance(getApplicationContext());
        location.registerCallback(this);
        lat=location.getLat();
        lon=location.getLon();
        accuracy=location.getAccuracy();
        time=location.getTime();
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
        EditText towerNameTE = (EditText) findViewById(R.id.towerName);
        String towerName = towerNameTE.getText().toString();

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

            String tower_material="";
            int line_voltage=0;
            String tower_type="";

            if (sqliteStorage.add_tower(line_name,towerName,lat,lon,ele,time,tower_material,line_voltage,tower_type)==true)
            {
                msbox(getResources().getString(R.string.success),getResources().getString(R.string.success_save));
                // очищаем имя опоры и выводим количество сохранённых опор:
                towerNameTE.clearComposingText();
            }
            else
            {
                msbox(getResources().getString(R.string.error),getResources().getString(R.string.error_save_to_db));
            }


           // msbox("message","success save '"+towerName+"' ("+lat+","+lon+","+accuracy+") to line: '"+line_name+"'");
            // успешный выход из активности:
            setResult(Activity.RESULT_OK);
            Log.d("addTower.saveTower()", "close activity");
            //finish();
        }
    }


    @Override
    public void updateLocation(double lat, double lon, double accuracy, double ele, long time)
    {
        boolean accuracy_state=false;
        boolean location_state=false;
        boolean time_state=false;
        this.lat=lat;
        this.lon=lon;
        this.time=time;
        this.accuracy=accuracy;
        this.ele=ele;
        save_button_state=false;

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
            location_state=true;
        }
        // accuracy:
        label=accuracy+" метров";
        tv = (TextView) findViewById(R.id.tower_accuracy);
        tv.setText(label);
        if (accuracy<15)
        {
            tv.setTextColor(Color.rgb(0,200,0));
            accuracy_state=true;
        }
        else if(accuracy<40)
        {
            tv.setTextColor(Color.rgb(200,200,0));
        }
        else
        {
            tv.setTextColor(Color.rgb(200,0,0));
        }
        // Time
        if(time!=0)
        {
            Date curDate = new Date();
            long curUnix = curDate.getTime();
            long timeDelta=curUnix-time;
            Log.d("updateLocation()","curDate="+curDate.toString()+" curUnix="+curUnix+" time="+time+" timeDelta="+timeDelta);
            if (timeDelta<60000)
            {
                label=(timeDelta/1000)+" сек. назад";
            }
            else if (timeDelta<3600000)
            {
                label=(timeDelta/1000/60)+" минут назад";
            }
            else if (timeDelta<86400000)
            {
                label=(timeDelta/1000/3600)+" часов назад";
            }
            else
            {
                label="более суток назад";
            }
            tv = (TextView) findViewById(R.id.tower_location_time);
            tv.setText(label);
            if (timeDelta/1000<15)
            {
                tv.setTextColor(Color.rgb(0,200,0));
                time_state=true;
            }
            else if(timeDelta/1000<40)
            {
                tv.setTextColor(Color.rgb(200,200,0));
            }
            else
            {
                tv.setTextColor(Color.rgb(200,0,0));
            }
        }
        // разрешаем сохранение, если местоположение свежее и хорошее:
        if(location_state&&time_state&&accuracy_state)
        {
            // сохранение опоры
            Button save = (Button) findViewById(R.id.tower_save_button);
            save.setEnabled(true);
            save.setText(getResources().getString(R.string.saveTower));
            // завершение сбора опор линии
            Button end = (Button) findViewById(R.id.stop_session);
            end.setText(getResources().getString(R.string.stopSessionButton));
        }
        else
        {
            // сохранение опоры
            Button save = (Button) findViewById(R.id.tower_save_button);
            save.setEnabled(false);
            save.setText(getResources().getString(R.string.saveTowerUnavaleble));
            // завершение сбора опор линии
            Button end = (Button) findViewById(R.id.stop_session);
            end.setText(getResources().getString(R.string.stopSessionButtonWithoutSaveTower));
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
