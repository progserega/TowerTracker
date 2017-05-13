package ru.drsk.progserega.towertracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class addTower extends AppCompatActivity {
    private String line_name=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tower);
        Intent intent = getIntent();
        line_name = intent.getStringExtra(LineName.LINE_NAME);
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

            msbox("message","success save '"+towerName+"' to line: '"+line_name+"'");
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
