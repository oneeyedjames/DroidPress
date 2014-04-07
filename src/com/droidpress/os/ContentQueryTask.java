package com.droidpress.os;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.droidpress.content.ContentObject;

public class ContentQueryTask<T extends ContentObject>
extends AsyncTask<Uri, Void, T> {
	private Context mContext;
	private Class<T> mContentClass;

	public ContentQueryTask(Context context, Class<T> contentClass) {
		super();

		mContext = context;
		mContentClass = contentClass;
	}

	@Override
	protected T doInBackground(Uri... params) {
		if (params.length > 0) {
			Cursor cursor = query(params[0]);

			ContentObject.Factory<T> factory = new ContentObject
					.Factory<T>(mContext, mContentClass);

			return factory.newInstance(cursor);
		}

		return null;
	}

	protected Cursor query(Uri uri) {
		return mContext.getContentResolver()
				.query(uri, null, null, null, null);
	}
}