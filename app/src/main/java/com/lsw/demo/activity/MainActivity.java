package com.lsw.demo.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.Button;

import com.lsw.demo.R;
import com.lsw.demo.api.FileDownloadListener;
import com.lsw.demo.service.DownloadService;
import com.lsw.demo.utils.DownloadTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ServiceConnection serviceConnection = new MyServiceConnection();
    private DownloadService.DownloadBinder downloadBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startDownload = (Button) findViewById(R.id.start_download);
        Button pauseDownload = (Button) findViewById(R.id.pause_download);
        Button cancelDownload = (Button) findViewById(R.id.cancel_download);
        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_download:
                String url = "http://cdn.data.video.iqiyi.com/cdn/qiyiapp/20170109/143519wa13aeabb7130d2ddd04915138696da9c/TTSCore.zip";
                downloadBinder.startDownload(url);
                break;
            case R.id.pause_download:
                break;
            case R.id.cancel_download:
                break;
            default:
                break;
        }
    }

    class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

}
