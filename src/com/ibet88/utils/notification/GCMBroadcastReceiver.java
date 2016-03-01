package com.ibet88.utils.notification;

import android.content.Context;

import com.google.android.gcm.GCMConstants;

public class GCMBroadcastReceiver extends com.google.android.gcm.GCMBroadcastReceiver {
	
	/**
	 * Gets the class name of the intent service that will handle GCM messages.
	 */
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return getDefaultIntentServiceClassName(context);
	}

	/**
	 * Gets the default class name of the intent service that will handle GCM
	 * messages.
	 */
	static final String getDefaultIntentServiceClassName(Context context) {
		String className = "com.ibet88.utils.notification" + GCMConstants.DEFAULT_INTENT_SERVICE_CLASS_NAME;
		return className;
	}
	
	
}
