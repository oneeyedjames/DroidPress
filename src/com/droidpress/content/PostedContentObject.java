package com.droidpress.content;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.droidpress.content.ContentSchema.AuthoredColumns;
import com.droidpress.content.ContentSchema.EditableColumns;
import com.droidpress.os.Executable;

public abstract class PostedContentObject extends ContentObject
implements EditableColumns, AuthoredColumns {

	public PostedContentObject(Context context) {
		super(context);
	}

	public PostedContentObject(Context context, ContentValues values) {
		super(context, values);
	}

	public PostedContentObject(Context context, Cursor cursor) {
		super(context, cursor);
	}

	public int trash() {
		setStatus(STATUS_TRASH);

		return update();
	}

	public void trash(final Executable callback) {
		new Executable() {
			@Override
			public void run() {
				trash();

				if (callback != null)
					callback.execute();
			}
		}.executeInBackground();
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

	@Override
	public String getStatus() {
		return getString(STATUS);
	}

	@Override
	public void setStatus(String status) {
		put(STATUS, status);
	}
}