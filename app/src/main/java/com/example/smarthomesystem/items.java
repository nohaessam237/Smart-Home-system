package com.example.smarthomesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class items extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        int itemID = getIntent().getIntExtra("id",0);

        TextView itemid = findViewById(R.id.itemid);
        itemid.setText("Action Activity "+itemID);
    }
}