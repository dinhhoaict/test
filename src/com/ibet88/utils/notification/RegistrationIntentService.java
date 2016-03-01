package com.ibet88.utils.notification;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class RegistrationIntentService extends IntentService {

	private static final String TAG = "RegIntentService";
	private static final String[] TOPICS = { "global" };
	private static final String SENDER_ID = "1045194114354"; // Server API key: AIzaSyAAqOyZNEiOu4P9JA7iu7t3EQZ-c1JMpIk
	
	
	public RegistrationIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		try {
			// [START register_for_gcm]
			// Initially this call goes out to the network to retrieve the
			// token, subsequent calls
			// are local.
			// R.string.gcm_defaultSenderId (the Sender ID) is typically derived
			// from google-services.json.
			// See https://developers.google.com/cloud-messaging/android/start
			// for details on this file.
			// [START get_token]
			InstanceID instanceID = InstanceID.getInstance(this);
			String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			// [END get_token]
			Log.i(TAG, "GCM Registration Token: " + token);

			// TODO: Implement this method to send any registration to your
			// app's servers.
			sendRegistrationToServer(token);

			// [END register_for_gcm]
		} catch (Exception e) {
			Log.d(TAG, "Failed to complete token refresh", e);
		}
		// Notify UI that registration has completed, so the progress indicator
		// can be hidden.
//		Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
//		LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
	}

	/**
	 * Persist registration to third-party servers.
	 *
	 * Modify this method to associate the user's GCM registration token with
	 * any server-side account maintained by your application.
	 *
	 * @param token
	 *            The new token.
	 */
	private void sendRegistrationToServer(String token) {
		// Add custom implementation, as needed.
	}

}