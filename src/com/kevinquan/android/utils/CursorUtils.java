/*
 * Copyright 2013 Kevin Quan (kevin.quan@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevinquan.android.utils;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

/**
 * Utilities to enable safe handling of {@link Cursor} objects
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class CursorUtils {

    /**
     * Simple structure to hold details about the index to create
     * @author Kevin Quan (kevin.quan@gmail.com)
     *
     */
    public static class IndexInfo {
        protected String mTable;
        protected String mColumn;
        
        public IndexInfo(String table, String column) {
            this.mTable = table;
            this.mColumn = column;
        }
        
        public String getTable() {
            return mTable;
        }

        public String getColumn() {
            return mColumn;
        }
        
        public String getDefaultIndexName() {
            return "idx_"+mTable+"_"+mColumn;
        }
        
        public String getDefaultCreateQuery() {
            return "CREATE INDEX "+getDefaultIndexName()+" ON "+mTable+"("+mColumn+")";
        }

    }
    
    private static final String TAG = CursorUtils.class.getSimpleName();
    
    public static final long VALUE_NO_ID = -1;
    public static final int VALUE_FALSE = 0;
    
    public static final int VALUE_TRUE = 1;
    
    /**
     * Convenience method to add an index to a table following a specific pattern.  The index will be named idx_[table name]_[column name] 
     * @param database The database to create the index
     * @param index details about the index.
     */
    public static void addIndex(SQLiteDatabase database, List<IndexInfo> index) {
        if (database == null || index == null) return;
        for (IndexInfo info : index) {
            String indexQuery = "CREATE INDEX idx_"+info.mTable+"_"+info.mColumn+" ON "+info.mTable+"("+info.mColumn+")";
            Log.d(TAG, "Adding index: "+indexQuery);
            database.execSQL(indexQuery);
        }
    }

    /**
     * Check whether the column name exists in the result set.
     * @param result The result set to check
     * @param columnName The column name to check
     * @return true if the column exists in the result set
     */
    public static boolean hasColumn(Cursor result, String columnName) {
        if (result == null || TextUtils.isEmpty(columnName)) return false;
        int columnIndex = result.getColumnIndex(columnName);
        return columnIndex != -1;
    }
    
    /**
     * Safely close a cursor
     * @param cursorToClose The cursor to close
     */
    public static void safeClose(Cursor cursorToClose) {
        if (cursorToClose != null) {
            cursorToClose.close();
        }
    }
    
    /**
     * Retrieve a double from the provided column in the provided result set.
     * @param result The result set to retrieve from
     * @param columnName The column to retrieve
     * @return The actual double, or the default value if the column doesn't exist.
     */
    public static double safeGetDouble(Cursor result, String columnName, double defaultValue) {
        if (result == null || TextUtils.isEmpty(columnName)) return defaultValue;
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1 || result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getDouble(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve double value for "+columnName,e);
            return defaultValue;
        }
    }
    
    /**
     * Retrieve a float from the provided column in the provided result set.
     * @param result The result set to retrieve from
     * @param columnName The column to retrieve
     * @return The actual float, or the default value if the column doesn't exist.
     */
    public static float safeGetFloat(Cursor result, String columnName, float defaultValue) {
        if (result == null || TextUtils.isEmpty(columnName)) return defaultValue;
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1 || result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getFloat(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve float value for "+columnName,e);
            return defaultValue;
        }
    }

    /**
     * Retrieve an integer from the provided column in the provided result set.
     * @param result The result set to retrieve from
     * @param columnName The column to retrieve
     * @return The actual integer, or the default value if the column doesn't exist.
     */
    public static int safeGetInt(Cursor result, String columnName, int defaultValue) {
        if (result == null || TextUtils.isEmpty(columnName)) return defaultValue;
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1 || result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getInt(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve integar value for "+columnName,e);
            return defaultValue;
        }
    }

    /**
     * Retrieve a boolean from the provided column in the provided result set.  It is expected that the 
     * provider models boolean using the values provided in {@link CursorUtils}
     * @param result The result set to retrieve from
     * @param columnName The column to retrieve
     * @return The actual boolean, or null if the column doesn't exist.
     */
    public static boolean safeGetIntBackedBoolean(Cursor result, String columnName, boolean defaultValue) {
        if (result == null || TextUtils.isEmpty(columnName)) return defaultValue;
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1 || result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getInt(columnIndex) == VALUE_TRUE;
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve boolean (int backed) value for "+columnName,e);
            return defaultValue;
        }
    }
    
    /**
     * Retrieve a long from the provided column in the provided result set.
     * @param result The result set to retrieve from
     * @param columnName The column to retrieve
     * @return The actual long, or the default value if the column doesn't exist.
     */
    public static long safeGetLong(Cursor result, String columnName, long defaultValue) {
        if (result == null || TextUtils.isEmpty(columnName)) return defaultValue;
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1 || result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getLong(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve long value for "+columnName,e);
            return defaultValue;
        }
    }
    
    /**
     * Retrieve a string from the provided column in the provided result set.
     * @param result The result set to retrieve from
     * @param columnName The column to retrieve
     * @return The actual string, or null if the column doesn't exist.
     */
    public static String safeGetString(Cursor result, String columnName) {
        if (result == null || TextUtils.isEmpty(columnName)) return null;
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1 || result.getColumnCount() <= columnIndex) return null;
        try {
            return result.getString(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve string value for "+columnName,e);
            return null;
        }
    }
    
    /**
     * Perform quick tests on a cursor to check that it has results.  Will also
     * move the cursor to the beginning of the result set (i.e., moveToFirst())
     * @param result The cursor to examine
     * @return True if the cursor has results and can be moved to the beginning
     */
    public static boolean hasResults(Cursor result) {
        return result != null && result.getCount() > 0 && result.moveToFirst();
    }

}
