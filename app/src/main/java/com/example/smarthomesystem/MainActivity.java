package com.example.smarthomesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private void showPopupWindowForAction(String action) {
        // Implement your popup window logic here
        // You can create a dialog, AlertDialog, or any custom popup view
        // Example:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Perform action: " + action);
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Handle action here
            // You can perform any specific action based on 'action' parameter
            // For example, open another activity, perform some operation, etc.
            Toast.makeText(MainActivity.this, "Action performed: " + action, Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Cancel action
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    FirebaseAuth auth;

    ArrayList<recycler_list> recycler_list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        auth = FirebaseAuth.getInstance();
        auth.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recycler_list = new ArrayList<recycler_list>();

        recycler_list.add(new recycler_list(R.drawable.temperature,"Temperature"));
        recycler_list.add(new recycler_list(R.drawable.password,"Password"));
        recycler_list.add(new recycler_list(R.drawable.light,"Light"));
        recycler_list.add(new recycler_list(R.drawable.fan,"Fan"));
        recycler_list.add(new recycler_list(R.drawable.attack,"Entry Attack"));
        recycler_list.add(new recycler_list(R.drawable.message,"Message"));

        recycler_adapter recycler_adapter = new recycler_adapter(recycler_list,this);
        recyclerView.setAdapter(recycler_adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemID = item.getItemId();

        if(itemID == R.id.menuprofile){
            startActivity(new Intent(MainActivity.this, profile.class));
        }
        if(itemID == R.id.menusettings){
            startActivity(new Intent(MainActivity.this, settings.class));
        }
        if(itemID == R.id.menulogout){
            auth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            Toast.makeText(this, "logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
        if(itemID == R.id.menusearch){
            startActivity(new Intent(MainActivity.this,Search.class));
        }
        if(itemID == R.id.menulogcat){
            startActivity(new Intent(MainActivity.this,Log.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.options_menu, menu);

        return true;
    }
}
