package com.example.android_dynamic_icon;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.content.Intent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.app.PendingIntent;
import android.app.AlarmManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** AndDynamicIconPlugin */
public class MethodCallImplementation implements MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  private Activity activity;
  private static final String TAG = AndroidDynamicIconPlugin.getTAG();
  IconUtils iconUtils = new IconUtils();


  MethodCallImplementation(Context context, Activity activity) {
    this.context = context;
    this.activity = activity;
  }

  void setActivity(Activity activity) {
      this.activity = activity;
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
      switch (call.method) {
        case "initialize":
          {

              iconUtils.initialize(call);
              break;
          }
        case "changeIcon":
          {
              iconUtils.changeIcon(call, activity);
              break;
          }
        default:
            result.notImplemented();
            break;
      }
  }
}
