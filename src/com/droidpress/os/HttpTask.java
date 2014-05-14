package com.droidpress.os;

import java.io.IOException;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

public abstract class HttpTask extends AsyncTask<Uri, Void, String> {
	private static final String TAG = "HttpTask";

	@Override
	protected String doInBackground(Uri... params) {
		if (params.length > 0) {
			AndroidHttpClient client = AndroidHttpClient
					.newInstance(getUserAgent());

			HttpUriRequest request = new HttpGet(params[0].toString());

			prepareRequest(request);

			HttpResponse response;

			try {
				response = client.execute(request);

				processResponse(response);

				return EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
			}
		}

		return null;
	}

	public abstract String getUserAgent();

	protected void prepareRequest(HttpUriRequest request) {}

	protected void processResponse(HttpResponse response) {}
}