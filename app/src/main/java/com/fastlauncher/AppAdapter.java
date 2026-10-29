package com.fastlauncher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<AppInfo> apps;
    private final List<AppInfo> allApps;
    private final Context context;

    public AppAdapter(Context context, List<AppInfo> apps) {
        this.context = context;
        this.allApps = new ArrayList<>(apps);
        this.apps = new ArrayList<>(apps);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo app = apps.get(position);
        holder.icon.setImageDrawable(app.icon);
        holder.label.setText(app.label);
        holder.itemView.setOnClickListener(v -> launchApp(app));
        holder.itemView.setOnLongClickListener(v -> {
            Toast.makeText(context, app.packageName, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public void filter(String query) {
        apps = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            apps.addAll(allApps);
        } else {
            String lower = query.toLowerCase();
            for (AppInfo app : allApps) {
                if (app.label.toLowerCase().contains(lower)) {
                    apps.add(app);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void launchApp(AppInfo app) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(app.packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Cannot open " + app.label, Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.app_icon);
            label = itemView.findViewById(R.id.app_label);
        }
    }
}
