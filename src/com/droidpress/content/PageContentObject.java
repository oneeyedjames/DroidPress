package com.droidpress.content;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.droidpress.content.ContentSchema.PageColumns;

public abstract class PageContentObject extends PostContentObject
implements PageColumns {
	protected static final Map<String, FieldType> sFieldTypeMap;

	static {
		sFieldTypeMap = new HashMap<String, FieldType>(PostContentObject.sFieldTypeMap);
		sFieldTypeMap.put(_PARENT_ID, FieldType.LONG);
		sFieldTypeMap.put(_REMOTE_PARENT_ID, FieldType.LONG);
	}

	public PageContentObject(Context context) {
		super(context);
	}

	public PageContentObject(Context context, ContentValues values) {
		super(context, values);
	}

	public PageContentObject(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public long getParentId() {
		return getLong(_PARENT_ID);
	}

	@Override
	public void setParentId(long parentId) {
		put(_PARENT_ID, parentId);
	}

	@Override
	public long getRemoteParentId() {
		return getLong(_REMOTE_PARENT_ID);
	}

	@Override
	public void setRemoteParentId(long remoteParentId) {
		put(_REMOTE_PARENT_ID, remoteParentId);
	}
}