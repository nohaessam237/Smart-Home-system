package com.example.smarthomesystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class items extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-home-system-7cd5a-default-rtdb.firebaseio.com/");
    //the tmp value here is gonna be retained by the tmp-sensor here it's just a default value
    int tmp = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int itemID = getIntent().getIntExtra("id", 0);
        final TextView tmp_value = findViewById(R.id.tmpvalue);

        //temperature activity
        if (itemID == 0) {
            setContentView(R.layout.activity_temperature);

            // Start a timer to send temperature data to Firebase every few seconds
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendTemperatureToFirebase();
//                    tmp_value.setText(tmp);
                }
            }, 0, 5000);

        }
        //implement password activity
        else if (itemID == 1) {
            startActivity(new Intent(this, PasswordActivity.class));
            finish();
        }
        //implement Light activity
        else if (itemID == 2) {
            startActivity(new Intent(this, Light.class));
            finish();

        }
        //implement fan activity
        else if (itemID == 3)
        {
            startActivity(new Intent(this, Fan.class));
            finish();

        }
        else if (itemID == 4)
        {
            startActivity(new Intent(this, entryAttack.class));
            finish();
        }
        else if (itemID == 5)
        {
            startActivity(new Intent(this, Message_Activity.class));
            finish();

        }
    }

    private void sendTemperatureToFirebase()
    {
        databaseReference.child("temperature").setValue(tmp);
    }


}