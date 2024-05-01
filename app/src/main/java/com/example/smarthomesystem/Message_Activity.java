package com.example.smarthomesystem;

// MessageDisplayActivity.java

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Message_Activity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private DatabaseReference msgrec;
    private DatabaseReference MR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        msgrec=databaseReference.child("logs");
        MR=databaseReference.child("message");
        EditText messageInput = (EditText) findViewById(R.id.message_input);
        Button displayButton = (Button) findViewById(R.id.display_button);

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageInput.getText().toString();
                sendMsgToFB(msg);
            }
        });
        HashMap<String, Object> record = new HashMap<>();
        record.put("description", "Display Messages");
        record.put("id", 2);
        record.put("img",  10101);
        record.put("type", "Feature message");
        record.put("timestamp",get_timestamp() );
        msgrec.child("MsgRec").setValue(record);
        LogsManager logsManager = LogsManager.getInstance();
        logsManager.addLog(new Log_recycler_list(2, get_timestamp(), "Display Messages", "Feature message", R.drawable.message));
    }
    private String get_timestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void sendMsgToFB(String msg)
    {
        MR.setValue(msg, (error, ref) -> {
            if (error != null) {
                Toast.makeText(Message_Activity.this, "Failed to send the message to Firebase", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Message_Activity.this, "Message sent to Firebase Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
