package com.example.smarthomesystem;

import androidx.annotation.NonNull;
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

    FirebaseAuth auth;
    ArrayList<recycler_list> recycler_list;
    RecyclerView recyclerView;
    Log log = new Log();

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

        recycler_list = new ArrayList<>();

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
