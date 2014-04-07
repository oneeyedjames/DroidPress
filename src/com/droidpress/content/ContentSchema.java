package com.droidpress.content;

import java.util.Date;

import android.net.Uri;

public interface ContentSchema {
	public static final String SCHEME = "content";

	public interface BaseColumns extends android.provider.BaseColumns {
		public static final String PATH_SEGMENT_CURRENT = "current";
		public static final String PATH_SEGMENT_PREV    = "prev";
		public static final String PATH_SEGMENT_NEXT    = "next";
	}

	public interface RemoteColumns extends BaseColumns {
		public static final String PATH_SEGMENT_REMOTE = "remote";

		public static final String _REMOTE_ID = "_remote_id";

		public long getId();
		public void setId(long id);

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
		public static final String PATH_SEGMENT_PENDING = "pending";

		public static final String STATUS_PENDING = "pending";

		public static final String MOD_DATE = "modDate";

		public Date getModDate();
		public void setModDate(Date modDate);
	}

	public interface PostColumns extends AuthoredColumns, EditableColumns {
		public static final String COMMENT_STATUS        = "commentStatus";
		public static final String COMMENT_STATUS_OPEN   = "open";
		public static final String COMMENT_STATUS_CLOSED = "closed";

		public static final String TITLE = "title";

		public static final String EXCERPT = "excerpt";
		public static final String CONTENT = "content";

		public static final String PERMALINK = "permalink";

		public String getCommentStatus();
		public void setCommentStatus(String commentStatus);

		public String getTitle();
		public void setTitle(String title);

		public String getExcerpt();
		public void setExcerpt(String excerpt);

		public String getContent();
		public void setContent(String content);

		public Uri getPermalink();
		public void setPermalink(Uri permalink);
	}

	public interface PageColumns extends PostColumns, NestedColumns {

	}

	public interface AuthorColumns extends RemoteColumns {
		public static final String NAME  = "name";
		public static final String EMAIL = "email";
		public static final String URL   = "url";

		public String getName();
		public void setName(String name);

		public String getEmail();
		public void setEmail(String email);

		public Uri getUrl();
		public void setUrl(Uri url);
	}

	public interface CommentColumns extends AuthoredColumns, EditableColumns, NestedColumns {
		public static final String STATUS_APPROVED   = "approved";
		public static final String STATUS_UNAPPROVED = "unapproved";

		public static final String _POST_ID = "_post_id";
		public static final String _REMOTE_POST_ID = "_remote_post_id";

		public static final String CONTENT = "content";

		public long getPostId();
		public void setPostId(long postId);

		public long getRemotePostId();
		public void setRemotePostId(long remotePostId);

		public String getContent();
		public void setContent(String content);
	}
}