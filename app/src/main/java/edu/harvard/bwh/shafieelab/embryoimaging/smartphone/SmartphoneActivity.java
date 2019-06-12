package edu.harvard.bwh.shafieelab.embryoimaging.smartphone;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;
import com.otaliastudios.cameraview.Audio;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.Hdr;
import com.otaliastudios.cameraview.Mode;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;
import java.io.IOException;

import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.camera.AutoFitTextureView;

public class SmartphoneActivity extends AppCompatActivity  {



    Button mpicturebutton;

//    private CameraKitView cameraKitView;
private static CameraX.LensFacing lensFacing = CameraX.LensFacing.FRONT;


    private String TAG = "SmartphoneActivity";

    private AutoFitTextureView mTextureView;



    private View view;
    ProgressBar progressBar;
    ZoomageView EmbryoImage;
    TextView IDVIEW,reloadview;
    String ID = null;

    Button StartButton ;
    ImageButton retakebutton;
    CameraView camera;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.standalone_menu, menu);
        return true;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartphone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.standalone_menu);

        setSupportActionBar(toolbar);



        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }




        mTextureView = findViewById(R.id.texture);


        camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);
        camera.setMode(Mode.PICTURE); // for pictures
        camera.setFacing(Facing.FRONT);
        camera.setFlash(Flash.OFF);
        camera.setHdr(Hdr.ON);
        camera.setAudio(Audio.OFF);



        camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // Pinch to zoom!





        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {


                String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Smartphone/"+ID+"/";

                File directory = new File(path);
                if (!directory.exists()) {
                    boolean success = directory.mkdirs();
                    if(success){
                        Log.d("dire","Created");
                    }

                }
                File[] files = directory.listFiles();


                File file = new File(path, ID+"_"+files.length+".png");

                result.toBitmap(EmbryoImage.getMaxWidth(), EmbryoImage.getMaxHeight(), new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        EmbryoImage.setImageBitmap(bitmap);
                        EmbryoImage.setVisibility(View.VISIBLE);
                        camera.setVisibility(View.GONE);
                        retakebutton.setVisibility(View.VISIBLE);
                    }
                });


                result.toFile(file, new FileCallback() {
                    @Override
                    public void onFileReady(@Nullable File file) {

                        String msg =" Image saved" +file.getAbsolutePath();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        camera.setVisibility(View.GONE);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        EmbryoImage = findViewById(R.id.embryo_image);
        IDVIEW = findViewById(R.id.ID);

        reloadview =findViewById(R.id.reload);
        retakebutton =findViewById(R.id.retake);

        StartButton = findViewById(R.id.start);
        StartButton.setVisibility(View.GONE);
        retakebutton.setVisibility(View.GONE);
        reloadview.setVisibility(View.GONE);



        Menu menu = toolbar.getMenu();



        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartButton.setVisibility(View.GONE);

                camera.takePicture();



            }
        });

        retakebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retake();
                retakebutton.setVisibility(View.GONE);
            }
        });





        if(ID== null )

            showAlert();
        else {
            if(ID.equals(""))
                showAlert();
            else{
                EmbryoImage.setImageResource(0);

//                getimageloc();
                IDVIEW.setText("Patient ID:  "+ID);

                File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Smartphone/"+ID);
                folder.mkdirs();

                reloadview.setVisibility(View.GONE);
                retakebutton.setVisibility(View.GONE);
                camera.setVisibility(View.VISIBLE);

            }}









    }







    public void retake(){

//        getImage();





        if(ID== null )

            showAlert();
        else {
            if(ID.equals(""))
                showAlert();
            else{
                EmbryoImage.setImageResource(0);



                IDVIEW.setText("Patient ID:  "+ID);
            }}



            camera.setVisibility(View.VISIBLE);
            StartButton.setVisibility(View.VISIBLE);
    }


    private void showAlert(){






        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Patient ID");

        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ID = input.getText().toString();
//                getSupportActionBar().setTitle("Ovulation ");
//                IDVIEW.setText("Patient ID:  "+ID);

                if(!ID.equals(""))
                {
//                    callpic();
                    EmbryoImage.setImageResource(0);

                    IDVIEW.setText("Subject ID:  "+ID);

                    IDVIEW.setVisibility(View.VISIBLE);
                    StartButton.setVisibility(View.VISIBLE);
                    retakebutton.setVisibility(View.GONE);
                    camera.setVisibility(View.VISIBLE);
                    File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Smartphone/"+ID);
                    folder.mkdirs();


//                        TODO
//                    getImage();
                }
                else showAlert();


                //         getImage();
//                getdirectimage();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();

    }




}
