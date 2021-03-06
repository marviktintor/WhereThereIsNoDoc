package com.marvik.apps.coreutils.database.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by victor on 1/10/2016.
 */
public class DatabaseUtilities {

    private Context context;

    public DatabaseUtilities(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public int truncateTable(Uri uri) {
        return getContext().getContentResolver().delete(uri, null, null);
    }

    public boolean isExists(@NonNull Uri uri, @NonNull String[] columns, @NonNull String[] columnValues) {

        String where = null;

        if (columns.length != columnValues.length) {
            throw new IllegalArgumentException("Missing values for Columns " + Arrays.deepToString(columns) + ", You provided " + Arrays.deepToString(columnValues));
        }

        if (columns.length == columnValues.length) {

            for (int i = 0; i < columns.length; i++) {
                if (i == 0) {
                    where = "";
                }
                where += columns[i] + "='" + columnValues[i] + "' ";
                if (i < columns.length - 1) {
                    where += " AND ";
                }
            }
        }

        Log.i("WHERE_CLAUSE", "isExists(" + where + ")");

        Cursor cursor = getContext().getContentResolver().query(uri, null, where, null, null);

        boolean isExists = false;

        if (cursor != null) {
            isExists = cursor.getCount() > 0;
            cursor.close();
        }

        return isExists;
    }

    public String getColumnsValues(@NonNull Uri uri, @NonNull String[] index, @NonNull String[] indexColumn, String targetColumn) {
        String where = null;
        if (index.length == 0 && indexColumn.length == 0) {
            where = null;
        } else {
            where = "";
            if (index.length != indexColumn.length) {
                throw new IllegalArgumentException("Missing params for Columns " + Arrays.deepToString(indexColumn) + ", You provided " + Arrays.deepToString(index));
            }

            if (index.length == indexColumn.length) {
                for (int i = 0; i < indexColumn.length; i++) {
                    where += indexColumn[i] + "='" + index[i] + "' ";
                    if (i < (index.length - 1)) {
                        where += " AND ";
                    }
                }
            }
        }

        Log.i("WHERE_CLAUSE", "getColumnsValues(" + where + ")");

        Cursor cursor = getContext().getContentResolver().query(uri, null, where, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            String foundValue = null;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                foundValue = cursor.getString(cursor.getColumnIndex(targetColumn));
            }
            if (cursor != null) cursor.close();
            return foundValue;
        }
        if (cursor != null) cursor.close();
        return null;
    }
}
