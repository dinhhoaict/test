package net.igame.solitaire;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;

import android.util.Log;

public class MyHttpClient {

	private static final String TAG = "MyHttpClient";

	public static void downloadHttp(final String strUrl, final DownloadComplete downloadComplete) {
		Log.e(TAG, "Real URL: " + strUrl);
		new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] byteReceiver = null;
				if (strUrl.startsWith("https")) {
					HttpsURLConnection httpsURLConnection = null;
					try {
						URL url = new URL(strUrl);
						httpsURLConnection = (HttpsURLConnection) url.openConnection();
						httpsURLConnection.connect();
						httpsURLConnection.setConnectTimeout(15000);
						InputStream is = httpsURLConnection.getInputStream();
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int count = 0;
						byte[] buffer = new byte[1024];
						while ((count = is.read(buffer)) != -1) {
							byteArrayOutputStream.write(buffer, 0, count);
						}
						byteReceiver = byteArrayOutputStream.toByteArray();

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (httpsURLConnection != null)
							httpsURLConnection.disconnect();
					}
				} else {
					HttpURLConnection httpURLConnection = null;
					try {
						URL url = new URL(strUrl);
						httpURLConnection = (HttpURLConnection) url.openConnection();
						httpURLConnection.setConnectTimeout(15000);
						httpURLConnection.connect();
						InputStream is = httpURLConnection.getInputStream();
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int count = 0;
						byte[] buffer = new byte[1024];
						while ((count = is.read(buffer)) != -1) {
							byteArrayOutputStream.write(buffer, 0, count);
						}

						byteReceiver = byteArrayOutputStream.toByteArray();

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (httpURLConnection != null)
							httpURLConnection.disconnect();
					}
				}

				if (byteReceiver != null) {
					if (downloadComplete != null) {
						downloadComplete.onDownloadComplete(byteReceiver);
					}

				} else {
					downloadComplete.onError(strUrl);
				}

			}
		}).start();
	}
	public static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }

	    return result.toString();
	}
	public static final void postHttp(final String strUrl, final String dataParams, final DownloadComplete downloadComplete) {
		Log.e(TAG, strUrl);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				byte[] byteReceiver = null;
				if (strUrl.startsWith("https")) {
					HttpsURLConnection httpsURLConnection = null;
					try {
						URL url = new URL(strUrl);
						httpsURLConnection = (HttpsURLConnection) url.openConnection();
						httpsURLConnection.setRequestMethod("POST");
						httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

						httpsURLConnection.setRequestProperty("Content-Length",
								"" + Integer.toString(dataParams.getBytes().length));
						httpsURLConnection.setRequestProperty("Content-Language", "en-US");

						httpsURLConnection.setUseCaches(false);
						httpsURLConnection.setDoInput(true);
						httpsURLConnection.setDoOutput(true);

						// Send request
						DataOutputStream wr = new DataOutputStream(httpsURLConnection.getOutputStream());
						wr.writeBytes(dataParams);
						wr.flush();
						wr.close();
//						httpURLConnection.connect();
						InputStream is = httpsURLConnection.getInputStream();
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int count = 0;
						byte[] buffer = new byte[1024];
						while ((count = is.read(buffer)) != -1) {
							byteArrayOutputStream.write(buffer, 0, count);
						}
						byteReceiver = byteArrayOutputStream.toByteArray();

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (httpsURLConnection != null)
							httpsURLConnection.disconnect();
					}
				} else {
					HttpURLConnection httpURLConnection = null;
					try {
						URL url = new URL(strUrl);
						httpURLConnection = (HttpURLConnection) url.openConnection();
						httpURLConnection.setRequestMethod("POST");
						httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

						httpURLConnection.setRequestProperty("Content-Length",
								"" + Integer.toString(dataParams.getBytes().length));
						httpURLConnection.setRequestProperty("Content-Language", "en-US");

						httpURLConnection.setUseCaches(false);
						httpURLConnection.setDoInput(true);
						httpURLConnection.setDoOutput(true);

						// Send request
						DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
						wr.writeBytes(dataParams);
						wr.flush();
						wr.close();
//						httpURLConnection.connect();
						
						//Get Response
						InputStream is = httpURLConnection.getInputStream();
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						int count = 0;
						byte[] buffer = new byte[1024];
						while ((count = is.read(buffer)) != -1) {
							byteArrayOutputStream.write(buffer, 0, count);
						}

						byteReceiver = byteArrayOutputStream.toByteArray();

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (httpURLConnection != null)
							httpURLConnection.disconnect();
					}
				}

				if (byteReceiver != null) {
					if (downloadComplete != null) {
						downloadComplete.onDownloadComplete(byteReceiver);
					}

				} else {
					downloadComplete.onError(strUrl);
				}

			}
		}).start();
	}

	public interface DownloadComplete {
		public void onDownloadComplete(byte[] bytes);

		public void onError(String urlStr);
	}
}
