package com.droidpress.os;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

public class QueryTask extends AsyncTask<Uri, Void, Cursor> {
	private Context mContext;

	public QueryTask(Context context) {
		super();
		mContext = context;
	}

	@Override
	protected Cursor doInBackground(Uri... params) {
		if (params.length > 0)
			return query(params[0]);

		return null;
	}

	protected Cursor query(Uri uri) {
		return mContext.getContentResolver()
				.query(uri, null, null, null, null);
	}
}