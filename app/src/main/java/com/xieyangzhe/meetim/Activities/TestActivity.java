package com.xieyangzhe.meetim.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.OkHttp3Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TestActivity extends AppCompatActivity {

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private static final String PHOTO_FILE_NAME = "headPhoto.jpg";
    private File tempFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button buttonTest = findViewById(R.id.button_test);
        buttonTest.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                ContentResolver contentResolver = getContentResolver();
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    String url = "http://www.buaasoft.tk/index.php";
                    OkHttp3Util.uploadFile(url, convertBitmapToFile(bitmap), "test.png", null, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("asd", "onFailure: ");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.d("asd", "onResponse: ");
                        }
                    });

                } catch (Exception e) {
                    Log.d("Error", "onActivityResult: " + e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // convert bitmap to file

    public File convertBitmapToFile(Bitmap bitmap) {
        File f;
        try {
            // create a file to write bitmap data
            f = new File(TestActivity.this.getCacheDir(), "portrait");
            f.createNewFile();

            // convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            // write the bytes in file
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
