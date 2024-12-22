package com.example.filemanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class FileExplorerContentProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return databaseHelper.getReadableDatabase().query(
                "files", projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = databaseHelper.getWritableDatabase().insert("files", null, values);
        if (id != -1) {
            return Uri.withAppendedPath(FileProviderContract.CONTENT_URI, String.valueOf(id));
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return databaseHelper.getWritableDatabase().delete("files", selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return databaseHelper.getWritableDatabase().update("files", values, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
