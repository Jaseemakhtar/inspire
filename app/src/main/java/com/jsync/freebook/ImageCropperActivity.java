package com.jsync.freebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;

import java.io.ByteArrayOutputStream;

public class ImageCropperActivity extends AppCompatActivity {
    Intent received;
    Button btnCrop;
    CropImageView cropImageView;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        btnCrop = findViewById(R.id.btnCrop);
        cropImageView = findViewById(R.id.cropImageView);
        cropImageView.setCropMode(CropImageView.CropMode.CUSTOM);
        cropImageView.setOutputHeight(320);
        cropImageView.setOutputWidth(280);

        received = getIntent();
        uri = Uri.parse(received.getStringExtra("URI"));
        cropImageView.load(uri).execute(new LoadCallback() {
            @Override
            public void onSuccess() {
                Log.i("ImageCropper","Loaded URI in cropView");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("ImageCropper","Failed to load URI in cropView");
            }
        });

        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.crop(uri).execute(new CropCallback() {
                    @Override
                    public void onSuccess(Bitmap cropped) {
                        Log.i("ImageCropper","Success cropping in cropView");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        cropped.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        Intent back2Tab3 = new Intent();
                        back2Tab3.putExtra("IMAGEBYTESIMAGEBYTES",byteArray);
                        setResult(RESULT_OK,back2Tab3);
                        finish();


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ImageCropper","Error cropping in cropView");
                    }
                });
            }
        });
    }

    public interface FileSelectedListener{
        void fileSelected(String fileSelected);
    }
}
