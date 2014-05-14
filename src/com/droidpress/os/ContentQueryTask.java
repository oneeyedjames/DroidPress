package com.droidpress.os;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.droidpress.content.ContentObject;

public class ContentQueryTask<T extends ContentObject>
extends AsyncTask<Uri, Void, List<T>> {
	private Context mContext;
	private Class<T> mContentClass;

	public ContentQueryTask(Context context, Class<T> contentClass) {
		super();

		mContext = context;
		mContentClass = contentClass;
	}

	@Override
	protected List<T> doInBackground(Uri... params) {
		if (params.length > 0) {
			Cursor cursor = query(params[0]);

			if (cursor != null) {
				List<T> content = new LinkedList<T>();

				ContentObject.Factory<T> factory = new ContentObject
						.Factory<T>(mContext, mContentClass);

				while (cursor.moveToNext())
					content.add(factory.newInstance(cursor));

				cursor.close();

				return content;
			}
		}

		return null;
	}

	protected Cursor query(Uri uri) {
		return mContext.getContentResolver()
				.query(uri, null, null, null, null);
	}
}