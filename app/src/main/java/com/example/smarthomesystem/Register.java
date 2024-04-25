package com.example.smarthomesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.security.cert.PolicyNode;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-home-system-7cd5a-default-rtdb.firebaseio.com/");
    Log log = new Log();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText full_name = findViewById(R.id.full_name);
        final EditText user_name = findViewById(R.id.user_name);
        final EditText mobile_num = findViewById(R.id.phone);
        final EditText email_address = findViewById(R.id.textEmailAddress);
        final EditText birth_date = findViewById(R.id.birthdate);
        final EditText password = findViewById(R.id.password);
        final EditText conf_password = findViewById(R.id.confpassword);
        final Button registerBtn = findViewById(R.id.registerBtn);
        final ImageButton selectdateBtn = findViewById(R.id.birthdate_picker_button);
        final TextView loginNowBtn = findViewById(R.id.loginNowBtn);
        final URI profile_picture = URI.create("");


        selectdateBtn.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            // Get the current year, month, and day from the Calendar
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        birth_date.setText(selectedDate);
                    }, year, month, day);

            datePickerDialog.show();


        });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieve data from txt fields
                final String full_nameTxt = full_name.getText().toString();
                final String user_nameTxt = user_name.getText().toString();
                final String phone = mobile_num.getText().toString();
                final String emailTxt = email_address.getText().toString();
                final String birth_dateTxt = birth_date.getText().toString();
                final String pass = password.getText().toString();
                final String conf_pass = conf_password.getText().toString();

                //check data fields if either one is empty before sending it to firebase
                if (full_nameTxt.isEmpty() || phone.isEmpty() || emailTxt.isEmpty() || pass.isEmpty() || user_nameTxt.isEmpty() || birth_dateTxt.isEmpty()){
                    Toast.makeText(Register.this, "please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                //check if passwords match
                else if (!pass.equals(conf_pass)) {
                    Toast.makeText(Register.this, "passwords don't match", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if phone number, email or username is never been registered before
                            boolean found = false;
                            for (DataSnapshot dataSnapshotChild : snapshot.getChildren()) {
                                String EmailAddress = Objects.requireNonNull(dataSnapshotChild.child("email address").getValue()).toString();
                                String mobile_number = Objects.requireNonNull(dataSnapshotChild.child("mobile num").getValue()).toString();
                                String user_name = Objects.requireNonNull(dataSnapshotChild.child("user name").getValue()).toString();
                                if (mobile_number.equals(phone) || EmailAddress.equals(emailTxt) || user_name.equals(user_nameTxt)) {
                                    found = true;
                                    break;
                                }
                            }
                            if(found){
                                Toast.makeText(Register.this, "This user already exists with similar phone, email or username", Toast.LENGTH_LONG).show();
                            }
                            else{
                                //user_nameTxt is out identifier unique value for each customer
                                databaseReference.child("users").child(user_nameTxt).child("full name").setValue(full_nameTxt);
                                databaseReference.child("users").child(user_nameTxt).child("user name").setValue(user_nameTxt);
                                databaseReference.child("users").child(user_nameTxt).child("email address").setValue(emailTxt);
                                databaseReference.child("users").child(user_nameTxt).child("mobile num").setValue(phone);
                                databaseReference.child("users").child(user_nameTxt).child("birth date").setValue(birth_dateTxt);
                                databaseReference.child("users").child(user_nameTxt).child("password").setValue(pass);
                                databaseReference.child("users").child(user_nameTxt).child("profile picture").setValue(profile_picture);

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