package com.ibet88.utils.notification;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gcm.GCMRegistrar;

public class Notification {

	public static final String TAG = "Notification";
	// Asyntask
	// static AsyncTask<Void, Void, Void> mRegisterTask;

	/**
	 * must be applicationContext
	 */
	static Context context;

	public static final void init(Activity activity, String username) {
		Notification.context = activity.getApplicationContext();
		onCreate(activity, username);
	}

	public static void onCreate(final Activity activity, String username) {
		try {
			GCMRegistrar.checkDevice(context);
			GCMRegistrar.checkManifest(context);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(context);
		// Check if regid already presents
		if (regId.equals("")) {

			// Registration is not present, register now with GCM
			GCMRegistrar.register(context, CommonUtilities.SENDER_ID);
		} else {
			AccessToken accessToken = AccessToken.getCurrentAccessToken();
			Log.e(TAG, "regId = " + regId);
			if (username.length() > 0)
				ServerUtilities.register(context, username,
						accessToken.getUserId(), regId);
			// }
		}
	}

	/**
	 * Receiving push messages
	 * */
	// private static final BroadcastReceiver mHandleMessageReceiver = new
	// BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String newMessage =
	// intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
	// // Waking up mobile if it is sleeping
	// WakeLocker.acquire(context);
	//
	// /**
	// * Take appropriate action on this message depending upon your app
	// * requirement For now i am just displaying it on the screen
	// * */
	//
	// // Showing received message
	// // lblMessage.append(newMessage + "\n");
	// Toast.makeText(context, "New Message: " + newMessage,
	// Toast.LENGTH_LONG).show();
	//
	// // Releasing wake lock
	// WakeLocker.release();
	// }
	// };
	protected static void onDestroy() {
		// if (mRegisterTask != null) {
		// mRegisterTask.cancel(true);
		// }
		try {
			// context.unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(context);
		} catch (Exception e) {
		}
	}

	public static void onLogout() {
		// onDestroy();
	}
}
