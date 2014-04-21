package com.droidpress.content;

import java.util.Arrays;
import java.util.List;

import android.content.UriMatcher;
import android.net.Uri;

import com.droidpress.content.ContentObject.UriBuilder;
import com.droidpress.content.ContentProvider.SqlSelector;

public enum ContentUri implements ContentSchema, UriBuilder, SqlSelector {
	AUTHORS         (AuthorColumns.TABLE_NAME),
	AUTHOR_ID       (AuthorColumns.TABLE_NAME + "/#", 1),
	AUTHOR_POSTS    (AuthorColumns.TABLE_NAME + "/#/" + PostColumns.TABLE_NAME, 1, PostColumns._AUTHOR_ID),
	AUTHOR_COMMENTS (AuthorColumns.TABLE_NAME + "/#/" + CommentColumns.TABLE_NAME, 1, CommentColumns._AUTHOR_ID),

	POSTS         (PostColumns.TABLE_NAME),
	POST_ID       (PostColumns.TABLE_NAME + "/#", 1),
	POST_COMMENTS (PostColumns.TABLE_NAME + "/#/" + CommentColumns.TABLE_NAME, 1, CommentColumns._POST_ID),

	COMMENTS   (CommentColumns.TABLE_NAME),
	COMMENT_ID (CommentColumns.TABLE_NAME + "/#", 1);

	private static final Uri sBaseUri;

	private static final UriMatcher sUriMatcher;

	static {
		sBaseUri = Uri.parse(String.format("%s://%s/", SCHEME, AUTHORITY));

		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	}

	public static ContentUri match(Uri uri) {
		int match = sUriMatcher.match(uri);
		if (match == UriMatcher.NO_MATCH)
			return null;

		ContentUri matchUri = values()[match];

		return matchUri;
	}

	public final String path;
	public final int idPathPosition;
	public final String foreignKey;

	private ContentUri(String path) {
		this(path, -1);
	}

	private ContentUri(String path, int idPathPosition) {
		this(path, idPathPosition, null);
	}

	private ContentUri(String path, int idPathPosition, String foreignKey) {
		this.path = path;
		this.idPathPosition = idPathPosition;
		this.foreignKey = foreignKey;

		addToMatcher();
	}

	private void addToMatcher() {
		sUriMatcher.addURI(AUTHORITY, path, ordinal());
	}

	private List<String> getPathSegments() {
		return Arrays.asList(path.split("\\/"));
	}

	@Override
	public Uri build() {
		return sBaseUri.buildUpon().appendEncodedPath(path).build();
	}

	@Override
	public Uri build(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri build(String slug) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		List<String> segs = getPathSegments();

		if (idPathPosition >= 0 && foreignKey != null)
			return segs.get(idPathPosition + 1);
		else
			return segs.get(0);
	}

	@Override
	public String getSelection() {
		if (idPathPosition < 0)
			return null;

		String field = foreignKey == null ? BaseColumns._ID : foreignKey;

		return getTableName() + "." + field + " = ?";
	}

	@Override
	public String[] getSelectionArgs(Uri uri) {
		if (idPathPosition < 0)
			return null;

		return new String[] { uri.getPathSegments().get(idPathPosition) };
	}
}