package com.example.smarthomesystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Log extends AppCompatActivity {

    private ArrayList<String> logData;
    private ArrayAdapter<String> logAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logData = new ArrayList<>();
        GridView gridView = findViewById(R.id.gridView);
        logAdapter = new ArrayAdapter<>(this, R.layout.activity_log, logData);
        gridView.setAdapter(logAdapter);
    }

//    public void logAction(String action) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
//        String timestamp = sdf.format(new Date());
//        String logEntry = timestamp + ": " + action;
//        logData.add(logEntry);
//        logAdapter.notifyDataSetChanged();
//    }
}
