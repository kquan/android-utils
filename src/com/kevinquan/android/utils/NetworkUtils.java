
package com.kevinquan.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    @SuppressWarnings("unused")
    private static final String TAG = NetworkUtils.class.getSimpleName();
    
    // Change this flag to true if we want all network access to be off.
    public static boolean NETWORK_OFF_DEBUG_FLAG = false;

    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return !NETWORK_OFF_DEBUG_FLAG && netInfo != null && netInfo.isConnected();
    }
}
