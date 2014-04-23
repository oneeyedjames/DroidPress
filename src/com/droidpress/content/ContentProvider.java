package com.droidpress.content;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.droidpress.content.ContentObject.UriBuilder;
import com.droidpress.content.ContentSchema.AuthorColumns;
import com.droidpress.content.ContentSchema.CommentColumns;
import com.droidpress.content.ContentSchema.PostColumns;
import com.droidpress.content.ContentSchema.TaxonomyColumns;
import com.droidpress.content.ContentSchema.TermColumns;

public class ContentProvider extends android.content.ContentProvider {
	private static final String DATABASE_NAME = "droidpress.sqlite";
	private static final int DATABASE_VERSION = 1;

	private static final ProjectionMap sAuthorColumnMap;
	private static final ProjectionMap sPostColumnMap;
	private static final ProjectionMap sCommentColumnMap;
	private static final ProjectionMap sTaxonomyColumnMap;
	private static final ProjectionMap sTermColumnMap;

	static {
		sAuthorColumnMap = new ProjectionMap(AuthorColumns.TABLE_NAME);
		sAuthorColumnMap.addColumn(AuthorColumns._ID);
		sAuthorColumnMap.addColumn(AuthorColumns._REMOTE_ID);
		sAuthorColumnMap.addColumn(AuthorColumns.NAME);
		sAuthorColumnMap.addColumn(AuthorColumns.EMAIL);
		sAuthorColumnMap.addColumn(AuthorColumns.URL);

		sPostColumnMap = new ProjectionMap(PostColumns.TABLE_NAME);
		sPostColumnMap.addColumn(PostColumns._ID);
		sPostColumnMap.addColumn(PostColumns._REMOTE_ID);
		sPostColumnMap.addColumn(PostColumns._PARENT_ID);
		sPostColumnMap.addColumn(PostColumns._REMOTE_PARENT_ID);
		sPostColumnMap.addColumn(PostColumns._AUTHOR_ID);
		sPostColumnMap.addColumn(PostColumns._REMOTE_AUTHOR_ID);
		sPostColumnMap.addColumn(PostColumns.TYPE);
		sPostColumnMap.addColumn(PostColumns.TITLE);
		sPostColumnMap.addColumn(PostColumns.EXCERPT);
		sPostColumnMap.addColumn(PostColumns.CONTENT);
		sPostColumnMap.addColumn(PostColumns.PERMALINK);
		sPostColumnMap.addColumn(PostColumns.STATUS);
		sPostColumnMap.addColumn(PostColumns.PUB_DATE);
		sPostColumnMap.addColumn(PostColumns.MOD_DATE);
		sPostColumnMap.addColumn(PostColumns.COMMENT_STATUS);

		sCommentColumnMap = new ProjectionMap(CommentColumns.TABLE_NAME);
		sCommentColumnMap.addColumn(CommentColumns._ID);
		sCommentColumnMap.addColumn(CommentColumns._REMOTE_ID);
		sCommentColumnMap.addColumn(CommentColumns._PARENT_ID);
		sCommentColumnMap.addColumn(CommentColumns._REMOTE_PARENT_ID);
		sCommentColumnMap.addColumn(CommentColumns._AUTHOR_ID);
		sCommentColumnMap.addColumn(CommentColumns._REMOTE_AUTHOR_ID);
		sCommentColumnMap.addColumn(CommentColumns._POST_ID);
		sCommentColumnMap.addColumn(CommentColumns._REMOTE_POST_ID);
		sCommentColumnMap.addColumn(CommentColumns.CONTENT);
		sCommentColumnMap.addColumn(CommentColumns.STATUS);
		sCommentColumnMap.addColumn(CommentColumns.PUB_DATE);
		sCommentColumnMap.addColumn(CommentColumns.MOD_DATE);

		sTaxonomyColumnMap = new ProjectionMap(TaxonomyColumns.TABLE_NAME);
		sTaxonomyColumnMap.addColumn(TaxonomyColumns._ID);
		sTaxonomyColumnMap.addColumn(TaxonomyColumns._REMOTE_ID);
		sTaxonomyColumnMap.addColumn(TaxonomyColumns.TITLE);
		sTaxonomyColumnMap.addColumn(TaxonomyColumns.HIERARCHICAL);

		sTermColumnMap = new ProjectionMap(TermColumns.TABLE_NAME);
		sTermColumnMap.addColumn(TermColumns._ID);
		sTermColumnMap.addColumn(TermColumns._REMOTE_ID);
		sTermColumnMap.addColumn(TermColumns._PARENT_ID);
		sTermColumnMap.addColumn(TermColumns._REMOTE_PARENT_ID);
		sTermColumnMap.addColumn(TermColumns._TAXONOMY_ID);
		sTermColumnMap.addColumn(TermColumns._REMOTE_TAXONOMY_ID);
		sTermColumnMap.addColumn(TermColumns.TITLE);
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
		case AUTHORS_PAGE:
			return AuthorColumns.CONTENT_TYPE;
		case AUTHOR_ID:
		case AUTHOR_REMOTE:
			return AuthorColumns.CONTENT_ITEM_TYPE;
		case AUTHOR_POSTS:
		case AUTHOR_POSTS_PAGE:
		case TERM_POSTS:
		case TERM_POSTS_PAGE:
		case POSTS:
		case POSTS_PAGE:
		case POSTS_STATUS:
			return PostColumns.CONTENT_TYPE;
		case POST_ID:
		case POST_REMOTE:
			return PostColumns.CONTENT_ITEM_TYPE;
		case AUTHOR_COMMENTS:
		case AUTHOR_COMMENTS_PAGE:
		case POST_COMMENTS:
		case POST_COMMENTS_PAGE:
		case COMMENTS:
		case COMMENTS_PAGE:
		case COMMENTS_STATUS:
			return CommentColumns.CONTENT_TYPE;
		case COMMENT_ID:
		case COMMENT_REMOTE:
			return CommentColumns.CONTENT_ITEM_TYPE;
		case TAXONOMIES:
		case TAXONOMIES_PAGE:
			return TaxonomyColumns.CONTENT_TYPE;
		case TAXONOMY_ID:
		case TAXONOMY_REMOTE:
			return TaxonomyColumns.CONTENT_ITEM_TYPE;
		case TAXONOMY_TERMS:
		case TAXONOMY_TERMS_PAGE:
		case POST_TERMS:
		case POST_TERMS_PAGE:
		case POST_TAXONOMY_TERMS:
		case TERMS:
		case TERMS_PAGE:
			return TermColumns.CONTENT_TYPE;
		case TERM_ID:
		case TERM_REMOTE:
			return TermColumns.CONTENT_ITEM_TYPE;
		}

		throw new IllegalArgumentException("Illegal Content URI: " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String queryLimit = null;

		ContentUri contentUri = ContentUri.match(uri);

		switch (contentUri) {
		case AUTHORS:
		case AUTHORS_PAGE:
			qb.setTables(AuthorColumns.TABLE_NAME);
			qb.setProjectionMap(sAuthorColumnMap);

			sortOrder = AuthorColumns.SORT_ORDER;

			queryLimit = getQueryLimit(AuthorColumns.PAGE_LIMIT,
					contentUri.getPage(uri));

			break;
		case AUTHOR_ID:
			qb.setTables(AuthorColumns.TABLE_NAME);
			qb.setProjectionMap(sAuthorColumnMap);

			selection = AuthorColumns._ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };

			queryLimit = "1";

			break;
		case AUTHOR_REMOTE:
			qb.setTables(AuthorColumns.TABLE_NAME);
			qb.setProjectionMap(sAuthorColumnMap);

			selection = AuthorColumns._REMOTE_ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };

			queryLimit = "1";

			break;
		case AUTHOR_POSTS:
		case AUTHOR_POSTS_PAGE:
			selection = PostColumns._AUTHOR_ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };
		case TERM_POSTS:
		case TERM_POSTS_PAGE:
			// TODO not sure yet how to handle many-to-many relation
		case POSTS:
		case POSTS_PAGE:
			qb.setTables(PostColumns.TABLE_NAME);
			qb.setProjectionMap(sPostColumnMap);

			sortOrder = PostColumns.SORT_ORDER;

			queryLimit = getQueryLimit(PostColumns.PAGE_LIMIT,
					contentUri.getPage(uri));

			break;
		case POST_ID:
			qb.setTables(PostColumns.TABLE_NAME);
			qb.setProjectionMap(sPostColumnMap);

			selection = PostColumns._ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };

			queryLimit = "1";

			break;
		case POST_REMOTE:
			qb.setTables(PostColumns.TABLE_NAME);
			qb.setProjectionMap(sPostColumnMap);

			selection = PostColumns._REMOTE_ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };

			queryLimit = "1";

			break;
		case AUTHOR_COMMENTS:
		case AUTHOR_COMMENTS_PAGE:
			// TODO this will always be overridden by the post cases
			selection = CommentColumns._AUTHOR_ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };
		case POST_COMMENTS:
		case POST_COMMENTS_PAGE:
			selection = CommentColumns._POST_ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };
		case COMMENTS:
		case COMMENTS_PAGE:
			qb.setTables(CommentColumns.TABLE_NAME);
			qb.setProjectionMap(sCommentColumnMap);

			sortOrder = CommentColumns.SORT_ORDER;

			queryLimit = getQueryLimit(CommentColumns.PAGE_LIMIT,
					contentUri.getPage(uri));

			break;
		case COMMENT_ID:
			qb.setProjectionMap(sCommentColumnMap);
			qb.setTables(CommentColumns.TABLE_NAME);

			selection = CommentColumns._ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };

			queryLimit = "1";

			break;
		case COMMENT_REMOTE:
			qb.setProjectionMap(sCommentColumnMap);
			qb.setTables(CommentColumns.TABLE_NAME);

			selection = CommentColumns._REMOTE_ID + " = ?";
			selectionArgs = new String[] { contentUri.getSlug(uri) };

			queryLimit = "1";

			break;
		case TAXONOMIES:
			qb.setProjectionMap(sTaxonomyColumnMap);
			break;
		case TAXONOMY_ID:
			qb.setProjectionMap(sTaxonomyColumnMap);
			break;
		case TERMS:
			qb.setProjectionMap(sTermColumnMap);
			break;
		case TERM_ID:
			qb.setProjectionMap(sTermColumnMap);
			break;
		default:
			throw new IllegalArgumentException("Illegal Content URI: " + uri);
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

		switch (contentUri) {
		case AUTHOR_POSTS:
			values.put(PostColumns._AUTHOR_ID, contentUri.getId(uri));
			break;
		case AUTHOR_COMMENTS:
			values.put(CommentColumns._AUTHOR_ID, contentUri.getId(uri));
			break;
		case POST_COMMENTS:
			values.put(CommentColumns._POST_ID, contentUri.getId(uri));
			break;
		default:
			// do nothing
			break;
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
			throw new IllegalArgumentException("Illegal Content URI: " + uri);
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
			tableName = AuthorColumns.TABLE_NAME;
			selection = AuthorColumns._ID + " = ?";
			break;
		case POST_ID:
			tableName = PostColumns.TABLE_NAME;
			selection = PostColumns._ID + " = ?";
			break;
		case COMMENT_ID:
			tableName = CommentColumns.TABLE_NAME;
			selection = CommentColumns._ID + " = ?";
			break;
		default:
			throw new IllegalArgumentException("Illegal Content URI: " + uri);
		}

		selectionArgs = new String[] { contentUri.getSlug(uri) };

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
			tableName = AuthorColumns.TABLE_NAME;
			selection = AuthorColumns._ID + " = ?";
			break;
		case AUTHOR_POSTS:
			tableName = PostColumns.TABLE_NAME;
			selection = PostColumns._AUTHOR_ID + " = ?";
			break;
		case AUTHOR_COMMENTS:
			tableName = CommentColumns.TABLE_NAME;
			selection = CommentColumns._AUTHOR_ID + " = ?";
			break;
		case POST_ID:
			tableName = PostColumns.TABLE_NAME;
			selection = PostColumns._ID + " = ?";
			break;
		case POST_COMMENTS:
			tableName = CommentColumns.TABLE_NAME;
			selection = CommentColumns._POST_ID + " = ?";
			break;
		case COMMENT_ID:
			tableName = CommentColumns.TABLE_NAME;
			selection = CommentColumns._ID + " = ?";
			break;
		default:
			throw new IllegalArgumentException("Illegal Content URI: " + uri);
		}

		selectionArgs = new String[] { contentUri.getSlug(uri) };

		int count = 0;

		if (tableName != null) {
			SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
			count = db.delete(tableName, selection, selectionArgs);
		}

		return count;
	}

	protected String getQueryLimit(int pageLimit, int pageOffset) {
		return pageLimit + " OFFSET " + (pageLimit  * pageOffset);
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

	@SuppressWarnings("serial")
	private static class ProjectionMap extends HashMap<String, String> {
		private String mDefaultTableName;

		public ProjectionMap(String tableName) {
			mDefaultTableName = tableName;
		}

		public void addColumn(String columnName) {
			addColumn(columnName, mDefaultTableName);
		}

		public void addColumn(String columnName, String tableName) {
			addColumn(columnName, tableName, columnName);
		}

		public void addColumn(String columnName, String tableName,
				String columnAlias) {
			put(columnAlias, tableName + "." + columnName);
		}
	}
}