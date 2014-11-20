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
package com.kevinquan.google;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Common utilities for working with Google Play Services
 * 
 * @author Kevin Quan (kevin.quan@gmail.com.com)
 *
 */
public class PlayServicesHelper {

    static final String TAG = PlayServicesHelper.class.getSimpleName();
    
    protected static final int TUTORIAL_PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    public static boolean hasUseableGooglePlayServices(Context context) {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        switch (result) {
            case ConnectionResult.SUCCESS: return true;
            case ConnectionResult.SERVICE_MISSING:
                Log.w(TAG, "No Google Play Services found on device.");
                return false;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.w(TAG, "Google Play Services found, but the version requires update.");
                return false;
            case ConnectionResult.SERVICE_DISABLED:
                Log.w(TAG, "Google Play Services found, but has been disabled.");
                return false;
            case ConnectionResult.SERVICE_INVALID:
                Log.w(TAG, "Google Play Services found, but has invalid signature.");
                return false;
                /*
            case ConnectionResult.DATE_INVALID:
                Log.w(TAG, "Google Play Services found, but current date is invalid.");
                return false;
                */
        }
        Log.w(TAG, "Unknown result code returned: "+result);
        return false;
    }
    
    public static boolean checkAndDownloadPlayServices(Activity activity) {
    	if (activity == null) {
    		return false;
    	}
    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
    	if (ConnectionResult.SUCCESS != resultCode) {
    		if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
    			GooglePlayServicesUtil.getErrorDialog(resultCode, activity, TUTORIAL_PLAY_SERVICES_RESOLUTION_REQUEST);
    		}
    		return false;
    	} else {
    		return true;
    	}
    }
}
