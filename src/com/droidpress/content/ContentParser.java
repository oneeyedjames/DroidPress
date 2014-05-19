package com.droidpress.content;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ContentParser<T extends ContentObject> {
	private static final String TAG = "ContentParser";

	private static final String DATE_FORMAT;
	private static final DateFormat DATE_PARSER;

	static {
		DATE_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'sszzzzz";
		DATE_PARSER = new SimpleDateFormat(DATE_FORMAT);
	}

	protected static Date parseDate(String dateString) {
		try {
			return DATE_PARSER.parse(dateString);
		} catch (ParseException e) {
			Log.e(TAG, "ParseException", e);
			return null;
		}
	}

	private Context  mContext;
	private Class<T> mContentClass;

	private ContentObject.Factory<T> mFactory;

	public ContentParser(Context context, Class<T> contentClass) {
		mContext = context;
		mContentClass = contentClass;
	}

	public Context getContext() {
		return mContext;
	}

	public T parseObject(String json) {
		try {
			return parseObject(new JSONObject(json));
		} catch (JSONException e) {
			onException(e);
			return null;
		}
	}

	public T parseObject(JSONObject jsonObject) {
		try {
			T contentObject = newInstance();
			bridge(contentObject, jsonObject);
			return contentObject;
		} catch (JSONException e) {
			onException(e);
			return null;
		}
	}

	public List<T> parseArray(String json) {
		try {
			return parseArray(new JSONArray(json));
		} catch (JSONException e) {
			onException(e);
			return null;
		}
	}

	public List<T> parseArray(JSONArray jsonArray) {
		try {
			List<T> list = new LinkedList<T>();
			for (int i = 0, n = jsonArray.length(); i < n; i++)
				list.add(parseObject(jsonArray.getJSONObject(i)));
			return list;
		} catch (JSONException e) {
			onException(e);
			return null;
		}
	}

	protected ContentObject.Factory<T> getFactory() {
		if (mFactory == null)
			mFactory = new ContentObject.Factory<T>(mContext, mContentClass);

		return mFactory;
	}

	protected T newInstance() {
		return getFactory().newInstance(new ContentValues());
	}

	protected abstract void bridge(T contentObject, JSONObject jsonObject)
			throws JSONException;

	protected void onException(JSONException e) {}
}