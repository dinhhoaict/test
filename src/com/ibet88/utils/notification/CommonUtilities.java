package com.ibet88.utils.notification;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
	public final static String SERVER_URL = "http://123.30.50.73/cmsv2/gcm_server_php/register.php";

    // Google project id
//    public static final String SENDER_ID = "234669545383";
	// Game4U
    public static final String SENDER_ID = "545878525556";// server: AIzaSyAw09evq9C30aJw4jinXaf9gKgVkTKbFhg

    /**
     * Tag used on log messages.
     */
    static final String TAG = "CommonUtilities";
    
    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
