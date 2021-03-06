package com.droidpress.content;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public abstract class ContentObject implements ContentSchema.BaseColumns {
	private static final String TAG = "ContentObject";

	public static final int LOAD_REPLACE = 0x01;
	public static final int LOAD_MERGE   = 0x02;

	private static final Executor sExecutor;
	private static final Handler  sHandler;

	/**
	 * May be used as baseline in child classes
	 */
	protected static final Map<String, FieldType> sFieldTypeMap;

	static {
		sExecutor = Executors.newCachedThreadPool();
		sHandler  = new Handler(Looper.getMainLooper());

		sFieldTypeMap = new HashMap<String, FieldType>();
		sFieldTypeMap.put(_ID, FieldType.LONG);
	}

	protected static void runOnMainThread(Runnable action) {
		if (action != null)
			sHandler.post(action);
	}

	protected static void runInBackground(Runnable action) {
		if (action != null)
			sExecutor.execute(action);
	}

	public static final class Factory<T extends ContentObject> {
		private static final String TAG = "ContentObject.Factory";

		private Context  mContext;
		private Class<T> mContentClass;

		public Factory(Context context, Class<T> contentClass) {
			mContext      = context;
			mContentClass = contentClass;
		}

		public T newInstance(ContentValues values) {
			return newInstance(values, ContentValues.class);
		}

		public T newInstance(Cursor cursor) {
			return newInstance(cursor, Cursor.class);
		}

		private T newInstance(Object source, Class<?> type) {
			try {
				Constructor<T> constructor = mContentClass
						.getConstructor(Context.class, type);

				return constructor.newInstance(mContext, source);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "IllegalArgumentException", e);
			} catch (InstantiationException e) {
				Log.e(TAG, "InstantiationException", e);
			} catch (IllegalAccessException e) {
				Log.e(TAG, "IllegalAccessException", e);
			} catch (InvocationTargetException e) {
				Log.e(TAG, "InvocationTargetException", e);
			} catch (NoSuchMethodException e) {
				Log.e(TAG, "NoSuchMethodException", e);
			}

			return null;
		}
	}

	public static enum FieldType {
		NULL,
		BOOLEAN,
		BYTE,
		SHORT,
		INTEGER,
		LONG,
		FLOAT,
		DOUBLE,
		STRING,
		DATE,
		URI;
	}

	public interface UriBuilder {
		public Uri build();
		public Uri build(long id);
		public Uri build(long id, int page);
		public Uri build(String slug);
	}

	private Context mContext;
	private ContentValues mValues;

	public ContentObject(Context context) {
		mContext = context;
		mValues  = new ContentValues();
	}

	public ContentObject(Context context, Cursor cursor) {
		this(context);
		loadCursor(cursor);
	}

	public ContentObject(Context context, ContentValues values) {
		this(context);
		loadValues(values);
	}

	public ContentObject loadCursor(Cursor cursor) {
		return loadCursor(cursor, LOAD_REPLACE);
	}

	public ContentObject loadCursor(Cursor cursor, int mode) {
		if (cursor != null && !cursor.isClosed()) {
			if (mode == LOAD_REPLACE)
				mValues.clear();

			String fieldName;

			for (int i = 0; i < cursor.getColumnCount(); i++) {
				fieldName = cursor.getColumnName(i);

				switch (getFieldType(fieldName)) {
				case BOOLEAN:
					mValues.put(fieldName, cursor.getInt(i) != 0);
					break;
				case BYTE:
					mValues.put(fieldName, (byte) (cursor.getInt(i) & 0xFF));
					break;
				case SHORT:
					mValues.put(fieldName, cursor.getShort(i));
					break;
				case INTEGER:
					mValues.put(fieldName, cursor.getInt(i));
					break;
				case LONG:
				case DATE:
					mValues.put(fieldName, cursor.getLong(i));
					break;
				case FLOAT:
					mValues.put(fieldName, cursor.getFloat(i));
					break;
				case DOUBLE:
					mValues.put(fieldName, cursor.getDouble(i));
					break;
				case STRING:
				case URI:
					mValues.put(fieldName, cursor.getString(i));
					break;
				case NULL:
				default:
					mValues.putNull(fieldName);
					break;
				}
			}
		}

		return this;
	}

	public ContentObject loadValues(ContentValues values) {
		return loadValues(values, LOAD_MERGE);
	}

	public ContentObject loadValues(ContentValues values, int mode) {
		if (mode == LOAD_REPLACE)
			mValues.clear();

		mValues.putAll(values);

		return this;
	}

	public Context getContext() {
		return mContext;
	}

	public ContentValues getValues() {
		return new ContentValues(mValues);
	}

	@Override
	public String toString() {
		return mValues.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;

		if (getId() == 0)
			return false;

		if (!getClass().equals(object.getClass()))
			return false;

		return getId() == ((ContentObject) object).getId();
	}

	/**
	 * Override in child classes to add extra fields
	 * 
	 * @return Map of field names and types
	 */
	protected Map<String, FieldType> getFieldTypeMap() {
		return sFieldTypeMap;
	}

	protected FieldType getFieldType(String fieldName) {
		Map<String, FieldType> fieldTypeMap = getFieldTypeMap();

		if (fieldTypeMap.containsKey(fieldName))
			return fieldTypeMap.get(fieldName);

		return FieldType.NULL;
	}

	public Uri save() {
		beforeSave();

		if (getId() > 0)
			update();
		else
			insert();

		afterSave();

		return getItemUri();
	}

	public Uri insert() {
		if (Thread.currentThread().getId() == 1)
			Log.w(TAG, "Inserting on main thread.");

		beforeInsert();

		Uri uri = mContext.getContentResolver()
				.insert(getInsertUri(), getValues());

		afterInsert();

		put(_ID, Long.parseLong(uri.getPathSegments().get(1)));

		return uri;
	}

	public int update() {
		if (Thread.currentThread().getId() == 1)
			Log.w(TAG, "Updating on main thread.");

		beforeUpdate();

		int count = mContext.getContentResolver()
				.update(getItemUri(), getValues(), null, null);

		afterUpdate();

		return count;
	}

	public int delete() {
		if (Thread.currentThread().getId() == 1)
			Log.w(TAG, "Deleting on main thread.");

		beforeDelete();

		int count = mContext.getContentResolver()
				.delete(getItemUri(), null, null);

		put(_ID, 0);

		afterDelete();

		return count;
	}

	public void save(final Runnable callback) {
		runInBackground(new Runnable() {
			@Override
			public void run() {
				save();
				runOnMainThread(callback);
			}
		});
	}

	public void delete(final Runnable callback) {
		runInBackground(new Runnable() {
			@Override
			public void run() {
				delete();
				runOnMainThread(callback);
			}
		});
	}

	protected void beforeSave() {}
	protected void afterSave() {}

	protected void beforeInsert() {}
	protected void afterInsert() {}

	protected void beforeUpdate() {}
	protected void afterUpdate() {}

	protected void beforeDelete() {}
	protected void afterDelete() {}

	protected abstract UriBuilder getUriBuilder();
	protected abstract UriBuilder getItemUriBuilder();

	public Uri getUri() {
		return getUriBuilder().build();
	}

	public Uri getItemUri() {
		return getItemUriBuilder().build(getId());
	}

	/**
	 * Defaults to base URI. Child classes may override with nested URI.
	 * 
	 * @return URI to be used with INSERT action on ContentProvider.
	 */
	public Uri getInsertUri() {
		return getUri();
	}


	@Override
	public long getId() {
		return getLong(_ID);
	}

	@Override
	public void setId(long id) {
		put(_ID, id);
	}


	/* Low-level content accessors */

	protected boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	protected boolean getBoolean(String key, boolean defValue) {
		return mValues.containsKey(key) ? mValues.getAsBoolean(key) : defValue;
	}

	protected byte getByte(String key) {
		return getByte(key, (byte) 0);
	}

	protected byte getByte(String key, byte defValue) {
		return mValues.containsKey(key) ? mValues.getAsByte(key) : defValue;
	}

	protected short getShort(String key) {
		return getShort(key, (short) 0);
	}

	protected short getShort(String key, short defValue) {
		return mValues.containsKey(key) ? mValues.getAsShort(key) : defValue;
	}

	protected int getInteger(String key) {
		return getInteger(key, 0);
	}

	protected int getInteger(String key, int defValue) {
		return mValues.containsKey(key) ? mValues.getAsInteger(key) : defValue;
	}

	protected long getLong(String key) {
		return getLong(key, 0);
	}

	protected long getLong(String key, long defValue) {
		return mValues.containsKey(key) ? mValues.getAsLong(key) : defValue;
	}

	protected float getFloat(String key) {
		return getFloat(key, 0);
	}

	protected float getFloat(String key, float defValue) {
		return mValues.containsKey(key) ? mValues.getAsFloat(key) : defValue;
	}

	protected double getDouble(String key) {
		return getDouble(key, 0);
	}

	protected double getDouble(String key, double defValue) {
		return mValues.containsKey(key) ? mValues.getAsDouble(key) : defValue;
	}

	protected String getString(String key) {
		return getString(key, null);
	}

	protected String getString(String key, String defValue) {
		return mValues.containsKey(key) ? mValues.getAsString(key) : defValue;
	}

	protected Date getDate(String key) {
		return getDate(key, null);
	}

	protected Date getDate(String key, Date defValue) {
		return mValues.containsKey(key) ? new Date(getLong(key)) : defValue;
	}

	protected Uri getUri(String key) {
		return getUri(key, null);
	}

	protected Uri getUri(String key, Uri defValue) {
		return mValues.containsKey(key) ? Uri.parse(getString(key)) : defValue;
	}

	/* End low-level content accessors */


	/* Low-level content mutators */

	protected ContentObject put(String key, boolean value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, byte value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, short value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, int value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, long value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, float value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, double value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, String value) {
		mValues.put(key, value);
		return this;
	}

	protected ContentObject put(String key, Date value) {
		put(key, value.getTime());
		return this;
	}

	protected ContentObject put(String key, Uri value) {
		put(key, value.toString());
		return this;
	}

	/* End low-level content mutators */
}