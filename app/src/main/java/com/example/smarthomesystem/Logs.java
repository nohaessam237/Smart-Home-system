    package com.example.smarthomesystem;

    import android.os.Bundle;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;

    public class Logs extends AppCompatActivity {

        ArrayList<Log_recycler_list> logs_list;
        RecyclerView recyclerView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_log);
            Toolbar toolbar = findViewById(R.id.logs_toolbar);
            setSupportActionBar(toolbar);

            recyclerView = findViewById(R.id.logs_recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));

            logs_list = new ArrayList<>();


            logs_list.add(new Log_recycler_list(0, get_timestamp(), "{HttpStatus: 400, errorCode: 190, subErrorCode: -1, errorType: OAuthException, errorMessage: Invalid OAuth access token signature.}", "error", R.drawable.error));
            logs_list.add(new Log_recycler_list(1, get_timestamp(), "HostConnection::get() New Host Connection established 0x72c40d487e90, tid 5919", "debug", R.drawable.debug));
            logs_list.add(new Log_recycler_list(2, get_timestamp(), "Considering local module com.google.android.gms.providerinstaller.dynamite:0 and remote module com.google.android.gms.providerinstaller.dynamite:0", "info", R.drawable.info));
            logs_list.add(new Log_recycler_list(3, get_timestamp(), "Failed to choose config with EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...", "warning", R.drawable.warning));

            logs_recycler_adapter logs_adapter = new logs_recycler_adapter(logs_list,this);
            recyclerView.setAdapter(logs_adapter);

        }

        private String get_timestamp(){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }
