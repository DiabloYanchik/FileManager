package com.example.filemanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FileScannerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            // Сканируем файловую систему
            FileHelper.scanFileSystem(getApplicationContext());
        }).start();
        return START_STICKY;
    }
}
