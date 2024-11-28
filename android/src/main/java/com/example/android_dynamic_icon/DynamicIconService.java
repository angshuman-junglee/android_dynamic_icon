package com.example.android_dynamic_icon;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

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

        Notification notification = createNotification();

        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            type = FOREGROUND_SERVICE_TYPE_DATA_SYNC;
        }

        // Start the service in the foreground with the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, type);
        }

        return START_REDELIVER_INTENT;
    }

    private Notification createNotification() {
        // Create a notification channel (required for Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "my_channel",  // Channel ID
                    "My Foreground Service",  // Channel name
                    NotificationManager.IMPORTANCE_LOW);  // Importance level
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the actual notification
        return new NotificationCompat.Builder(this, "my_channel")
                .setContentTitle("Sync in Progress")
                .setContentText("Data is being synchronized in the background.")
                .setOngoing(true)  // This keeps the notification visible until the service stops
                .build();
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