package com.gagan.networkswitcher;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

public class NetworkTileService extends TileService {
    @Override public void onClick() {
        super.onClick();
        new Thread(() -> {
            try {
                boolean to5G = getSharedPreferences("state", MODE_PRIVATE).getBoolean("next5g", true);
                boolean ok = to5G ? NetworkController.setPreferred5G(this) : NetworkController.setPreferred4G(this);
                getSharedPreferences("state", MODE_PRIVATE).edit().putBoolean("next5g", !to5G).apply();
                updateTile(to5G, ok);
            } catch (Throwable e) { updateTile(false, false); }
        }).start();
    }

    private void updateTile(boolean fiveG, boolean ok) {
        runOnUiThread(() -> {
            Tile tile = getQsTile(); if (tile == null) return;
            tile.setLabel(ok ? (fiveG ? "5G" : "4G") : "Network error");
            tile.setState(ok ? Tile.STATE_ACTIVE : Tile.STATE_UNAVAILABLE);
            tile.updateTile();
            if (!ok) Toast.makeText(this, "Open Network Switcher and check Shizuku.", Toast.LENGTH_SHORT).show();
        });
    }
}
