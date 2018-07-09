package ru.drsk.progserega.towertracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class LineName extends AppCompatActivity {
    public final static String LINE_NAME = "lineName";
    private String line_type=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_name);
        Intent intent = getIntent();
        line_type = intent.getStringExtra(selectSessionType.LINE_TYPE);
        Log.d("LineName.onCreate(line_type="+line_type+")", "1");
    }
    /**
     * Called when the user clicks the Send button
     */
    public void startAddTower(View view) {
        // Do something in response to button
        //Intent intent = new Intent(this, addStationBug.class);
        Log.d("startAddTower()", "1");
        EditText editText = (EditText) findViewById(R.id.line_name);
        String line_name = editText.getText().toString();
        if(line_name.isEmpty())
        {
            msbox("error","Имя линии не должно быть пустым!");
            Log.d("error","Имя линии не должно быть пустым!");
        }
        else
        {
            Log.d("startAddTower()", "line_name='"+line_name+"'");
            Intent intent = new Intent(this, addTower.class);
            intent.putExtra(LINE_NAME, line_name);
            Log.d("startAddTower()", "2");
            startActivity(intent);
        }
    }
    private void msbox(String str,String str2)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setTitle(str);
        dlgAlert.setMessage(str2);
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
             //   finish();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
