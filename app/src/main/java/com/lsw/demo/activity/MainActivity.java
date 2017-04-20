package com.lsw.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.Button;

import com.lsw.demo.R;
import com.lsw.demo.api.FileDownloadListener;
import com.lsw.demo.utils.DownloadTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FileDownloadListener mListener = new FileDownloadListener() {
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
    };

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_download:
                String url = "http://cdn.data.video.iqiyi.com/cdn/qiyiapp/20170109/143519wa13aeabb7130d2ddd04915138696da9c/TTSCore.zip";
                new DownloadTask(mListener).execute(url);
                break;
            case R.id.pause_download:
                break;
            case R.id.cancel_download:
                break;
            default:
                break;
        }
    }
}
