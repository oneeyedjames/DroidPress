package com.droidpress.content;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthoredContentParser<T extends AuthoredContentObject> extends ContentParser<T> {
	public AuthoredContentParser(Context context, Class<T> contentClass) {
		super(context, contentClass);
	}

	@Override
	protected void bridge(T contentObject, JSONObject jsonObject)
			throws JSONException {
		contentObject.setRemoteId(jsonObject.getLong("ID"));
		contentObject.setStatus(jsonObject.getString("status"));
		contentObject.setPubDate(parseDate(jsonObject.getString("date")));
		contentObject.setModDate(parseDate(jsonObject.getString("modified")));
	}
}