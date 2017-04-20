package com.lsw.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.lsw.demo.api.FileDownloadListener;
import com.lsw.demo.utils.DownloadTask;

public class DownloadService extends Service {

    DownloadBinder downloadBinder = new DownloadBinder();
    DownloadListener downloadListener = new DownloadListener();
    private DownloadTask downloadTask;
    private String url;


    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    public class DownloadBinder extends Binder {
        public void startDownload(String url) {
            new DownloadTask(downloadListener).execute(url);
        }

        public void pauseDownload() {

        }

        public void cancelDownload() {

        }
    }

    class DownloadListener implements FileDownloadListener{

        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailed() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onCancel() {

        }
    }
}
