package com.droidpress.content;

import android.content.UriMatcher;
import android.net.Uri;

import com.droidpress.content.ContentObject.UriBuilder;

public enum ContentUri implements ContentSchema, UriBuilder {
	AUTHORS      (AuthorColumns.CONTENT_URI),
	AUTHORS_PAGE (AuthorColumns.CONTENT_PAGE_URI, -1, 2),

	AUTHOR_ID     (AuthorColumns.CONTENT_ITEM_URI, 1),
	AUTHOR_REMOTE (AuthorColumns.CONTENT_REMOTE_URI, 2),

	AUTHOR_POSTS      (AuthorColumns.CONTENT_ITEM_URI + PostColumns.CONTENT_URI, 1),
	AUTHOR_POSTS_PAGE (AuthorColumns.CONTENT_ITEM_URI + PostColumns.CONTENT_PAGE_URI, 1, 4),

	AUTHOR_COMMENTS      (AuthorColumns.CONTENT_ITEM_URI + CommentColumns.CONTENT_URI, 1),
	AUTHOR_COMMENTS_PAGE (AuthorColumns.CONTENT_ITEM_URI + CommentColumns.CONTENT_PAGE_URI, 1, 4),

	POSTS        (PostColumns.CONTENT_URI),
	POSTS_PAGE   (PostColumns.CONTENT_PAGE_URI, -1, 2),
	POSTS_STATUS (PostColumns.CONTENT_STATUS_URI, 2),

	POST_ID     (PostColumns.CONTENT_ITEM_URI, 1),
	POST_REMOTE (PostColumns.CONTENT_REMOTE_URI, 2),

	POST_COMMENTS      (PostColumns.CONTENT_ITEM_URI + CommentColumns.CONTENT_URI, 1),
	POST_COMMENTS_PAGE (PostColumns.CONTENT_ITEM_URI + CommentColumns.CONTENT_PAGE_URI, 1, 4),

	POST_TERMS      (PostColumns.CONTENT_ITEM_URI + TermColumns.CONTENT_URI, 1),
	POST_TERMS_PAGE (PostColumns.CONTENT_ITEM_URI + TermColumns.CONTENT_PAGE_URI, 1, 4),

	POST_TAXONOMY_TERMS (PostColumns.CONTENT_ITEM_URI + TaxonomyColumns.CONTENT_ITEM_URI + TermColumns.CONTENT_URI, 1),

	COMMENTS        (CommentColumns.CONTENT_URI),
	COMMENTS_PAGE   (CommentColumns.CONTENT_PAGE_URI, -1, 2),
	COMMENTS_STATUS (CommentColumns.CONTENT_STATUS_URI, 2),

	COMMENT_ID     (CommentColumns.CONTENT_ITEM_URI, 1),
	COMMENT_REMOTE (CommentColumns.CONTENT_REMOTE_URI, 2),

	TAXONOMIES      (TaxonomyColumns.CONTENT_URI),
	TAXONOMIES_PAGE (TaxonomyColumns.CONTENT_PAGE_URI, -1, 2),

	TAXONOMY_ID     (TaxonomyColumns.CONTENT_ITEM_URI, 1),
	TAXONOMY_REMOTE (TaxonomyColumns.CONTENT_REMOTE_URI, 2),

	TAXONOMY_TERMS      (TaxonomyColumns.CONTENT_ITEM_URI + TermColumns.CONTENT_URI, 1),
	TAXONOMY_TERMS_PAGE (TaxonomyColumns.CONTENT_ITEM_URI + TermColumns.CONTENT_PAGE_URI, 1, 4),

	TERMS      (TermColumns.CONTENT_URI),
	TERMS_PAGE (TermColumns.CONTENT_PAGE_URI, -1, 2),

	TERM_ID     (TermColumns.CONTENT_ITEM_URI, 1),
	TERM_REMOTE (TermColumns.CONTENT_REMOTE_URI, 2),

	TERM_POSTS      (TermColumns.CONTENT_ITEM_URI + PostColumns.CONTENT_URI, 1),
	TERM_POSTS_PAGE (TermColumns.CONTENT_ITEM_URI + PostColumns.CONTENT_PAGE_URI, 1, 4);

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
	public final int keyPathPosition;
	public final int pagePathPosition;

	private ContentUri(String path) {
		this(path, -1);
	}

	private ContentUri(String path, int keyPathPosition) {
		this(path, keyPathPosition, -1);
	}

	private ContentUri(String path, int keyPathPosition, int pagePathPosition) {
		this.path = path;
		this.keyPathPosition  = keyPathPosition;
		this.pagePathPosition = pagePathPosition;

		addToMatcher();
	}

	private void addToMatcher() {
		sUriMatcher.addURI(AUTHORITY, path, ordinal());
	}

	public boolean matches(Uri uri) {
		return sUriMatcher.match(uri) == ordinal();
	}

	public long getId(Uri uri) {
		return Long.parseLong(getSlug(uri));
	}

	public String getSlug(Uri uri) {
		if (!matches(uri) || keyPathPosition < 0)
			return null;

		return uri.getPathSegments().get(keyPathPosition);
	}

	public int getPage(Uri uri) {
		if (!matches(uri) || pagePathPosition < 0)
			return 0;

		return Integer.parseInt(uri.getPathSegments().get(pagePathPosition));
	}

	@Override
	public Uri build() {
		return sBaseUri.buildUpon().appendEncodedPath(path).build();
	}

	@Override
	public Uri build(long id) {
		String path = this.path.replaceFirst("#", id + "");
		return sBaseUri.buildUpon().appendEncodedPath(path).build();
	}

	@Override
	public Uri build(String slug) {
		String path = this.path.replaceFirst("*", slug);
		return sBaseUri.buildUpon().appendEncodedPath(path).build();
	}
}