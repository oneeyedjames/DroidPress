package com.droidpress.http;

import java.io.UnsupportedEncodingException;

import android.net.Uri;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

public class HttpFactory {
	public static HttpUriRequest build(Uri uri, HttpMethod method)
			throws UnsupportedEncodingException {
		switch (method) {
		case GET:
			return new HttpGet(uri.toString());
		case POST:
			String url = uri.buildUpon().query(null).build().toString();

			HttpPost request = new HttpPost(url);

			request.setEntity(new StringEntity(uri.getQuery()));

			return request;
		default:
			throw new IllegalArgumentException("Invalid HTTP Method");
		}
	}
}