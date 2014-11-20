package com.kevinquan.google.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class RegisterGcmService extends IntentService {

	private static final String TAG = RegisterGcmService.class.getSimpleName();
	
	protected static final String EXTRA_SENDER_ID = "ExtraSenderId";
	
	public RegisterGcmService() {
		super(TAG);
	}
	
	protected RegisterGcmService(String name) {
		super(name);
	}

	protected Gcm getGcmHelper() {
		return new Gcm();
	}
	
	protected String getSenderId(Intent intent) {
		return intent != null ? intent.getStringExtra(EXTRA_SENDER_ID) : null;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String senderId = getSenderId(intent);
		if (TextUtils.isEmpty(senderId)) {
			Log.w(TAG, "Not registering for GCM as No sender ID provided");
			return;
		}
		GcmUtils.registerForGcmId(this, senderId, getGcmHelper());
	}
}
