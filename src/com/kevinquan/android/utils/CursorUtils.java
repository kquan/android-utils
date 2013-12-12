package com.kevinquan.android.utils;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CursorUtils {

    private static final String TAG = CursorUtils.class.getSimpleName();
    
    public static final long VALUE_NO_ID = -1;
    
    public static final int VALUE_FALSE = 0;
    public static final int VALUE_TRUE = 1;
    
    public static class IndexInfo {
        String table;
        String column;
        
        public IndexInfo(String table, String column) {
            this.table = table;
            this.column = column;
        }
    }
    
    public static void addIndex(SQLiteDatabase database, List<IndexInfo> index) {
        for (IndexInfo info : index) {
            String indexQuery = "CREATE INDEX idx_"+info.table+"_"+info.column+" ON "+info.table+"("+info.column+")";
            Log.d(TAG, "Adding index: "+indexQuery);
            database.execSQL(indexQuery);
        }
    }

    public static boolean hasColumn(Cursor result, String columnName) {
        int columnIndex = result.getColumnIndex(columnName);
        return columnIndex != -1;
    }
    
    public static String safeGetString(Cursor result, String columnName) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return null;
        try {
            return result.getString(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve string value for "+columnName,e);
            return null;
        }
    }
    
    public static int safeGetInt(Cursor result, String columnName, int defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        try {
            return result.getInt(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve int value for "+columnName,e);
            return defaultValue;
        }
    }

    public static long safeGetLong(Cursor result, String columnName, long defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        try {
            return result.getLong(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve long value for "+columnName,e);
            return defaultValue;
        }
    }

    public static boolean safeGetIntBackedBoolean(Cursor result, String columnName, boolean defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        try {
            return result.getInt(columnIndex) == VALUE_TRUE;
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve boolean (int backed) value for "+columnName,e);
            return defaultValue;
        }
    }
    
    public static double safeGetDouble(Cursor result, String columnName, double defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        try {
            return result.getDouble(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve string value for "+columnName,e);
            return defaultValue;
        }
    }
    
    public static void safeClose(Cursor result) {
        if (result != null) {
            result.close();
        }
    }

}
