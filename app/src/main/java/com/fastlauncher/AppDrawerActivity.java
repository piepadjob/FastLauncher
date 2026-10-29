package com.fastlauncher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppDrawerActivity extends AppCompatActivity {

    private AppAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        RecyclerView recyclerView = findViewById(R.id.recycler_drawer);
        EditText searchBox = findViewById(R.id.search_box);
        progressBar = findViewById(R.id.progress_bar_drawer);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new AppAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        loadApps();
    }

    private void loadApps() {
        progressBar.setVisibility(View.VISIBLE);
        AsyncTask.execute(() -> {
            List<AppInfo> apps = MainActivity.getInstalledApps(this);
            runOnUiThread(() -> {
                adapter = new AppAdapter(this, apps);
                RecyclerView rv = findViewById(R.id.recycler_drawer);
                rv.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            });
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_down);
    }
}
