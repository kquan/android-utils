package com.kevinquan.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

public class DeviceUtils {

    @SuppressWarnings("unused")
    private static final String TAG = DeviceUtils.class.getSimpleName();
    
    public static final String SCHEME_FILE = "file://";
    
    public interface Permissions {
        public static final String INTERNET = "android.permission.INTERNET";
        public static final String NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
        public static final String WRITE_SD = "android.permission.WRITE_EXTERNAL_STORAGE";
    }

    public static int convertDpToPixels(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // 0.5 is added to allow proper rounding when truncating
        return (int) ((dp * displayMetrics.density) + 0.5);
    }
    
    public static DisplayMetrics getWindowMetrics(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
    
    public static boolean hasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

}
