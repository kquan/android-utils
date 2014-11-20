package com.kevinquan.google.gcm;

import java.io.IOException;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kevinquan.android.utils.DeviceUtils;
import com.kevinquan.android.utils.DeviceUtils.Permissions;
import com.kevinquan.google.GooglePermissions;

public class GcmUtils {

	private static final String TAG = GcmUtils.class.getSimpleName();
	
	protected static final String GCM_APP_PERMISSION_SUFFIX = ".permission.C2D_MESSAGE";
	
	public static boolean hasPermissionsForGcm(Context context) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN
				&& !DeviceUtils.hasPermission(context, Permissions.GET_ACCOUNTS)) {
			// Doc requirement is actually Android 4.0.4
			return false;
		}
		return DeviceUtils.hasPermission(context, GooglePermissions.CLOUD_MESSAGING) 
				&& DeviceUtils.hasPermission(context, Permissions.INTERNET) 
				&& DeviceUtils.hasPermission(context, context.getPackageName()+GCM_APP_PERMISSION_SUFFIX);
	}
	
	public static String registerForGcmId(Context context, String senderId, Gcm helper) {
		if (context == null) {
			return null;
		}
		if (helper == null) {
			helper = new Gcm();
		}
		String currentId = helper.getRegistrationId(context);
		if (!TextUtils.isEmpty(currentId)) {
			Log.d(TAG, "Not registering for GCM as a GCM ID already exists: "+currentId);
			return currentId;
		}
		
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		boolean success = false;
		String newRegistrationId = null;
		try {
			newRegistrationId = gcm.register(senderId);
		} catch (IOException ioe) {
			Log.e(TAG, "Could not register GCM ID", ioe);
			return null;
		}
		if (!TextUtils.isEmpty(newRegistrationId)) {
			success = helper.storeRegistrationId(context, newRegistrationId);
		}

		if (!success) {
			Log.w(TAG, "GCM ID was not registered successfully");
			return null;
		}
		return newRegistrationId;
	}

}
