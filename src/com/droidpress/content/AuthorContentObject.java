package com.droidpress.content;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.droidpress.content.ContentSchema.AuthorColumns;

public abstract class AuthorContentObject extends ContentObject
implements AuthorColumns {
	protected static final Map<String, FieldType> sFieldTypeMap;

	static {
		sFieldTypeMap = new HashMap<String, FieldType>(ContentObject.sFieldTypeMap);
		sFieldTypeMap.put(_REMOTE_ID, FieldType.LONG);
		sFieldTypeMap.put(NAME, FieldType.STRING);
		sFieldTypeMap.put(EMAIL, FieldType.STRING);
		sFieldTypeMap.put(URL, FieldType.STRING);
	}

	public AuthorContentObject(Context context) {
		super(context);
	}

	public AuthorContentObject(Context context, ContentValues values) {
		super(context, values);
	}

	public AuthorContentObject(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public long getRemoteId() {
		return getLong(_REMOTE_ID);
	}

	@Override
	public void setRemoteId(long remoteId) {
		put(_REMOTE_ID, remoteId);
	}

	@Override
	public String getName() {
		return getString(NAME);
	}

	@Override
	public void setName(String name) {
		put(NAME, name);
	}

	@Override
	public String getEmail() {
		return getString(EMAIL);
	}

	@Override
	public void setEmail(String email) {
		put(EMAIL, email);
	}

	@Override
	public Uri getUrl() {
		return getUri(URL);
	}

	@Override
	public void setUrl(Uri url) {
		put(URL, url);
	}
}