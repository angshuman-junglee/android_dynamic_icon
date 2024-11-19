package com.example.android_dynamic_icon;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DynamicIconService extends Service {
    private static final String TAG = AndroidDynamicIconPlugin.getTAG();

    private final IconUtils iconUtils = new IconUtils();
    private boolean iconChanged;
    private List<String> classNames;
    private List<String> args;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (intent != null && intent.hasExtra("classNames")) {
            classNames = intent.getStringArrayListExtra("classNames");
            args = intent.getStringArrayListExtra("args");
            iconChanged = intent.getBooleanExtra("iconChanged", false);
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        iconUtils.updateIcon(this, iconChanged, classNames, args);
        Log.i(TAG, "updateIcon");
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}