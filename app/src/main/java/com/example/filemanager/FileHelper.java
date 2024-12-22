package com.example.filemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    public static List<FileModel> getFilesFromContentProvider(Context context) {
        List<FileModel> files = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                FileProviderContract.CONTENT_URI,
                new String[]{"name", "path", "type", "mimeType"},
                null, null, null);

        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex("name");
                int pathIndex = cursor.getColumnIndex("path");
                int typeIndex = cursor.getColumnIndex("type");
                int mimeTypeIndex = cursor.getColumnIndex("mimeType");

                while (cursor.moveToNext()) {
                    if (nameIndex != -1 && pathIndex != -1 && typeIndex != -1) {
                        String name = cursor.getString(nameIndex);
                        String path = cursor.getString(pathIndex);
                        String type = cursor.getString(typeIndex);
                        String mimeType = mimeTypeIndex != -1 ? cursor.getString(mimeTypeIndex) : null;

                        files.add(new FileModel(name, path, type, mimeType));
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return files;
    }

    // Новый метод для сканирования файловой системы
    public static void scanFileSystem(Context context) {
        File rootDirectory = context.getFilesDir(); // Базовая директория приложения
        scanDirectoryRecursively(rootDirectory, context);
    }

    private static void scanDirectoryRecursively(File directory, Context context) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Рекурсивно сканируем вложенные папки
                    scanDirectoryRecursively(file, context);
                } else {
                    // Добавляем файл в базу данных через ContentProvider
                    addFileToDatabase(file, context);
                }
            }
        }
    }

    private static void addFileToDatabase(File file, Context context) {
        // Проверяем, существует ли уже файл в базе данных
        Cursor cursor = context.getContentResolver().query(
                FileProviderContract.CONTENT_URI,
                new String[]{"path"},
                "path = ?",
                new String[]{file.getAbsolutePath()},
                null);

        if (cursor != null) {
            boolean fileExists = cursor.moveToFirst();
            cursor.close();

            if (fileExists) return; // Файл уже есть в базе данных, ничего не делаем
        }

        // Добавляем новый файл
        ContentValues values = new ContentValues();
        values.put("name", file.getName());
        values.put("path", file.getAbsolutePath());
        values.put("type", file.isDirectory() ? "folder" : "file");
        values.put("mimeType", file.isDirectory() ? null : "text/plain"); // Для примера "text/plain"

        context.getContentResolver().insert(FileProviderContract.CONTENT_URI, values);
    }
}
