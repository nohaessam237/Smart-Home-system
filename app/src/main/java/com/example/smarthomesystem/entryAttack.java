package com.example.smarthomesystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class entryAttack extends AppCompatActivity {
    private DatabaseReference passwordRef; // Reference for the stored password in Firebase
    private DatabaseReference alertRef; // Reference for the alert state in Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_attack);

        // Initialize Firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        passwordRef = database.getReference("password"); // Reference for the stored password
        alertRef = database.getReference("alert"); // Reference to update the alert state

        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String enteredPassword = passwordEditText.getText().toString().trim();

            // Get the stored password from Firebase
            passwordRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String storedPassword = dataSnapshot.getValue(String.class);

                    if (storedPassword != null && storedPassword.equals(enteredPassword)) {
                        // Correct password, set alert state to false
                        alertRef.setValue(false);
                        Toast.makeText(entryAttack.this, "Access Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        // Incorrect password, set alert state to true
                        alertRef.setValue(true);
                        Toast.makeText(entryAttack.this, "Access Denied", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle errors if Firebase access is cancelled
                    Toast.makeText(entryAttack.this, "Error accessing Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}