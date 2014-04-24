package com.droidpress.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

public class QueryBuilder extends SQLiteQueryBuilder {
	private String   mSelection = null;
	private String[] mSelectionArgs = null;

	private String mSortOrder = null;
	private String mQueryLimit = null;

	private int mPageLimit = 0;
	private int mPageOffset = 0;

	public QueryBuilder() {
		super();
	}

	public boolean hasSelection() {
		return !TextUtils.isEmpty(mSelection);
	}

	public boolean hasSelectionArgs() {
		return mSelectionArgs != null && mSelectionArgs.length > 0;
	}

	public boolean hasSortOrder() {
		return !TextUtils.isEmpty(mSortOrder);
	}

	public boolean hasQueryLimit() {
		return !TextUtils.isEmpty(mQueryLimit);
	}

	public Cursor query(SQLiteDatabase db) {
		if (mPageLimit > 0 && !hasQueryLimit()) {
			if (mPageOffset > 0)
				setQueryLimit(mPageLimit, mPageOffset);
			else
				setQueryLimit(mPageLimit);
		}

		return query(db, null, mSelection, mSelectionArgs, null, null,
				mSortOrder, mQueryLimit);
	}

	public void setSelection(String selection) {
		mSelection = selection;
	}

	public void setSelectionArgs(String... selectionArgs) {
		mSelectionArgs = selectionArgs;
	}

	public void setSortOrder(String sortOrder) {
		mSortOrder = sortOrder;
	}

	public void setSortOrder(String sortOrder, String defaultSortOrder) {
		mSortOrder = !TextUtils.isEmpty(sortOrder) ? sortOrder : defaultSortOrder;
	}

	public void setQueryLimit(int pageLimit) {
		mQueryLimit = pageLimit + "";
	}

	public void setQueryLimit(int pageLimit, int pageOffset) {
		mQueryLimit = pageLimit + " OFFSET " + (pageLimit * pageOffset);
	}

	public void setPageLimit(int pageLimit) {
		mPageLimit = pageLimit;
	}

	public void setPageOffset(int pageOffset) {
		mPageOffset = pageOffset;
	}
}