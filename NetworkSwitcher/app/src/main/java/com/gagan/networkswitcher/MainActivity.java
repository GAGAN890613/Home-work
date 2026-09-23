package com.gagan.networkswitcher;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {
    private static final int SHIZUKU_REQUEST = 100;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 10);
        }
        Shizuku.addRequestPermissionResultListener((requestCode, grantResult) -> runOnUiThread(this::refresh));
        buildUi();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 64, 48, 48);

        TextView title = new TextView(this);
        title.setText("Network Switcher"); title.setTextSize(28); title.setPadding(0,0,0,24);
        root.addView(title);

        TextView status = new TextView(this); status.setTextSize(16); status.setPadding(0,0,0,32);
        root.addView(status);

        Button five = new Button(this); five.setText("Switch to Preferred 5G");
        five.setOnClickListener(v -> apply(true, status)); root.addView(five);

        Button four = new Button(this); four.setText("Switch to Preferred 4G");
        four.setOnClickListener(v -> apply(false, status)); root.addView(four);

        Button shizuku = new Button(this); shizuku.setText("Grant Shizuku permission");
        shizuku.setOnClickListener(v -> {
            if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED)
                Shizuku.requestPermission(SHIZUKU_REQUEST);
            else refresh();
        }); root.addView(shizuku);
        setContentView(root); refresh(status);
    }

    private void apply(boolean fiveG, TextView status) {
        status.setText("Applying…");
        new Thread(() -> {
            try {
                boolean ok = fiveG ? NetworkController.setPreferred5G(this) : NetworkController.setPreferred4G(this);
                runOnUiThread(() -> status.setText(ok ? "Applied. The modem may take a few seconds to reconnect." : "The phone rejected the change."));
            } catch (Throwable e) {
                runOnUiThread(() -> status.setText("Failed: " + e.getMessage()));
            }
        }).start();
    }

    private void refresh() { refresh(null); }
    private void refresh(TextView external) {
        if (external == null) return;
        if (!Shizuku.pingBinder()) external.setText("Shizuku is not running.");
        else if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) external.setText("Shizuku is running, but permission is not granted.");
        else external.setText("Ready — Shizuku permission granted.");
    }
}
