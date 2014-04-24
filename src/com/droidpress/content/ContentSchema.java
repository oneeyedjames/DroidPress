package com.droidpress.content;

import java.util.Date;

import android.net.Uri;

public interface ContentSchema {
	public static final String SCHEME = "content";
	public static final String AUTHORITY = "com.droidpress.content.contentprovider";

	public interface BaseColumns extends android.provider.BaseColumns {
		public static final String CURRENT = "current";
		public static final String PREV    = "prev";
		public static final String NEXT    = "next";
		public static final String PAGE    = "page";

		public long getId();
		public void setId(long id);
	}

	public interface RemoteColumns extends BaseColumns {
		public static final String REMOTE = "remote";

		public static final String _REMOTE_ID = "_remote_id";

		public long getRemoteId();
		public void setRemoteId(long remoteId);
	}

	public interface NestedColumns extends RemoteColumns {
		public static final String _PARENT_ID = "_parent_id";
		public static final String _REMOTE_PARENT_ID = "_remote_parent_id";

		public long getParentId();
		public void setParentId(long parentId);

		public long getRemoteParentId();
		public void setRemoteParentId(long remoteParentId);
	}

	public interface AuthoredColumns extends RemoteColumns {
		public static final String _AUTHOR_ID = "_author_id";
		public static final String _REMOTE_AUTHOR_ID = "_remote_author_id";

		public long getAuthorId();
		public void setAuthorId(long authorId);

		public long getRemoteAuthorId();
		public void setRemoteAuthorId(long remoteAuthorId);
	}

	public interface ReadableColumns extends RemoteColumns {
		public static final String STATUS         = "status";
		public static final String STATUS_PUBLISH = "publish";
		public static final String STATUS_TRASH   = "trash";

		public static final String PUB_DATE = "pubDate";

		public String getStatus();
		public void setStatus(String status);

		public Date getPubDate();
		public void setPubDate(Date pubDate);
	}

	public interface EditableColumns extends ReadableColumns {
		public static final String STATUS_PENDING = "pending";

		public static final String MOD_DATE = "modDate";

		public Date getModDate();
		public void setModDate(Date modDate);
	}

	public interface AuthorColumns extends RemoteColumns {
		public static final String TABLE_NAME = "authors";

		public static final String NAME  = "name";
		public static final String EMAIL = "email";
		public static final String URL   = "url";

		public static final String SORT_ORDER = NAME + " ASC";

		public static final int PAGE_LIMIT = 10;

		public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + " BIGINT PRIMARY KEY AUTOINCREMENT, "
				+ _REMOTE_ID + " BIGINT, "
				+ NAME + " TEXT, "
				+ EMAIL + " TEXT, "
				+ URL + "TEXT);";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.droidpress.author";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.droidpress.author";

		public static final String CONTENT_URI        = "/" + TABLE_NAME;
		public static final String CONTENT_PAGE_URI   = "/" + TABLE_NAME + "/" + PAGE + "/#";
		public static final String CONTENT_ITEM_URI   = "/" + TABLE_NAME + "/#";
		public static final String CONTENT_REMOTE_URI = "/" + TABLE_NAME + "/" + REMOTE + "/#";

		public String getName();
		public void setName(String name);

		public String getEmail();
		public void setEmail(String email);

		public Uri getUrl();
		public void setUrl(Uri url);
	}

	public interface PostColumns extends AuthoredColumns, NestedColumns, EditableColumns {
		public static final String TABLE_NAME = "posts";

		public static final String TYPE = "type";
		public static final String TYPE_DEFAULT = "post";

		public static final String TITLE = "title";

		public static final String EXCERPT = "excerpt";
		public static final String CONTENT = "content";

		public static final String PERMALINK = "permalink";

		public static final String COMMENT_STATUS        = "commentStatus";
		public static final String COMMENT_STATUS_OPEN   = "open";
		public static final String COMMENT_STATUS_CLOSED = "closed";

		public static final String SORT_ORDER = PUB_DATE + " DESC";

		public static final int PAGE_LIMIT = 10;

		public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + " BIGINT PRIMARY KEY AUTOINCREMENT, "
				+ _REMOTE_ID + " BIGINT, "
				+ _PARENT_ID + " BIGINT, "
				+ _REMOTE_PARENT_ID + " BIGINT, "
				+ _AUTHOR_ID + " BIGINT, "
				+ _REMOTE_AUTHOR_ID + " BIGINT, "
				+ TYPE + " TEXT, "
				+ TITLE + " TEXT, "
				+ EXCERPT + " TEXT, "
				+ CONTENT + " TEXT, "
				+ PERMALINK + " TEXT, "
				+ STATUS + " TEXT, "
				+ COMMENT_STATUS + " TEXT, "
				+ PUB_DATE + " BIGINT, "
				+ MOD_DATE + " BIGINT);";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.droidpress.post";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.droidpress.post";

		public static final String CONTENT_URI        = "/" + TABLE_NAME;
		public static final String CONTENT_PAGE_URI   = "/" + TABLE_NAME + "/" + PAGE + "/#";
		public static final String CONTENT_ITEM_URI   = "/" + TABLE_NAME + "/#";
		public static final String CONTENT_REMOTE_URI = "/" + TABLE_NAME + "/" + REMOTE + "/#";
		public static final String CONTENT_STATUS_URI = "/" + TABLE_NAME + "/" + STATUS + "/*";

		public String getType();
		public void setType(String type);

		public String getTitle();
		public void setTitle(String title);

		public String getExcerpt();
		public void setExcerpt(String excerpt);

		public String getContent();
		public void setContent(String content);

		public Uri getPermalink();
		public void setPermalink(Uri permalink);

		public String getCommentStatus();
		public void setCommentStatus(String commentStatus);
	}

	public interface CommentColumns extends AuthoredColumns, NestedColumns, EditableColumns {
		public static final String TABLE_NAME = "comments";

		public static final String STATUS_APPROVED   = "approved";
		public static final String STATUS_UNAPPROVED = "unapproved";

		public static final String _POST_ID = "_post_id";
		public static final String _REMOTE_POST_ID = "_remote_post_id";

		public static final String CONTENT = "content";

		public static final String SORT_ORDER = PUB_DATE + " DESC";

		public static final int PAGE_LIMIT = 25;

		public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + " BIGINT PRIMARY KEY AUTOINCREMENT, "
				+ _REMOTE_ID + " BIGINT, "
				+ _PARENT_ID + " BIGINT, "
				+ _REMOTE_PARENT_ID + " BIGINT, "
				+ _AUTHOR_ID + " BIGINT, "
				+ _REMOTE_AUTHOR_ID + " BIGINT, "
				+ CONTENT + " TEXT, "
				+ STATUS + " TEXT, "
				+ PUB_DATE + " BIGINT, "
				+ MOD_DATE + " BIGINT);";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.droidpress.comment";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.droidpress.comment";

		public static final String CONTENT_URI        = "/" + TABLE_NAME;
		public static final String CONTENT_PAGE_URI   = "/" + TABLE_NAME + "/" + PAGE + "/#";
		public static final String CONTENT_ITEM_URI   = "/" + TABLE_NAME + "/#";
		public static final String CONTENT_REMOTE_URI = "/" + TABLE_NAME + "/" + REMOTE + "/#";
		public static final String CONTENT_STATUS_URI = "/" + TABLE_NAME + "/" + STATUS + "/*";

		public long getPostId();
		public void setPostId(long postId);

		public long getRemotePostId();
		public void setRemotePostId(long remotePostId);

		public String getContent();
		public void setContent(String content);
	}

	public interface TaxonomyColumns extends RemoteColumns {
		public static final String TABLE_NAME = "taxonomies";

		public static final String TITLE = "title";

		public static final String HIERARCHICAL = "hierarchical";

		public static final String SORT_ORDER = TITLE + " ASC";

		public static final int PAGE_LIMIT = 25;

		public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + " BIGINT PRIMARY KEY AUTOINCREMENT, "
				+ _REMOTE_ID + " BIGINT, "
				+ TITLE + " TEXT);";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.droidpress.taxonomy";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.droidpress.taxonomy";

		public static final String CONTENT_URI        = "/" + TABLE_NAME;
		public static final String CONTENT_PAGE_URI   = "/" + TABLE_NAME + "/" + PAGE + "/#";
		public static final String CONTENT_ITEM_URI   = "/" + TABLE_NAME + "/#";
		public static final String CONTENT_REMOTE_URI = "/" + TABLE_NAME + "/" + REMOTE + "/#";

		public String getTitle();
		public void setTitle(String title);

		public boolean isHierarchical();
		public void setHierarchical(boolean hierarchical);
	}

	public interface TermColumns extends NestedColumns {
		public static final String TABLE_NAME = "terms";

		public static final String _TAXONOMY_ID = "_taxonomy_id";
		public static final String _REMOTE_TAXONOMY_ID = "_remote_taxonomy_id";

		public static final String TITLE = "title";

		public static final String SORT_ORDER = TITLE + " ASC";

		public static final int PAGE_LIMIT = 25;

		public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + " BIGINT PRIMARY KEY AUTOINCREMENT, "
				+ _REMOTE_ID + " BIGINT, "
				+ _PARENT_ID + " BIGINT, "
				+ _REMOTE_PARENT_ID + " BIGINT, "
				+ TITLE + " TEXT);";

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.droidpress.term";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.droidpress.term";

		public static final String CONTENT_URI        = "/" + TABLE_NAME;
		public static final String CONTENT_PAGE_URI   = "/" + TABLE_NAME + "/" + PAGE + "/#";
		public static final String CONTENT_ITEM_URI   = "/" + TABLE_NAME + "/#";
		public static final String CONTENT_REMOTE_URI = "/" + TABLE_NAME + "/" + REMOTE + "/#";

		public long getTaxonomyId();
		public void setTaxonomyId(long taxonomyId);

		public long getRemoteTaxonomyId();
		public void setRemoteTaxonomyId(long remoteTaxonomyId);

		public String getTitle();
		public void setTitle(String title);
	}

	public interface PostTermColumns {
		public static final String TABLE_NAME = "postTerms";

		public static final String _POST_ID = "_post_id";
		public static final String _TERM_ID = "_term_id";

		public static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
				+ _POST_ID + " BIGINT, "
				+ _TERM_ID + " BIGINT);";
	}
}