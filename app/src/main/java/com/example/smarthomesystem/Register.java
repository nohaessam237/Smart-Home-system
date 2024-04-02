package com.example.smarthomesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-home-system-7cd5a-default-rtdb.firebaseio.com/");
    //userID is a unique identifier to each user it's randomly generated every time a new user register,
    // values between 1 & 10 million.
    int userID = (int) (Math.random() * 10000000) + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText full_name = findViewById(R.id.full_name);
        final EditText mobile_num = findViewById(R.id.phone);
        final EditText email_address = findViewById(R.id.textEmailAddress);
        final EditText password = findViewById(R.id.password);
        final EditText conf_password = findViewById(R.id.confpassword);
        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.loginNowBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieve data from txt fields
                final String full_nameTxt = full_name.getText().toString();
                final String phone = mobile_num.getText().toString();
                final String emailTxt = email_address.getText().toString();
                final String pass = password.getText().toString();
                final String conf_pass = conf_password.getText().toString();

                //check data fields if either one is empty before sending it to firebase
                if (full_nameTxt.isEmpty() || phone.isEmpty() || emailTxt.isEmpty() || pass.isEmpty()){
                    Toast.makeText(Register.this, "please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                //check if passwords match
                else if (!pass.equals(conf_pass)) {
                    Toast.makeText(Register.this, "passwords don't match", Toast.LENGTH_SHORT).show();
                }
                //send all value to firebase real-time database, we are using userID counter as identifier for each user
                //it's randomly generated every time a new user is registered
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if ID is never been registered before & phone number

                            if(snapshot.hasChild(phone)){
                                Toast.makeText(Register.this, "mobile number already registered",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                databaseReference.child("users").child(String.valueOf(userID)).child("full name").setValue(full_nameTxt);
                                databaseReference.child("users").child(String.valueOf(userID)).child("mobile num").setValue(phone);
                                databaseReference.child("users").child(String.valueOf(userID)).child("email address").setValue(emailTxt);
                                databaseReference.child("users").child(String.valueOf(userID)).child("password").setValue(pass);

                                Toast.makeText(Register.this, "User Registered Successfully.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }


            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}