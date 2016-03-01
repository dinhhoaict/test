package com.ibet88.utils.notification;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService{
	private static final String TAG = "MyInstanceID";
	@Override
	public void onTokenRefresh() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onTokenRefresh");
		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);
	}
}
