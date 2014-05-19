package com.droidpress.content;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class PostContentParser extends AuthoredContentParser<PostContentObject> {
	private static final String TAG = "PostContentParser";

	public PostContentParser(Context context) {
		super(context, PostContentObject.class);
	}

	@Override
	protected void bridge(PostContentObject contentObject,
			JSONObject jsonObject) throws JSONException {
		super.bridge(contentObject, jsonObject);

		contentObject.setType(jsonObject.getString("type"));
		contentObject.setTitle(jsonObject.getString("title"));
		contentObject.setExcerpt(jsonObject.getString("excerpt"));
		contentObject.setContent(jsonObject.getString("content"));
		contentObject.setPermalink(Uri.parse(jsonObject.getString("link")));
	}

	@Override
	protected void onException(JSONException e) {
		Log.i(TAG, "JSONException", e);
	}
}