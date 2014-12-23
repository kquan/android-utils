package com.kevinquan.google.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Implements pattern described in: http://stackoverflow.com/a/23863974/1339200
 *
 * Force re-registration of GCM ID if package is replaced (re-installed).
 * Note that this will remove the existing GCM ID which may be undesirable
 * depending on how many pushes the application gets
 *
 * Remember to add an entry into AndroidManifest with intent filter:
			<intent-filter>
                <action android:name="android.intent.action.MY_PACKAGE_REPLACED" />
            </intent-filter>
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public abstract class PackageReplacedReceiver extends BroadcastReceiver {

	private static final String TAG = PackageReplacedReceiver.class.getSimpleName();

	protected abstract Intent getRegisterGcmIdService(Context context);

	@Override
	public void onReceive(Context context, Intent intent) {
		if (context == null || intent == null) {
			return;
		}
		if (Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
			Log.d(TAG, "Own package is being replaced, Re-register GCM ID");
			removeExistingGcmId(context);
			context.startService(getRegisterGcmIdService(context));
		}
	}

	protected void removeExistingGcmId(Context context) {
		new Gcm().removeRegistrationId(context);
	}
}
