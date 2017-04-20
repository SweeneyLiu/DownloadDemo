package com.lsw.demo.api;

/**
 * Created by liushuwei on 2017/4/20.
 */

public interface FileDownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPause();

    void onCancel();
}
