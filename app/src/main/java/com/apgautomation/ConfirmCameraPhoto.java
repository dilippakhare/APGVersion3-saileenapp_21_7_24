package com.apgautomation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.PictureResult;

public class ConfirmCameraPhoto extends AppCompatActivity {

    ImageView imageView;
    Button btnCacel,btnSelect;
    public static PictureResult pictureResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_camera_photo);
        imageView=findViewById(R.id.img);
        btnSelect=findViewById(R.id.btnSelect);
        btnCacel=findViewById(R.id.btnCacel);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK,new Intent());
                MainActivityLauncher.pictureResult=pictureResult;
                finish();
            }
        });
        btnCacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityLauncher.pictureResult=null;
                setResult(RESULT_CANCELED,new Intent());
                finish();
            }
        });

        try {
           pictureResult.toBitmap(1000, 1000, new BitmapCallback() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    try {
                        imageView.setImageBitmap(bitmap);
                    }
                    catch (Exception ex){}
                }
            });
        }
        catch (Exception ex)
        {}
    }
}