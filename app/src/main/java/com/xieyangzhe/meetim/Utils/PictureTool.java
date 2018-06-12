package com.xieyangzhe.meetim.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.xieyangzhe.meetim.Activities.TestActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by joseph on 6/12/18.
 */

public class PictureTool {
    public static final String DIR = Environment.getExternalStorageDirectory() + "/MeetIM/";
    public static final String URL = "http://www.buaasoft.tk/index.php";

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
            return file.getAbsolutePath();
        } catch (Exception e) {
            Log.d("Error", "savePic: " + e.getMessage());
            return "";
        }
    }

    public static String uploadPic(Bitmap bitmap, String fileName) {
        File file = convertBitmapToFile(bitmap);
        if (file == null) {
            return "";
        }
        OkHttp3Util.uploadFile(URL, convertBitmapToFile(bitmap), fileName, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("asd", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("asd", "onResponse: ");
            }
        });
        return fileName;
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
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
