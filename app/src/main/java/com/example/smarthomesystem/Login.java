package com.example.smarthomesystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.textEmailAddress);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(v -> {
            final String email_address = email.getText().toString();
            final String pass = password.getText().toString();

            if (email_address.isEmpty() || pass.isEmpty()){
                Toast.makeText(Login.this, "please enter your email address or password", Toast.LENGTH_SHORT).show();
            }
            else{
                startActivity(new Intent(Login.this, MainActivityKt.class));
            }
        });



        registerNowBtn.setOnClickListener(v -> {
            //open register Activity
            startActivity(new Intent(Login.this, Register.class));
        });

    }
}