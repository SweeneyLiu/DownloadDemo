package com.lsw.demo.utils;

import android.os.AsyncTask;

import com.lsw.demo.DownloadDemoApplication;
import com.lsw.demo.api.FileDownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liushuwei on 2017/4/20.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    private FileDownloadListener mDownloadListener;
    private static final int TYPE_SUCCESS = 0;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSE = 2;
    private static final int TYPE_CANCEL = 3;

    private boolean isCancel = false;
    private boolean isPause = false;

    private int lastProgress;
    private File downloadFileName;
//    private String downloadFilePath;

    public DownloadTask(FileDownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String downloadUrl = params[0];
        String fileFullName = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);//获取请求文件名
//        String fileName = fileFullName.substring(0,fileFullName.indexOf("."));
        File directoryFile = DownloadDemoApplication.getContext().getFilesDir();//files路径
        downloadFileName = new File(directoryFile,fileFullName);//获得文件的全路径
//        downloadFilePath = new File(directoryFile.getAbsolutePath(),fileName).getAbsolutePath();

        long downloadedLength = 0;//文件已经下载的长度
        long totalLength = 0;//待下载文件的总长度
        InputStream is = null;
        RandomAccessFile savedFile = null;

        if(downloadFileName.exists()){
            downloadedLength = downloadFileName.length();
        }

        try {
            totalLength = getContentLength(downloadUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(totalLength == 0){
            return TYPE_FAILED;
        }else if(totalLength == downloadedLength){
            return TYPE_SUCCESS;
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                // 断点下载，指定从哪个字节开始下载
                .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                .url(downloadUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(downloadFileName, "rw");
                savedFile.seek(downloadedLength); // 跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCancel) {
                        return TYPE_CANCEL;
                    } else if(isPause) {
                        return TYPE_PAUSE;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        // 计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / totalLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCancel && downloadFileName != null) {
                    downloadFileName.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 获取下载文件的大小
     * @param url
     * @return
     * @throws IOException
     */
    private long getContentLength(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress >= lastProgress) {
            mDownloadListener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch(integer){
            case TYPE_SUCCESS:
                mDownloadListener.onSuccess();
                try {
                    ZipUtil.UnZipFolder(downloadFileName.getAbsolutePath(),DownloadDemoApplication.getContext().getFilesDir().getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TYPE_FAILED:
                mDownloadListener.onFailed();
                break;
            case TYPE_PAUSE:
                mDownloadListener.onPause();
                break;
            case TYPE_CANCEL:
                mDownloadListener.onCancel();
                break;
            default:
                break;
        }
    }
}
