package com.ibet88.utils.notification;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class Notification {

	public static final String TAG = "Notification";
	// Asyntask
//	static AsyncTask<Void, Void, Void> mRegisterTask;

	/**
	 * must be applicationContext
	 */
	static Context context;

	public static final void init(Activity activity) {
		Notification.context = activity.getApplicationContext();
		onCreate(activity);
	}

	public static void onCreate(final Activity activity) {
		// Check if Internet present
		// if (!cd.isConnectingToInternet()) {
		// // Internet Connection is not present
		// alert.showAlertDialog(MainActivity.this,
		// "Internet Connection Error",
		// "Please connect to working Internet connection", false);
		// // stop executing code by return
		// return;
		// }

		// Make sure the device has the proper dependencies.
		try {
			GCMRegistrar.checkDevice(context);
			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.
			GCMRegistrar.checkManifest(context);
		} catch (Exception e) {
			e.printStackTrace();
//			if (Config.DEBUG) {
//				activity.runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						Toast.makeText(activity, "Thiết bị hoặc cấu hình ứng dụng không hỗ trợ GCM!", Toast.LENGTH_SHORT).show();
//					}
//				});
//			}
			return;
		}

//		context.registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(context);
		// Check if regid already presents
		if (regId.equals("")) {
			
			// Registration is not present, register now with GCM
			GCMRegistrar.register(context, CommonUtilities.SENDER_ID);
		} else {
			// Device is already registered on GCM
//			if (GCMRegistrar.isRegisteredOnServer(context)) {
//				// Skips registration.
//				Toast.makeText(context, "Already registered with GCM", Toast.LENGTH_LONG).show();
//			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.

				// Register on our server
				// On server creates a new user
				ServerUtilities.register(context, "abc", "123", regId);
//			}
		}
	}

	/**
	 * Receiving push messages
	 * */
//	private static final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
//			// Waking up mobile if it is sleeping
//			WakeLocker.acquire(context);
//
//			/**
//			 * Take appropriate action on this message depending upon your app
//			 * requirement For now i am just displaying it on the screen
//			 * */
//
//			// Showing received message
//			// lblMessage.append(newMessage + "\n");
//			Toast.makeText(context, "New Message: " + newMessage, Toast.LENGTH_LONG).show();
//
//			// Releasing wake lock
//			WakeLocker.release();
//		}
//	};
	protected static void onDestroy() {
//		if (mRegisterTask != null) {
//			mRegisterTask.cancel(true);
//		}
		try {
//			context.unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(context);
		} catch (Exception e) {
		}
	}

	public static void onLogout() {
		// onDestroy();
	}
}
