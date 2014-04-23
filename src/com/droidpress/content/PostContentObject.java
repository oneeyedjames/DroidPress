package com.droidpress.content;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.droidpress.content.ContentSchema.PostColumns;

public class PostContentObject extends AuthoredContentObject
implements PostColumns {
	protected static final Map<String, FieldType> sFieldTypeMap;

	static {
		sFieldTypeMap = new HashMap<String, FieldType>(AuthoredContentObject.sFieldTypeMap);
		sFieldTypeMap.put(TITLE, FieldType.STRING);
		sFieldTypeMap.put(EXCERPT, FieldType.STRING);
		sFieldTypeMap.put(CONTENT, FieldType.STRING);
		sFieldTypeMap.put(PERMALINK, FieldType.URI);
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

	@Override
	protected UriBuilder getUriBuilder() {
		return ContentUri.POSTS;
	}

	@Override
	protected UriBuilder getItemUriBuilder() {
		return ContentUri.POST_ID;
	}

	public boolean isPublished() {
		return STATUS_PUBLISH.equals(getStatus());
	}

	@Override
	public String getType() {
		return getString(TYPE, TYPE_DEFAULT);
	}

	@Override
	public void setType(String type) {
		put(TYPE, type);
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