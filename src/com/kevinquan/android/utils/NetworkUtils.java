/*
 * Copyright 2013 Kevin Quan (kevin.quan@gmail.com)
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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Collection of utilities that relate to the network
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class NetworkUtils {

    @SuppressWarnings("unused")
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
}
