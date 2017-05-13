package ru.drsk.progserega.towertracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class selectSessionType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_session_type);
    }
       /**
     * Called when the user clicks the Send button
     */
    public void startLineSession(View view) {
        Log.d("startSession()", "1");

        // Do something in response to button
        //Intent intent = new Intent(this, addStationBug.class);
        Intent intent = new Intent(this, addTower.class);
            /*EditText editText = (EditText) findViewById(R.id.edit_message);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);*/
        Log.d("stationAddBug()", "2");
        startActivity(intent);
        Log.d("stationAddBug()", "3");

    }
}
