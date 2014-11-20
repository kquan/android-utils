package com.kevinquan.google.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.kevinquan.android.utils.BuildUtils;

public class Gcm {

	private static final String TAG = Gcm.class.getSimpleName();
	
	protected static final String PREFERENCE_GCM_ID_KEY = "gcmRegistrationId";
	protected static final String PREFERENCE_GCM_APP_VERSION = "gcmAppVersion";
	
	public SharedPreferences getGcmPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public String getRegistrationId(Context context) {
		if (context == null) {
			return null;
		}
		SharedPreferences preferences = getGcmPreferences(context);
		String registrationId = preferences.getString(PREFERENCE_GCM_ID_KEY, null);
		if (TextUtils.isEmpty(registrationId)) {
			return null;
		}
		int appVersion = preferences.getInt(PREFERENCE_GCM_APP_VERSION, Integer.MIN_VALUE);
		int currentAppVersion = BuildUtils.getVersionCode(context);
		if (appVersion != currentAppVersion) {
			Log.v(TAG, "GCM registration ID found but app version was incorrect.  Found for version: "+appVersion);
			return null;
		}
		return registrationId;
	}
	
	public boolean hasRegistrationId(Context context) {
		return !TextUtils.isEmpty(getRegistrationId(context));
	}
	
	public boolean removeRegistrationId(Context context) {
		if (context == null) {
			return false;
		}
		SharedPreferences.Editor editor = getGcmPreferences(context).edit();
		// Cannot use apply() due to minSdk = 8
		editor.remove(PREFERENCE_GCM_ID_KEY).remove(PREFERENCE_GCM_APP_VERSION).commit();
		return true;
	}
	
	
	public boolean storeRegistrationId(Context context, String registrationId) {
		if (context == null || TextUtils.isEmpty(registrationId)) {
			return false;
		}
		SharedPreferences.Editor editor = getGcmPreferences(context).edit();
		int appVersion = BuildUtils.getVersionCode(context);
		// Cannot use apply() due to minSdk = 8
		editor.putString(PREFERENCE_GCM_ID_KEY, registrationId).putInt(PREFERENCE_GCM_APP_VERSION, appVersion).commit();
		Log.v(TAG, "New registration ID stored");
		return true;
	}
}
