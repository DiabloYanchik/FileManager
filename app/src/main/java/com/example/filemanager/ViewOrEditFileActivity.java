package com.example.filemanager;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewOrEditFileActivity extends AppCompatActivity {

    private EditText editTextContent;
    private Button buttonSave;
    private String filePath;
    private String parentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_or_edit_file);

        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);

        // Получаем пути из Intent
        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        parentPath = intent.getStringExtra("parentPath");

        loadFileContent();

        // Сохранение изменений
        buttonSave.setOnClickListener(v -> saveFileContentAndReturn());
    }

    private void loadFileContent() {
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            String content = new String(buffer);
            editTextContent.setText(content);
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка чтения файла: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFileContentAndReturn() {
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String content = editTextContent.getText().toString();
            fos.write(content.getBytes());

            // Уведомляем пользователя
            Toast.makeText(this, "Файл сохранён", Toast.LENGTH_SHORT).show();

            // Возвращаемся обратно в директорию
            Intent resultIntent = new Intent();
            resultIntent.putExtra("parentPath", parentPath); // Возвращаем путь к папке
            setResult(RESULT_OK, resultIntent);
            finish(); // Завершаем текущую активность
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка сохранения файла: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
