package com.xieyangzhe.meetim.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by joseph on 6/12/18.
 */

public class OkHttp3Util {
    private static OkHttpClient okHttpClient = null;


    private OkHttp3Util() {
    }

    public static OkHttpClient getInstance() {

        if (okHttpClient == null) {
            //加同步安全
            synchronized (OkHttp3Util.class) {
                if (okHttpClient == null) {
                    //okhttp可以缓存数据....指定缓存路径
                    File sdcache = new File(Environment.getExternalStorageDirectory(),
                            "cache");
                    //指定缓存大小
                    int cacheSize = 10 * 1024 * 1024;

                    okHttpClient = new OkHttpClient.Builder()//构建器
                            .connectTimeout(15, TimeUnit.SECONDS)//连接超时
                            .writeTimeout(20, TimeUnit.SECONDS)//写入超时
                            .readTimeout(20, TimeUnit.SECONDS)//读取超时
                            .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize))
                            .build();
                }
            }

        }

        return okHttpClient;
    }

    public static void doGet(String oldUrl, Callback callback) {

        OkHttpClient okHttpClient = getInstance();
        Request request = new Request.Builder().url(oldUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);


    }

    public static void doPost(String url, Map<String, String> params, Callback callback) {
        OkHttpClient okHttpClient = getInstance();

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));

        }

        Request request = new Request.Builder().url(url).post(builder.build()).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

    }

    public static void uploadFile(String url, File file, String fileName, Map<String,String>
            params,Callback callback) {


        OkHttpClient okHttpClient = getInstance();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (params != null){
            for (String key : params.keySet()){
                builder.addFormDataPart(key,params.get(key));
            }
        }

        builder.addFormDataPart("file",fileName,RequestBody.create
                (MediaType.parse("application/octet-stream"),file));

        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder().url(url).post(multipartBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void doPostJson(String url, String jsonParams, Callback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse
                ("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);

    }

    public static void download(final String url, final String saveDir) {
        OkHttpClient client = new OkHttpClient();
        //获取请求对象
        Request request = new Request.Builder().url(url).build();
        //获取响应体
        try {
            ResponseBody body = client.newCall(request).execute().body();
            //获取流
            InputStream in = body.byteStream();
            //转化为bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            File appDir = new File(saveDir);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = getNameFromUrl(url);
            File file = new File(appDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.d("Error", "download: " + e.getMessage());
        }

    }

    public static String isExistDir(String saveDir) throws IOException {
        // 下载位置
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
            if (!downloadFile.mkdirs()) {
                downloadFile.createNewFile();
            }
            String savePath = downloadFile.getAbsolutePath();
            Log.e("savePath", savePath);
            return savePath;
        }
        return null;
    }

    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}