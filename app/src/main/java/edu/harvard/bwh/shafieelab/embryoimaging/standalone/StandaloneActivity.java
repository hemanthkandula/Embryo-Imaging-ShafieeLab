package edu.harvard.bwh.shafieelab.embryoimaging.standalone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import edu.harvard.bwh.shafieelab.embryoimaging.EndPoints;
import edu.harvard.bwh.shafieelab.embryoimaging.R;

public class StandaloneActivity extends AppCompatActivity {

    private View view;
    ProgressBar progressBar;
    ZoomageView EmbryoImage;
    TextView IDVIEW,reloadview;
    String ID = null;

    Button StartButton ;
    ImageButton retakebutton;

    FloatingActionButton fab;

    private String TAG = "STANDALONE";

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
        setContentView(R.layout.activity_standalone);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.standalone_menu);

//        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //What to do on back clicked
//                Toast.makeText(getApplicationContext(),"Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        fab = findViewById(R.id.fab);
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
                getdirectimage();
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

                File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Standalone/"+ID);
                folder.mkdirs();

                reloadview.setVisibility(View.GONE);
                retakebutton.setVisibility(View.GONE);

            }}






    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
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


                    checkslide();

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

    private void showerroralert() {


        new AlertDialog.Builder(this)
                .setTitle("No connection!")
                .setMessage("Please make show Standalone device is connected.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        progressBar.setVisibility(View.GONE);

                        StartButton.setEnabled(true);
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission

            getdirectimage();
        }
    }


    private void checkslide() {
        final String[] checked_slide = {"init"};


        AsyncHttpClient client = new AsyncHttpClient();

        progressBar.setVisibility(View.VISIBLE);

        fab.setEnabled(false);


        try {

            RequestParams params = new RequestParams();
            params.put("slide", ID);
//        params.put("more", "data");

            client.get(getApplicationContext(), EndPoints.URL_check_slide, params,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            progressBar.setVisibility(View.GONE);
                            fab.setEnabled(true);


                            System.out.println(new String(responseBody));


                            checked_slide[0] = new String(responseBody);
                            Toast.makeText(getApplicationContext(), checked_slide[0], Toast.LENGTH_SHORT).show();
                            if (checked_slide[0].equals("Embryo dish ID matched")) {

                                EmbryoImage.setImageResource(0);

                                IDVIEW.setText("Subject ID:  " + ID);

                                IDVIEW.setVisibility(View.VISIBLE);
                                StartButton.setVisibility(View.VISIBLE);
                                retakebutton.setVisibility(View.GONE);

                                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Embryo Images/Standalone/" + ID);
                                folder.mkdirs();
                            } else {
                                showAlert();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                            progressBar.setVisibility(View.GONE);

                            showerroralert();
                            fab.setEnabled(true);


                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

//    return checked_slide[0];

    }







    private void getdirectimage() {
//        Glide.with(getApplicationContext())
//                //.load(mImageUri) // Load image from assets
//                .load(EndPoints.URL_get_image) // Image URL
//                .into(EmbryoImage); // ImageView to display image


        if (!isStoragePermissionGranted()) {
            return;
        }




        EmbryoImage.setImageResource(0);
        RequestOptions requestOptions = new RequestOptions();
        //requestOptions.signature(new ObjectKey(String.valueOf(System.currentTimeMillis())));

        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);



        progressBar.setVisibility(View.VISIBLE);
        retakebutton.setVisibility(View.GONE);
        StartButton.setEnabled(false);

        Glide.with(getApplicationContext())
                .load(EndPoints.URL_get_image+ID)
                .apply(requestOptions)
                .error(R.drawable.error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        showerroralert();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        Bitmap bmp = ((BitmapDrawable)resource).getBitmap();

                        String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Standalone/"+ID+"/";

                        File directory = new File(path);
                        if (!directory.exists()) {
                            boolean success = directory.mkdirs();
                            if(success){
                                Log.d("dire","Created");
                            }

                        }
                        File[] files = directory.listFiles();


                        File file = new File(path, ID+"_"+files.length+".png");
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                        StartButton.setVisibility(View.GONE);
                        retakebutton.setVisibility(View.VISIBLE);



                        return false;
                    }
                })
                .into(EmbryoImage)
        ;



//        AsyncHttpClient client = new AsyncHttpClient();
//
//
//        client.get(EndPoints.URL_get_image, null, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//
//
//
//
//
//
//
//                Bitmap bmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//
//                String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Standalone/"+ID+"/";
//
//                File directory = new File(path);
//
//                File[] files = directory.listFiles();
//
//
//
//
//                File file = new File(path, ID+"_"+files.length+".png");
//                FileOutputStream out = null;
//                try {
//                    out = new FileOutputStream(file);
//                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//                    // PNG is a lossless format, the compression factor (100) is ignored
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (out != null) {
//                            out.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                progressBar.setVisibility(View.INVISIBLE);
//
//            }
//        });

    }


    void getimageloc(){



        EmbryoImage.setImageResource(0);
        RequestOptions requestOptions = new RequestOptions();
        //requestOptions.signature(new ObjectKey(String.valueOf(System.currentTimeMillis())));

        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);

        String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Standalone/"+ID+".png";

        Uri imageUri = Uri.fromFile(new File(path));


        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(imageUri)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        Log.d(TAG, e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .into(EmbryoImage)
        ;
        progressBar.setVisibility(View.GONE);

    }





//    private void getdirectimage() {
//
//        Glide.with(this)
//                .load("url here") // image url
//                .placeholder(R.drawable.placeholder) // any placeholder to load at start
//                .error(R.drawable.)  // any image in case of error
//                .override(200, 200); // resizing
//        .centerCrop();
//        .into(imageView);  // imageview object
//    }


    public void getImage2(){



        AsyncHttpClient client = new AsyncHttpClient();




        JSONObject jsonParams = new JSONObject();
        try {

            StringEntity entity = new StringEntity(jsonParams.toString());


            client.post(getApplicationContext(), EndPoints.URL_get_image2, entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            System.out.println(new String(responseBody));
                            Bitmap bmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);

//                            EmbryoImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, EmbryoImage.getWidth(),
//                                    EmbryoImage.getHeight(), false));



                            String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Standalone/";

                            File file = new File(path, ID+".png");
                            FileOutputStream out = null;
                            try {
                                out = new FileOutputStream(file);
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }




//                            Drawable d = new BitmapDrawable(getResources(), bmp);
//                            EmbryoImage.setImageDrawable(d);


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            Log.d("TAG",new String(responseBody));

                        }
                    });

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }



//    private void getImage(){
//
//
//        //EmbryoImage.setImageResource(android.R.color.transparent);
//
//        AsyncHttpClient client = new AsyncHttpClient();
//
//        progressBar.setVisibility(View.VISIBLE);
//
//
////        JSONObject jsonParams = new JSONObject();
//
//
////            StringEntity entity = new StringEntity(jsonParams.toString());
//
//
//
//        client.get( EndPoints.URL_get_image, null,
//                new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
////                            getImage2();
//                        //Log.d("TAG",new String(responseBody));
//
//
////                            Picasso.with(getApplicationContext()).load(EndPoints.URL_get_image2).resize(50, 50).
////                                    centerCrop().into(EmbryoImage);
//
//                        progressBar.setVisibility(View.INVISIBLE);
//
//                        Bitmap bmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//                        //EmbryoImage.setImageBitmap(bmp);
//
//
////                            EmbryoImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, EmbryoImage.getWidth(),
////                                    EmbryoImage.getHeight(), false));
//
//
//
//                        String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Standalone/";
//
//                        File file = new File(path, ID+".png");
//                        FileOutputStream out = null;
//                        try {
//                            out = new FileOutputStream(file);
//                            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//                            // PNG is a lossless format, the compression factor (100) is ignored
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                                if (out != null) {
//                                    out.close();
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//
//
//
//                        File imgFile = new  File(Environment.getExternalStorageDirectory().toString()+"/Embryo Images/Standalone/"+ID+".png");
//
//
//                        if(imgFile.exists()){
//
//
//                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//                            ImageView myImage = (ImageView) view.findViewById(R.id.embryo_image);
//
//                            myImage.setImageBitmap(myBitmap);
////                                reloadview.setVisibility(View.INVISIBLE);
//                            progressBar.setVisibility(View.INVISIBLE);
//
//                        }
//
//
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
////                            Log.d("TAGerror",new String(responseBody));
//
//
//
////reloadview.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//                });
//
//
//
//
//    }

    public void retake(){

//        getImage();





        if(ID== null )

            showAlert();
        else {
            if(ID.equals(""))
                showAlert();
            else{
                EmbryoImage.setImageResource(0);


                getdirectimage();
                IDVIEW.setText("Patient ID:  "+ID);
            }}



        getdirectimage();

    }

    public void showAl(View v){

    }




}
