package com.example.filemanager;

import android.content.ContentValues;
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

public class NewFileOrFolderActivity extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file_or_folder);

        editTextName = findViewById(R.id.editTextName);
        buttonCreate = findViewById(R.id.buttonCreate);

        buttonCreate.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                boolean isFolder = getIntent().getBooleanExtra("isFolder", false);
                createFileOrFolder(name, isFolder);
                finish();
            } else {
                Toast.makeText(this, "Введите имя файла или папки", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createFileOrFolder(String name, boolean isFolder) {
        String folderPath = getIntent().getStringExtra("folderPath");
        File directory = folderPath != null ? new File(folderPath) : getFilesDir(); // Корневая директория
        File newFile = new File(directory, name);

        try {
            boolean created = isFolder ? newFile.mkdir() : newFile.createNewFile();
            if (created) {
                Toast.makeText(this, isFolder ? "Папка создана" : "Файл создан", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка создания", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
