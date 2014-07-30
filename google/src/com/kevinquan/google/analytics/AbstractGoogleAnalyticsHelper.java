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
package com.kevinquan.google.analytics;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kevinquan.android.utils.DeviceUtils;
import com.kevinquan.android.utils.GoogleAnalyticsUtils.BaseAnalytics;

/**
 * Helper class for common Google Analytics calls
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class AbstractGoogleAnalyticsHelper implements BaseAnalytics {

	private static final String TAG = AbstractGoogleAnalyticsHelper.class.getSimpleName();
	
    // Temporary: Copied from libGoogleAnalyticsServices.jar as Fields type does not exist in Play Services yet.
    private interface Fields {
    	static final java.lang.String SCREEN_NAME = "&cd";
    }
    
    private static final int NO_CONFIGURATION_FILE_ID = -1;
    
    protected int mConfigurationFileId;
    protected static AbstractGoogleAnalyticsHelper mInstance;
    protected Tracker mConfiguredTracker;
    
    public AbstractGoogleAnalyticsHelper(int configurationFileId) { 
    	mConfigurationFileId = configurationFileId;
    }
    
    protected Tracker getTracker(Context context) {
    	if (mConfiguredTracker == null) {
        	if (context == null) {
        		Log.w(TAG, "Could not instantiate analytics tracker as context does not exist.");
        		return null;
        	} else if (mConfigurationFileId == NO_CONFIGURATION_FILE_ID) {
        		Log.w(TAG, "Could not instantiate analytics tracker as configuration not provided.");
        		return null;
        	}
        	mConfiguredTracker = GoogleAnalytics.getInstance(context).newTracker(mConfigurationFileId);
    	}
    	return mConfiguredTracker;
    }
    
    public Tracker sendView(Activity activity, String screenName) {
        Tracker tracker = getTracker(activity);
        if (tracker == null) {
        	Log.w(TAG, "Could not log screen view for "+screenName+" as tracker could not be retrieved.");
        	return null;
        }
        tracker.set(Fields.SCREEN_NAME, screenName);
        tracker.send(new HitBuilders.AppViewBuilder().build());
        return tracker;
    }
    
    public Tracker sendOrientation(Activity activity, Tracker tracker) {
        if (tracker == null) {
            tracker = getTracker(activity);    
        }
        if (tracker == null) {
        	Log.w(TAG, "Could not log orientation as tracker could not be retrieved.");
        	return null;
        }
        int orientation = DeviceUtils.getScreenOrientation(activity);
        String label = null;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            label = BaseAnalytics.ORIENTATION_PORTRAIT;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            label = BaseAnalytics.ORIENTATION_LANDSCAPE;
        }
        if (!TextUtils.isEmpty(label)) {
            tracker.send(new HitBuilders.EventBuilder(BaseAnalytics.CATEGORY_NAVIGATION, BaseAnalytics.ACTION_ORIENTATION).setLabel(label).build());
        }
        return tracker;
    }
    
    public Tracker sendEvent(Context context, String category, String action, String label) {
    	return sendEvent(context, category, action, label, 0L);
    }
    
    public Tracker sendEvent(Context context, String category, String action, String label, long value) {
    	HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder(category, action);
    	if (!TextUtils.isEmpty(label)) {
    		builder.setLabel(label);
    	}
    	if (value != 0) {
    		builder.setValue(value);
    	}
    	Tracker tracker = getTracker(context);
    	if (tracker != null) {
    		tracker.send(builder.build());
    	} else {
        	Log.w(TAG, "Could not log event for "+category+"/"+action+" as tracker could not be retrieved.");
        }
    	return tracker;
    }
    
    public Tracker sendTiming(Context context, String category, long timing, String action, String label) {
    	HitBuilders.TimingBuilder builder = new HitBuilders.TimingBuilder(category, action, timing);
    	if (!TextUtils.isEmpty(label)) {
    		builder.setLabel(label);
    	}
    	Tracker tracker = getTracker(context);
    	if (tracker != null) {
    		tracker.send(builder.build());
    	} else {
        	Log.w(TAG, "Could not log timing for "+category+"/"+action+" as tracker could not be retrieved.");
    	}
    	return tracker;
    }
    
    public Tracker sendActivityStart(Activity activity) {
    	// Legacy support: https://groups.google.com/forum/#!topic/ga-mobile-app-analytics/fO4l75d7d3I
    	// https://developers.google.com/analytics/devguides/collection/android/v4/sessions
    	//
    	// It looks like we just need to send an app view.  There doesn't seem to be a way to send a session end anymore.
    	return sendView(activity, activity.getClass().getName());
    }
    
    public Tracker sendActivityStop(Activity activity) {
    	// Legacy support: https://groups.google.com/forum/#!topic/ga-mobile-app-analytics/fO4l75d7d3I
    	// https://developers.google.com/analytics/devguides/collection/android/v4/sessions
    	//
    	// There doesn't seem to be a way to send a session end anymore.
    	return getTracker(activity);
    }

}
