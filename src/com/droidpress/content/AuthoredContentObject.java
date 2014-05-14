package com.droidpress.content;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.droidpress.content.ContentSchema.AuthoredColumns;
import com.droidpress.content.ContentSchema.EditableColumns;
import com.droidpress.content.ContentSchema.NestedColumns;

public abstract class AuthoredContentObject extends ContentObject
implements EditableColumns, NestedColumns, AuthoredColumns {
	protected static final Map<String, FieldType> sFieldTypeMap;

	static {
		sFieldTypeMap = new HashMap<String, FieldType>(ContentObject.sFieldTypeMap);
		sFieldTypeMap.put(_REMOTE_ID, FieldType.LONG);
		sFieldTypeMap.put(_AUTHOR_ID, FieldType.LONG);
		sFieldTypeMap.put(_REMOTE_AUTHOR_ID, FieldType.LONG);
		sFieldTypeMap.put(_PARENT_ID, FieldType.LONG);
		sFieldTypeMap.put(_REMOTE_PARENT_ID, FieldType.LONG);
		sFieldTypeMap.put(STATUS, FieldType.STRING);
		sFieldTypeMap.put(PUB_DATE, FieldType.DATE);
		sFieldTypeMap.put(MOD_DATE, FieldType.DATE);
	}

	public AuthoredContentObject(Context context) {
		super(context);
	}

	public AuthoredContentObject(Context context, ContentValues values) {
		super(context, values);
	}

	public AuthoredContentObject(Context context, Cursor cursor) {
		super(context, cursor);
	}

	public int trash() {
		setStatus(STATUS_TRASH);

		return update();
	}

	public void trash(final Runnable callback) {
		runInBackground(new Runnable() {
			@Override
			public void run() {
				trash();
				runOnMainThread(callback);
			}
		});
	}

	public boolean hasRemote() {
		return getRemoteId() > 0;
	}

	public boolean isPending() {
		return STATUS_PENDING.equals(getStatus());
	}

	public boolean isTrashed() {
		return STATUS_TRASH.equals(getStatus());
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
	public long getAuthorId() {
		return getLong(_AUTHOR_ID);
	}

	@Override
	public void setAuthorId(long authorId) {
		put(_AUTHOR_ID, authorId);
	}

	@Override
	public long getRemoteAuthorId() {
		return getLong(_REMOTE_AUTHOR_ID);
	}

	@Override
	public void setRemoteAuthorId(long remoteAuthorId) {
		put(_REMOTE_AUTHOR_ID, remoteAuthorId);
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

	@Override
	public String getStatus() {
		return getString(STATUS);
	}

	@Override
	public void setStatus(String status) {
		put(STATUS, status);
	}

	@Override
	public Date getPubDate() {
		return getDate(PUB_DATE);
	}

	@Override
	public void setPubDate(Date pubDate) {
		put(PUB_DATE, pubDate);
	}

	@Override
	public Date getModDate() {
		return getDate(MOD_DATE);
	}

	@Override
	public void setModDate(Date modDate) {
		put(MOD_DATE, modDate);
	}
}