package com.example.filemanager;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import java.io.File;

public class FileModel {

    private String name;
    private String path;
    private String type;
    private String mimeType;

    public FileModel(String name, String path, String type, String mimeType) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getMimeType() {
        if (mimeType != null) {
            return mimeType; // Используем заранее заданный MIME-тип
        }

        // Определяем MIME-тип по расширению файла
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        // MIME-тип по умолчанию, если расширение неизвестно
        return "*/*";
    }

    // Новый метод для получения URI
    public Uri getUri(Context context) {
        File file = new File(path);
        return FileProvider.getUriForFile(context, "com.example.filemanagerapp.provider", file);
    }
}
