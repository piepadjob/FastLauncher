package com.fastlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppAdapter adapter;
    private ProgressBar progressBar;
    private final BroadcastReceiver packageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApps();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Status bar trong suốt + icon trắng
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Xóa flag LIGHT_STATUS_BAR để icon status bar màu trắng
            window.getDecorView().setSystemUiVisibility(
                window.getDecorView().getSystemUiVisibility()
                & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_home);
        progressBar = findViewById(R.id.progress_bar);
        ImageButton btnDrawer = findViewById(R.id.btn_drawer);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new AppAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        btnDrawer.setOnClickListener(v -> {
            startActivity(new Intent(this, AppDrawerActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.no_anim);
        });

        // Register package install/uninstall receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(packageReceiver, filter);

        loadApps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(packageReceiver);
    }

    @Override
    public void onBackPressed() {
        // Do nothing — home screen, back should not exit
    }

    private void loadApps() {
        progressBar.setVisibility(View.VISIBLE);
        AsyncTask.execute(() -> {
            List<AppInfo> apps = getInstalledApps();
            runOnUiThread(() -> {
                adapter = new AppAdapter(this, apps);
                RecyclerView rv = findViewById(R.id.recycler_home);
                rv.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            });
        });
    }

    static List<AppInfo> getInstalledApps(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, (a, b) ->
                a.loadLabel(pm).toString().compareToIgnoreCase(b.loadLabel(pm).toString()));

        List<AppInfo> apps = new ArrayList<>();
        for (ResolveInfo ri : resolveInfos) {
            String pkg = ri.activityInfo.packageName;
            if (pkg.equals(context.getPackageName())) continue; // hide self
            apps.add(new AppInfo(
                    ri.loadLabel(pm).toString(),
                    pkg,
                    ri.loadIcon(pm)
            ));
        }
        return apps;
    }

    private List<AppInfo> getInstalledApps() {
        return getInstalledApps(this);
    }
}
