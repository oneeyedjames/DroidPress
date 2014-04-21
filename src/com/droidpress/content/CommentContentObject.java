package com.droidpress.content;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.droidpress.content.ContentSchema.CommentColumns;

public abstract class CommentContentObject extends AuthoredContentObject
implements CommentColumns {
	protected static final Map<String, FieldType> sFieldTypeMap;

	static {
		sFieldTypeMap = new HashMap<String, FieldType>(AuthoredContentObject.sFieldTypeMap);
		sFieldTypeMap.put(_PARENT_ID, FieldType.LONG);
		sFieldTypeMap.put(_REMOTE_PARENT_ID, FieldType.LONG);
		sFieldTypeMap.put(_POST_ID, FieldType.LONG);
		sFieldTypeMap.put(_REMOTE_POST_ID, FieldType.LONG);
		sFieldTypeMap.put(CONTENT, FieldType.STRING);
	}

	public CommentContentObject(Context context) {
		super(context);
	}

	public CommentContentObject(Context context, ContentValues values) {
		super(context, values);
	}

	public CommentContentObject(Context context, Cursor cursor) {
		super(context, cursor);
	}

	@Override
	public Uri save() {
		return save(true);
	}

	public Uri save(boolean saveDates) {
		if (saveDates)
			saveDates();

		return super.save();
	}

	private void saveDates() {
		Date now = new Date();

		if (getPubDate() == null)
			setPubDate(now);

		setModDate(now);
	}

	public boolean isApproved() {
		return STATUS_APPROVED.equals(getStatus());
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
	public long getPostId() {
		return getLong(_POST_ID);
	}

	@Override
	public void setPostId(long postId) {
		put(_POST_ID, postId);
	}

	@Override
	public long getRemotePostId() {
		return getLong(_REMOTE_POST_ID);
	}

	@Override
	public void setRemotePostId(long remotePostId) {
		put(_REMOTE_POST_ID, remotePostId);
	}

	@Override
	public String getContent() {
		return getString(CONTENT);
	}

	@Override
	public void setContent(String content) {
		put(CONTENT, content);
	}
}