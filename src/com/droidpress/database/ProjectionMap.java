package com.droidpress.database;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ProjectionMap extends HashMap<String, String> {
	private String mMainTableName;

	public ProjectionMap(String tableName) {
		mMainTableName = tableName;
	}

	public void addColumn(String columnName) {
		addColumn(columnName, mMainTableName);
	}

	public void addColumn(String columnName, String tableName) {
		addColumn(columnName, tableName, columnName);
	}

	public void addColumn(String columnName, String tableName,
			String columnAlias) {
		put(columnAlias, tableName + "." + columnName);
	}
}