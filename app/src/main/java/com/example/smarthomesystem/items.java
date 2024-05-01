package com.example.smarthomesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class items extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-home-system-7cd5a-default-rtdb.firebaseio.com/");
    //the tmp value here is gonna be retained by the tmp-sensor here it's just a default value
    int tmp=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        super.onCreate(savedInstanceState);
        int itemID = getIntent().getIntExtra("id",0);
        final TextView tmp_value = findViewById(R.id.tmpvalue);

        //temperature activity
        if(itemID == 0){
            setContentView(R.layout.activity_temperature);

            // Start a timer to send temperature data to Firebase every few seconds
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendTemperatureToFirebase();
                    //tmp_value.setText(tmp);
                }
            }, 0, 5000);

            if(!connected){
                Toast.makeText(this, "please check your connection", Toast.LENGTH_SHORT).show();
                tmp_value.setText(tmp);
            }
        }
        //implement password activity
        else if(itemID == 1){

            
        }
        //implement Light activity
        else if (itemID == 2) {
            
        }
        //implement fan activity
        else if (itemID == 3) {
            
        }
        //implement entry attack
        else if(itemID == 4){
            
        }
        //implement message attack
        else if (itemID == 5) {
            
        }

    }

    private void sendTemperatureToFirebase() {
        databaseReference.child("temperature").setValue(tmp);
    }
}