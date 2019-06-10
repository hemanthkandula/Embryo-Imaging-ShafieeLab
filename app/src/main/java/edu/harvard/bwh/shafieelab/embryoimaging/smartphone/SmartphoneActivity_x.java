package edu.harvard.bwh.shafieelab.embryoimaging.smartphone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.camera.AutoFitTextureView;

public class SmartphoneActivity_x extends AppCompatActivity  {



    Button mpicturebutton;

//    private CameraKitView cameraKitView;
private static CameraX.LensFacing lensFacing = CameraX.LensFacing.FRONT;


    private String TAG = "SmartphoneActivity_x";

    private AutoFitTextureView mTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartphone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mTextureView = findViewById(R.id.texture);
        mTextureView.post(new Runnable() {
            @Override
            public void run() {
                startCamera();
            }
        });


    }

    private void startCamera() {

//        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

        DisplayMetrics metrics = new DisplayMetrics();

        mTextureView.getDisplay().getRealMetrics(metrics);


        Rational screenAspectRatio = new Rational(metrics.widthPixels, metrics.heightPixels);




        PreviewConfig config = new PreviewConfig.Builder()
                .setLensFacing(lensFacing)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .        setTargetAspectRatio(screenAspectRatio)

                .build();
        Preview preview = new Preview(config);

        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    @Override
                    public void onUpdated(Preview.PreviewOutput previewOutput) {
                        // Your code here. For example, use previewOutput.getSurfaceTexture()
                        // and post to a GL renderer.

                        ViewGroup parent = (ViewGroup) mTextureView.getParent();
                        parent.removeView(mTextureView);
                        parent.addView(mTextureView,0);
                        mTextureView.setSurfaceTexture(previewOutput.getSurfaceTexture());
                    }
                });



        ImageCaptureConfig imageCaptureConfig =
                new ImageCaptureConfig.Builder()
                        .setLensFacing(lensFacing)
        .setFlashMode(FlashMode.OFF)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
        .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
        .build();

       final ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getApplicationContext().getExternalFilesDir(null), "pic.jpg");


                imageCapture.takePicture(file,
                        new ImageCapture.OnImageSavedListener() {
                            @Override
                            public void onImageSaved(File file) {
                                // insert your code here.
                                String msg =" Image saved" +file.getAbsolutePath();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                            }
                            @Override
                            public void onError(
                                    ImageCapture.UseCaseError useCaseError,
                                    String message,
                                    Throwable cause) {
                                // insert your code here.
                            }
                        });
            }
        });

        CameraX.bindToLifecycle(this, preview, imageCapture);
    }












}
