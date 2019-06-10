package edu.harvard.bwh.shafieelab.embryoimaging.smartphone;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Rational;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import java.io.File;

import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.camera.AutoFitTextureView;

public class SmartphoneActivity extends AppCompatActivity  {



    Button mpicturebutton;

//    private CameraKitView cameraKitView;
private static CameraX.LensFacing lensFacing = CameraX.LensFacing.FRONT;


    private String TAG = "SmartphoneActivity";

    private AutoFitTextureView mTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartphone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mTextureView = findViewById(R.id.texture);

    }













}
