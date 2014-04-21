package com.droidpress.content;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.droidpress.content.ContentObject.UriBuilder;
import com.droidpress.content.ContentSchema.AuthorColumns;
import com.droidpress.content.ContentSchema.CommentColumns;
import com.droidpress.content.ContentSchema.PostColumns;

public class ContentProvider extends android.content.ContentProvider {
	private static final String TAG = "ContentProvider";

	private static final String DATABASE_NAME = "";
	private static final int DATABASE_VERSION = 0;

	public interface SqlSelector {
		public String getTableName();
		public String getSelection();
		public String[] getSelectionArgs(Uri uri);
	}

	private DatabaseHelper mDatabaseHelper;

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (ContentUri.match(uri)) {
		case AUTHORS:
			return ContentSchema.AuthorColumns.CONTENT_TYPE;
		case AUTHOR_ID:
			return ContentSchema.AuthorColumns.CONTENT_ITEM_TYPE;
		case AUTHOR_POSTS:
		case POSTS:
			return ContentSchema.PostColumns.CONTENT_TYPE;
		case POST_ID:
			return ContentSchema.PostColumns.CONTENT_ITEM_TYPE;
			/* case PAGES:
			return ContentSchema.PageColumns.CONTENT_TYPE;
		case PAGE_ID:
			return ContentSchema.PageColumns.CONTENT_ITEM_TYPE; */
		case AUTHOR_COMMENTS:
		case POST_COMMENTS:
		case COMMENTS:
			return ContentSchema.CommentColumns.CONTENT_TYPE;
		case COMMENT_ID:
			return ContentSchema.CommentColumns.CONTENT_ITEM_TYPE;
		default:
			break;
		}

		Log.w(TAG, "Invalid content uri: " + uri);

		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String queryLimit = null;

		ContentUri contentUri = ContentUri.match(uri);

		switch (contentUri) {
		case AUTHORS:
			break;
		case AUTHOR_ID:
			break;
		case AUTHOR_POSTS:
			break;
		case AUTHOR_COMMENTS:
			break;
		case POSTS:
			break;
		case POST_ID:
			break;
		case POST_COMMENTS:
			break;
		case COMMENTS:
			break;
		case COMMENT_ID:
			break;
		default:
			throw new IllegalArgumentException("Illegal Content Uri: " + uri);
		}

		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, sortOrder, queryLimit);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tableName = null;

		ContentUri contentUri = ContentUri.match(uri);
		UriBuilder uriBuilder = null;

		String foreignKey;

		switch (contentUri) {
		case AUTHOR_POSTS:
			foreignKey = PostColumns._AUTHOR_ID;
			break;
		case AUTHOR_COMMENTS:
			foreignKey = CommentColumns._AUTHOR_ID;
			break;
		case POST_COMMENTS:
			foreignKey = CommentColumns._POST_ID;
			break;
		default:
			foreignKey = null;
			break;
		}

		if (foreignKey != null) {
			values.put(foreignKey, Long.parseLong(uri.getPathSegments()
					.get(contentUri.idPathPosition)));
		}

		switch (contentUri) {
		case AUTHORS:
			tableName = AuthorColumns.TABLE_NAME;
			uriBuilder = ContentUri.AUTHOR_ID;
			break;
		case AUTHOR_POSTS:
		case POSTS:
			tableName = PostColumns.TABLE_NAME;
			uriBuilder = ContentUri.POST_ID;
			break;
		case AUTHOR_COMMENTS:
		case POST_COMMENTS:
		case COMMENTS:
			tableName = CommentColumns.TABLE_NAME;
			uriBuilder = ContentUri.COMMENT_ID;
			break;
		default:
			throw new IllegalArgumentException("Illegal Content Uri: " + uri);
		}

		Uri rowUri = null;
		long rowId = 0;

		if (tableName != null) {
			SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

			rowId = db.insert(tableName, null, values);

			if (rowId > 0)
				rowUri = uriBuilder.build(rowId);

			if (rowUri != null)
				getContext().getContentResolver().notifyChange(rowUri, null);
		}

		return rowUri;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String tableName = null;

		ContentUri contentUri = ContentUri.match(uri);

		switch (contentUri) {
		case AUTHOR_ID:
		case POST_ID:
		case COMMENT_ID:
			tableName = contentUri.getTableName();
			selection = contentUri.getSelection();
			selectionArgs = contentUri.getSelectionArgs(uri);
			break;
		default:
			throw new IllegalArgumentException("Illegal Content Uri: " + uri);
		}

		int count = 0;

		if (tableName != null) {
			SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
			count = db.update(tableName, values, selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String tableName = null;

		ContentUri contentUri = ContentUri.match(uri);

		switch (contentUri) {
		case AUTHOR_ID:
		case AUTHOR_POSTS:
		case AUTHOR_COMMENTS:
		case POST_ID:
		case POST_COMMENTS:
		case COMMENT_ID:
			tableName = contentUri.getTableName();
			selection = contentUri.getSelection();
			selectionArgs = contentUri.getSelectionArgs(uri);
			break;
		default:
			throw new IllegalArgumentException("Illegal Content Uri: " + uri);
		}

		int count = 0;

		if (tableName != null) {
			SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
			count = db.delete(tableName, selection, selectionArgs);
		}

		return count;
	}

	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper() {
			super(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
}