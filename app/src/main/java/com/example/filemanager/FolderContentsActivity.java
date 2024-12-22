package com.example.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderContentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FileExplorerAdapter adapter;
    private List<FileModel> fileList;
    private String folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        folderPath = getIntent().getStringExtra("folderPath");
        loadFiles();
    }

    private void loadFiles() {
        fileList = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(new FileModel(file.getName(), file.getAbsolutePath(), "file", "text/plain"));
                }
            }
        }

        adapter = new FileExplorerAdapter(fileList, this, false); // false означает, что это экран для файлов
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.folder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_create_file) {
            // Открываем экран для создания файла
            Intent intent = new Intent(this, NewFileOrFolderActivity.class);
            intent.putExtra("isFolder", false);
            intent.putExtra("folderPath", folderPath); // Указываем текущую папку
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Получаем путь к папке
            String parentPath = data.getStringExtra("parentPath");
            if (parentPath != null && parentPath.equals(folderPath)) {
                // Если это та же папка, обновляем список файлов
                loadFiles();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFiles(); // Обновляем список файлов
    }
}
