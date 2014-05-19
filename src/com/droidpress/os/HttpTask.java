package com.droidpress.os;

import java.io.IOException;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import com.droidpress.http.HttpFactory;
import com.droidpress.http.HttpMethod;

public abstract class HttpTask extends AsyncTask<Uri, Void, String> {
	private static final String TAG = "HttpTask";

	@Override
	protected String doInBackground(Uri... params) {
		String result = null;

		if (params.length > 0) {
			AndroidHttpClient client = AndroidHttpClient
					.newInstance(getUserAgent());

			HttpUriRequest request;
			HttpResponse response;

			try {
				request = HttpFactory.build(params[0], getHttpMethod());

				prepareRequest(request);

				response = client.execute(request);

				processResponse(response);

				result = EntityUtils.toString(response.getEntity());
			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage());
			}

			client.close();
		}

		return result;
	}

	public abstract String getUserAgent();

	protected HttpMethod getHttpMethod() {
		return HttpMethod.GET;
	}

	protected void prepareRequest(HttpUriRequest request) {}

	protected void processResponse(HttpResponse response) {}
}