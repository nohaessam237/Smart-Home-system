package com.example.smarthomesystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://smart-home-system-7cd5a-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;
    CallbackManager mCallbackManager;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        final EditText username = findViewById(R.id.user_name);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);
        final TextView forget_pass = findViewById(R.id.forget_password);
        Button googleAuth = findViewById(R.id.googleBtn);
        Button facebookAuth = findViewById(R.id.login_button);


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        facebookAuth.setOnClickListener(view -> {
            if(accessToken != null && !accessToken.isExpired()){
                Toast.makeText(Login.this, "successfully logged in with facebook", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
            else{
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
            // Manually handle Facebook login
            LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));
        });

        forget_pass.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, ForgetPassword.class));
            finish();
        });

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Handle successful login
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // Handle canceled login
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                // Handle login error
                Toast.makeText(Login.this, "i guess we can't login with facebook", Toast.LENGTH_SHORT).show();
            }

        });


        GoogleSignInOptions google_sign_in = new GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN).requestIdToken(getString(R.string.google_oauth_client_id))
                        .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,google_sign_in);

        googleAuth.setOnClickListener(view -> {
                    if(auth.getCurrentUser() != null){
            startActivity(new Intent(Login.this, MainActivity.class));
            Toast.makeText(Login.this, "successfully logged in with google", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }


        });

        loginBtn.setOnClickListener(v -> {
            final String usernameTxt = username.getText().toString();
            final String pass = password.getText().toString();

            if (usernameTxt.isEmpty() || pass.isEmpty()){
                Toast.makeText(Login.this, "please enter your email address or password", Toast.LENGTH_SHORT).show();
            }
            else{
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check username if entered
                        if (snapshot.hasChild(usernameTxt.replace(".",","))) {
                            DataSnapshot userSnapshot = snapshot.child(usernameTxt.replace(".",","));
                            String storedPass = userSnapshot.child("password").getValue(String.class);
                            if (storedPass != null && storedPass.equals(pass)) {
                                Intent intent = new Intent(Login.this,MainActivity.class);
                                intent.putExtra("username",usernameTxt);
                                startActivity(intent);
                                Toast.makeText(Login.this, "successfully logged in", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Login.this, "wrong password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Login.this, "wrong credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        registerNowBtn.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = auth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            Toast.makeText(Login.this, "Welcome Home", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void googleSignIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuth(account.getIdToken());

                    } catch (ApiException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "something went wrong: result code is:" + result.getResultCode(), Toast.LENGTH_SHORT).show();
                }
            });


    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                HashMap<String,Object> map = new HashMap<>();
                assert user != null;
                map.put("id",user.getUid());
                map.put("full name", user.getDisplayName());
                map.put("profile picture", Objects.requireNonNull(user.getPhotoUrl()).toString());
                map.put("mobile num", user.getPhoneNumber());
                map.put("email address", user.getEmail());

                databaseReference.child("users").child(user.getUid()).setValue(map);
                startActivity(new Intent(Login.this, MainActivity.class));
                Toast.makeText(this, "successfully logged in", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Login.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void togglePasswordVisibility(View view) {
        EditText passwordEditText = findViewById(R.id.password);
        ImageView passwordToggle = findViewById(R.id.password_toggle);

        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Show password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordToggle.setImageResource(android.R.drawable.ic_menu_view);
        } else {
            // Hide password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordToggle.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        }

        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());

    }
}