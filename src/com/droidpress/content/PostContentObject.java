package com.droidpress.content;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.droidpress.content.ContentSchema.PostColumns;

public abstract class PostContentObject extends PostedContentObject
implements PostColumns {
	protected static final Map<String, FieldType> sFieldTypeMap;

	static {
		sFieldTypeMap = new HashMap<String, FieldType>(ContentObject.sFieldTypeMap);
		sFieldTypeMap.put(_REMOTE_ID, FieldType.LONG);
		sFieldTypeMap.put(_AUTHOR_ID, FieldType.LONG);
		sFieldTypeMap.put(_REMOTE_AUTHOR_ID, FieldType.LONG);
		sFieldTypeMap.put(TITLE, FieldType.STRING);
		sFieldTypeMap.put(EXCERPT, FieldType.STRING);
		sFieldTypeMap.put(CONTENT, FieldType.STRING);
		sFieldTypeMap.put(PERMALINK, FieldType.URI);
		sFieldTypeMap.put(PUB_DATE, FieldType.DATE);
		sFieldTypeMap.put(MOD_DATE, FieldType.DATE);
		sFieldTypeMap.put(STATUS, FieldType.STRING);
		sFieldTypeMap.put(COMMENT_STATUS, FieldType.STRING);
	}

	public PostContentObject(Context context, ContentValues values) {
		super(context, values);
	}

	public PostContentObject(Context context, Cursor cursor) {
		super(context, cursor);
	}

	public PostContentObject(Context context) {
		super(context);
	}

	public boolean isPublished() {
		return STATUS_PUBLISH.equals(getStatus());
	}

	@Override
	public String getTitle() {
		return getString(TITLE);
	}

	@Override
	public void setTitle(String title) {
		put(TITLE, title);
	}

	@Override
	public String getExcerpt() {
		return getString(EXCERPT);
	}

	@Override
	public void setExcerpt(String excerpt) {
		put(EXCERPT, excerpt);
	}

	@Override
	public String getContent() {
		return getString(CONTENT);
	}

	@Override
	public void setContent(String content) {
		put(CONTENT, content);
	}

	@Override
	public Uri getPermalink() {
		return getUri(PERMALINK);
	}

	@Override
	public void setPermalink(Uri permalink) {
		put(PERMALINK, permalink);
	}

	@Override
	public String getCommentStatus() {
		return getString(COMMENT_STATUS);
	}

	@Override
	public void setCommentStatus(String commentStatus) {
		put(COMMENT_STATUS, commentStatus);
	}
}