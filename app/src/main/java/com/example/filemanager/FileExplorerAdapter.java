package com.example.filemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FileExplorerAdapter extends RecyclerView.Adapter<FileExplorerAdapter.ViewHolder> {

    private final List<FileModel> fileList;
    private final Context context;
    private final boolean isFolderView;

    public FileExplorerAdapter(List<FileModel> fileList, Context context, boolean isFolderView) {
        this.fileList = fileList;
        this.context = context;
        this.isFolderView = isFolderView; // Если true, показываем только папки
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileModel file = fileList.get(position);

        // Установка текста
        holder.fileName.setText(file.getName());

        // Установка иконки
        if ("folder".equals(file.getType())) {
            holder.fileIcon.setImageResource(R.drawable.ic_folder);
        } else {
            holder.fileIcon.setImageResource(R.drawable.ic_file);
        }

        // Обработка нажатий
        holder.itemView.setOnClickListener(v -> {
            if ("folder".equals(file.getType())) {
                // Переход в папку
                Intent intent = new Intent(context, FolderContentsActivity.class);
                intent.putExtra("folderPath", file.getPath());
                context.startActivity(intent);
            } else {
                // Проверяем существование файла
                File actualFile = new File(file.getPath());
                if (!actualFile.exists()) {
                    Toast.makeText(context, "Файл не найден", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Получаем URI через FileProvider
                Uri fileUri = file.getUri(context);

                // Логируем данные
                Log.d("FileExplorerAdapter", "URI: " + fileUri);
                Log.d("FileExplorerAdapter", "MIME: " + file.getMimeType());

                // Открываем файл
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(fileUri, file.getMimeType());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Нет приложений для открытия файла", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if ("folder".equals(file.getType())) {
                // Переход в папку
                Intent intent = new Intent(context, FolderContentsActivity.class);
                intent.putExtra("folderPath", file.getPath());
                context.startActivity(intent);
            } else {
                // Открытие файла для редактирования
                Intent intent = new Intent(context, ViewOrEditFileActivity.class);
                intent.putExtra("filePath", file.getPath());
                intent.putExtra("parentPath", new File(file.getPath()).getParent()); // Передаём путь к папке
                ((AppCompatActivity) context).startActivityForResult(intent, 1);
            }
        });

        // Долгое нажатие
        holder.itemView.setOnLongClickListener(v -> {
            Intent intent = new Intent(context, EditFileOrFolderActivity.class);
            intent.putExtra("filePath", file.getPath());
            intent.putExtra("isFolder", "folder".equals(file.getType()));
            context.startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        ImageView fileIcon; // Добавляем сюда привязку для fileIcon

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
            fileIcon = itemView.findViewById(R.id.file_icon); // Привязываем fileIcon
        }
    }
}