package com.xieyangzhe.meetim.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.xieyangzhe.meetim.Activities.TestActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by joseph on 6/12/18.
 */

public class PictureTool {
    public static final String DIR = Environment.getExternalStorageDirectory() + "/MeetIM/";
    public static final String URL = "http://img.xieyangzhe.com/index.php";
    public static final String UPLOAD_SUCCESS = "UPLOAD_SUCCESS";

    public static String savePic(Bitmap bitmap) {

        File tmp = new File(DIR);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        String fileName = UUID.randomUUID().toString();
        File file = new File(DIR + fileName + ".png");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return fileName + ".png";
        } catch (Exception e) {
            Log.d("Error", "savePic: " + e.getMessage());
            return "";
        }
    }

    public static void uploadPic(Bitmap bitmap, String fileName) {
        File file = convertBitmapToFile(bitmap);
        if (file == null) {
            return;
        }
        OkHttp3Util.uploadFile(URL, convertBitmapToFile(bitmap), fileName, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("asd", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Intent intent = new Intent(UPLOAD_SUCCESS);
                intent.putExtra("filename", fileName);
                IMApplication.getAppContext().sendBroadcast(intent);
            }
        });
    }

    public static void downloadPic(String url) {

       OkHttp3Util.download(url, DIR);
    }

    public static Bitmap getPic(String path) {
        return BitmapFactory.decodeFile(path);
    }

    static private File convertBitmapToFile(Bitmap bitmap) {
        File f;
        try {
            // create a file to write bitmap data
            f = new File(IMApplication.getAppContext().getCacheDir(), "images");
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (Exception e) {
            return null;
        }
    }
}
