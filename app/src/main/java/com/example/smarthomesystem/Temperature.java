package com.example.smarthomesystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Temperature extends AppCompatActivity {

    private static final String TAG = "TemperatureActivity";
    private DatabaseReference databaseReference;
    private boolean Temperature;
    private DatabaseReference Temperaturerec;
    private DatabaseReference LR;

    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        conn=new Connection(this);
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Temperaturerec=databaseReference.child("logs");
        LR=databaseReference.child("temperature");
        TextView switchCompat = findViewById(R.id.tmpvalue);

        if (conn.isConnectionAvailable()) {
            LR.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double uu=snapshot.getValue(double.class);
                     switchCompat.setText(Double.toString(uu));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            HashMap<String, Object> record = new HashMap<>();
            record.put("description", "Room Temperature");
            record.put("id", 1);
            record.put("img",  10101);
            record.put("type", "Feature Temperature");
            record.put("timestamp",get_timestamp() );
            Temperaturerec.child("TemperatureRec").setValue(record);
            LogsManager logsManager = LogsManager.getInstance();
            logsManager.addLog(new Log_recycler_list(1, get_timestamp(), "Temperature Room", "Feature Temperature", R.drawable.temperature));


        } else {
            Toast.makeText(this, "You are offline :(", Toast.LENGTH_SHORT).show();
            switchCompat.setAlpha(0.5F);
            switchCompat.setEnabled(false);
        }


    }
    private String get_timestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
   /* private void sendTemperatureToFirebase(boolean Temperature) {
        LR.setValue(Temperature, (error, ref) -> {
            if (error != null) {
                Log.e(TAG, "Failed to set value to Firebase: " + error.getMessage());
            } else {
                Log.d(TAG, "Value sent to Firebase successfully");
            }
        });
    }*/

}


