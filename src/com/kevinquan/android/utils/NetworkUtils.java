/*
 * Copyright 2014 Kevin Quan (kevin.quan@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevinquan.android.utils;

import java.lang.reflect.Method;

import com.kevinquan.android.utils.DeviceUtils.Permissions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Collection of utilities that relate to the network
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    
    // Change this flag to true if we want all network access to be off.
    public static boolean DEBUG_NETWORK_OFF_FLAG = false;

    /**
     * Checks whether there is an available network connection (e.g., on which to send data)
     * @param context The context to use to check the network connection
     * @return True if there is an available network connection
     */
    public static boolean hasNetworkConnection(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return !DEBUG_NETWORK_OFF_FLAG && netInfo != null && netInfo.isConnected();
    }
    
    /**
     * Attempts to check whether wifi hotspot is enabled on the device.  There is an API to do this,
     * but currently it is internal to Android.  To work around this, we use reflection to invoke the method.
     * 
     * If the method cannot be invoked successfully, false will be returned by default.
     * @param context The context to use to check the hotspot status
     * @return True if the hotspot is enabled and can be checked
     */
    public static boolean isWifiHotspotEnabled(Context context) {
    	if (!DeviceUtils.hasPermission(context, Permissions.WIFI_STATE)) {
    		Log.w(TAG, "Could not check whether wifi hotspot is enabled as context does not have permission "+Permissions.WIFI_STATE);
    		return false;
    	}
    	WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		// This API is not public yet.  When it is, we can just use that instead of reflection.
		//wifiApEnabled = wifiManager.isWifiApEnabled();
		Method apEnabledMethod = null;
		try {
			apEnabledMethod = wifiManager.getClass().getMethod("isWifiApEnabled");
			if (apEnabledMethod != null) {
				try {
					return (Boolean)apEnabledMethod.invoke(wifiManager);
				} catch (Exception e) {
					Log.w(TAG, "Could not invoke method to check whether Wifi AP is enabled or not.", e);
				}
			}
		} catch (NoSuchMethodException nsme) {
			Log.w(TAG, "Could not check whether hotspot is enabled.", nsme);
		}
		return false;
    }
}
