package com.droidpress.content;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthorContentParser extends ContentParser<AuthorContentObject> {

	public AuthorContentParser(Context context) {
		super(context, AuthorContentObject.class);
	}

	@Override
	protected void bridge(AuthorContentObject contentObject,
			JSONObject jsonObject) throws JSONException {

	}
}