package com.example.android_dynamic_icon;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.flutter.plugin.common.MethodCall;

public class IconUtils {
    IconUtils() {

    }

    private static final String TAG = AndroidDynamicIconPlugin.getTAG();

    private static List<String> classNames = null;
    private static List<String> args =  new ArrayList<>();

     void initialize(MethodCall call) {
        classNames = call.arguments();
    }

     void changeIcon(MethodCall call, Activity activity) {
        if(classNames == null || classNames.isEmpty()) {
            Log.e(TAG,"Initialization Failed!");
            Log.i(TAG,"List all the activity-alias class names in initialize()");
            return;
        }

        args = call.arguments();
        String currentClassName = activity.getComponentName().getClassName();
        Log.i(TAG, "CurrentClassName: " + currentClassName);

        if (currentClassName.equals(args.get(0))) {
            Log.d(TAG, "Current activity is already " + currentClassName);
            return;
        }
        startIconService(activity);

    }

    private void startIconService(Activity activity){
        Intent intent = new Intent(activity, DynamicIconService.class);
        intent.putExtra("iconChanged", true);
        intent.putStringArrayListExtra("classNames", new ArrayList<>(classNames));
        intent.putStringArrayListExtra("args", new ArrayList<>(args));

        ContextCompat.startForegroundService(activity, intent);
    }



    void updateIcon(Context context, boolean iconChanged, List<String> classNames, List<String> args) {
         try {
             Log.d(TAG, "iconChanged = " + iconChanged);

             if (iconChanged) {
                 iconChanged = false;
                 String className = args.get(0);
                 PackageManager pm = context.getPackageManager();
                 String packageName = context.getPackageName();
                 boolean classNameFound = false;

                 for (String alias : classNames) {
                     ComponentName cn = new ComponentName(packageName, alias);
                     int componentState = className.equals(alias)
                             ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                             : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                     pm.setComponentEnabledSetting(cn, componentState, PackageManager.DONT_KILL_APP);
                     if (className.equals(alias)) {
                         classNameFound = true;
                     }
                 }

                 if (!classNameFound) {
                     Log.e(TAG, "class name " + className + " did not match in the initialized list.");
                     return;
                 }

                 Log.d(TAG, "Icon switched to " + className);

             }
             Log.d(TAG, "completed = " + iconChanged);

         } catch (Exception e){
             Log.e(TAG, "icon switch error", e);
        }
    }
}
