package com.lsw.demo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.lsw.demo.R;
import com.lsw.demo.activity.MainActivity;
import com.lsw.demo.api.FileDownloadListener;
import com.lsw.demo.utils.DownloadTask;

public class DownloadService extends Service {

    DownloadBinder downloadBinder = new DownloadBinder();
    DownloadListener downloadListener = new DownloadListener();
    private DownloadTask downloadTask;
    private String url;

    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    public class DownloadBinder extends Binder {
        public void startDownload(String url) {
            downloadTask = new DownloadTask(downloadListener);
            downloadTask.execute(url);
            getNotification("正在下载....",0);
        }

        public void pauseDownload() {

        }

        public void cancelDownload() {

        }
    }

    class DownloadListener implements FileDownloadListener{

        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("正在下载...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("下载完成", -1));
            getNotificationManager().cancel(1);
            Toast.makeText(DownloadService.this, "下载完成", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("下载失败", -1));
            getNotificationManager().cancel(1);
            Toast.makeText(DownloadService.this, "下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPause() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "暂停下载", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "取消下载", Toast.LENGTH_SHORT).show();
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title,int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        if (progress >= 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

}
