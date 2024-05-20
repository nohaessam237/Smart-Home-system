package com.example.smarthomesystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class profile extends AppCompatActivity {


    //handle sync info by regular username & pass
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private ImageView profilePicture;
    private ImageView backBtn;
    TextView username;
    TextView useremail;
    TextView userphone;
    FirebaseAuth auth;
    Connection conn;
    FirebaseDatabase fbdb;
    Users u;
    DBhelper db;

    private static final String PROFILE_STORAGE_PATH = "profile_images";
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
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

        db = new DBhelper(this);
        conn = new Connection(this);
        u=db.getUserData(Constants.currentEmail);

            if (conn.isConnectionAvailable()) {
                Toast.makeText(profile.this, " ONLINE ", Toast.LENGTH_SHORT).show();
                fbdb=FirebaseDatabase.getInstance();
                DatabaseReference rr=fbdb.getReference("users/" + Constants.currentUserName + "/user info");
                rr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users uu=snapshot.getValue(Users.class);
                        if(uu!=null)
                        {
                            username.setText(uu.username);
                            useremail.setText(uu.getEmail());
                            userphone.setText(uu.phone);
                            Glide.with(profile.this).load(uu.getphotoUrl()).error(R.drawable.unknown_user).into(profilePicture);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //profilePicture.setImageURI(null);
                profilePicture.setOnClickListener(v -> {
                    Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                    imagePickerIntent.setType("image/*");
                    startActivityForResult(imagePickerIntent, PICK_IMAGE_REQUEST_CODE);
                });

                backBtn.setOnClickListener(v -> {
                    startActivity(new Intent(profile.this, MainActivity.class));
                });


                logoutBtn.setOnClickListener(v -> {
                    auth.signOut();
                    startActivity(new Intent(profile.this, LoginActivity.class));
                    Toast.makeText(this, "logged out successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });

            } else {
                Toast.makeText(profile.this, " OFFLINE", Toast.LENGTH_SHORT).show();

               username.setText(u.getUsername().toString());
                useremail.setText(u.getEmail().toString());
                userphone.setText(u.getPhone().toString());
                profilePicture.setImageURI(null);


            }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                Uri selectedImageUri = data.getData();
                profilePicture.setImageURI(selectedImageUri);

                //databaseReference.child("users").child(user.getUid()).child("photoUrl").setValue(selectedImageUri.toString());

              //databaseReference.child("users").child(Constants.currentUserName).child("user info").child("photoUrl").setValue(selectedImageUri.toString());
                try {
                    saveUserInfoWithNewImage(selectedImageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Uri selectedImageUri = data.getData();
                profilePicture.setImageURI(selectedImageUri);
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void saveUserInfoWithNewImage(Uri profileUri) throws IOException {
        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(
                getApplication().getContentResolver(), profileUri
        );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 93, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        StorageReference imageDirectory = storage.getReference()
                .child(PROFILE_STORAGE_PATH)
                .child(Constants.currentUserName)
                .child(UUID.randomUUID().toString());

        imageDirectory.putBytes(imageByteArray).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageDirectory.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    databaseReference.child("users").child(Constants.currentUserName).child("user info").child("photoUrl").setValue(imageUrl);
                });
            }
        });
    }


}
