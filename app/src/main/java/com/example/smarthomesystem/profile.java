package com.example.smarthomesystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.HashMap;

public class profile extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-home-system-7cd5a-default-rtdb.firebaseio.com/");
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private ImageView profilePicture;
    private ImageView backBtn;
    TextView username;
    TextView useremail;
    TextView userphone;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();

        Button logoutBtn = findViewById(R.id.profile_logoutBtn);
        username = findViewById(R.id.user_name);
        useremail = findViewById(R.id.user_email);
        userphone = findViewById(R.id.user_phone);
        profilePicture = findViewById(R.id.profile_picture);
        backBtn = findViewById(R.id.arrow_back);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        profilePicture.setOnClickListener(v -> {
            Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
            imagePickerIntent.setType("image/*");
            startActivityForResult(imagePickerIntent, PICK_IMAGE_REQUEST_CODE);
        });

        backBtn.setOnClickListener(v -> {

        });


        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(profile.this, Login.class));
            Toast.makeText(this, "logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            FirebaseUser user = auth.getCurrentUser();
            Uri selectedImageUri = data.getData();
            profilePicture.setImageURI(selectedImageUri);
            username.setText(user.getDisplayName());
            useremail.setText(user.getEmail());
            userphone.setText(user.getPhoneNumber());

            if(user != null){
                databaseReference.child("users").child(String.valueOf(user.getPhotoUrl())).setValue(selectedImageUri);
            }else{
                Toast.makeText(this, "no signed user", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
