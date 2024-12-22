package com.example.filemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class EditFileOrFolderActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonRename, buttonDelete;
    private String filePath;
    private boolean isFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file_or_folder);

        editTextName = findViewById(R.id.editTextName);
        buttonRename = findViewById(R.id.buttonRename);
        buttonDelete = findViewById(R.id.buttonDelete);

        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        isFolder = intent.getBooleanExtra("isFolder", false);

        File file = new File(filePath);
        editTextName.setText(file.getName());

        // Переименование
        buttonRename.setOnClickListener(v -> renameFileOrFolder());

        // Удаление
        buttonDelete.setOnClickListener(v -> deleteFileOrFolder());
    }

    private void renameFileOrFolder() {
        String newName = editTextName.getText().toString().trim();
        if (newName.isEmpty()) {
            Toast.makeText(this, "Введите новое имя", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);
        File newFile = new File(file.getParent(), newName);

        if (file.renameTo(newFile)) {
            // Обновляем базу данных через ContentProvider
            ContentValues values = new ContentValues();
            values.put("name", newFile.getName());
            values.put("path", newFile.getAbsolutePath());

            String selection = "path=?";
            String[] selectionArgs = {filePath};

            getContentResolver().update(FileProviderContract.CONTENT_URI, values, selection, selectionArgs);

            Toast.makeText(this, "Переименовано", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка переименования", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFileOrFolder() {
        File file = new File(filePath);
        if (deleteRecursively(file)) {
            // Удаляем из базы данных через ContentProvider
            String selection = "path=?";
            String[] selectionArgs = {filePath};

            getContentResolver().delete(FileProviderContract.CONTENT_URI, selection, selectionArgs);

            Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursively(child);
            }
        }
        return file.delete();
    }
}
