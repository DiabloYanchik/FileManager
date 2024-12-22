package com.example.filemanager;

import android.net.Uri;

public class FileProviderContract {
    public static final String AUTHORITY = "com.example.filemanagerapp.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/files");
}
