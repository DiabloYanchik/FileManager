package com.example.filemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Константы для базы данных
    private static final String DATABASE_NAME = "files.db";
    private static final int DATABASE_VERSION = 3;

    // Константы для таблицы и столбцов
    public static final String TABLE_FILES = "files";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MIME_TYPE = "mimeType";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы
        db.execSQL("CREATE TABLE " + TABLE_FILES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PATH + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_MIME_TYPE + " TEXT)");

        // Создание индексов
        db.execSQL("CREATE INDEX index_name ON " + TABLE_FILES + "(" + COLUMN_NAME + ");");
        db.execSQL("CREATE INDEX index_path ON " + TABLE_FILES + "(" + COLUMN_PATH + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаление старой таблицы и создание новой
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        onCreate(db);
    }

    // Получить экземпляр базы данных только для чтения
    public SQLiteDatabase getReadableDatabaseInstance() {
        return this.getReadableDatabase();
    }

    // Получить экземпляр базы данных для чтения и записи
    public SQLiteDatabase getWritableDatabaseInstance() {
        return this.getWritableDatabase();
    }
}
