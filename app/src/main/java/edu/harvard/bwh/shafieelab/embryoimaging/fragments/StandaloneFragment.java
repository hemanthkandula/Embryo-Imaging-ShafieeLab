package edu.harvard.bwh.shafieelab.embryoimaging.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import edu.harvard.bwh.shafieelab.embryoimaging.EndPoints;
import edu.harvard.bwh.shafieelab.embryoimaging.R;
import mehdi.sakout.fancybuttons.FancyButton;


public class StandaloneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    ProgressBar progressBar;
    ZoomageView EmbryoImage;
    TextView IDVIEW,reloadview;
    String ID = null;

    Button StartButton ;
    ImageButton retakebutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_standalone, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
        progressBar = view.findViewById(R.id.progressBar);
        EmbryoImage = view.findViewById(R.id.embryo_image);
        IDVIEW = view.findViewById(R.id.ID);

        reloadview =view.findViewById(R.id.reload);
        retakebutton =view.findViewById(R.id.retake);

        StartButton = view.findViewById(R.id.start);
        StartButton.setVisibility(View.GONE);
        retakebutton.setVisibility(View.GONE);
        reloadview.setVisibility(View.GONE);


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

                File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Embryo Images/"+ID);
                folder.mkdirs();

                reloadview.setVisibility(View.GONE);
                retakebutton.setVisibility(View.GONE);

            }}








        return view;

    }




    private void showAlert(){






        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Patient ID");

        final EditText input = new EditText(getActivity());
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

                    File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Embryo Images/"+ID);
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





















    private void getdirectimage() {
//        Glide.with(getApplicationContext())
//                //.load(mImageUri) // Load image from assets
//                .load(EndPoints.URL_get_image) // Image URL
//                .into(EmbryoImage); // ImageView to display image



        EmbryoImage.setImageResource(0);
        RequestOptions requestOptions = new RequestOptions();
        //requestOptions.signature(new ObjectKey(String.valueOf(System.currentTimeMillis())));

        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);



        progressBar.setVisibility(View.VISIBLE);
        retakebutton.setVisibility(View.GONE);
        StartButton.setEnabled(false);

        Glide.with(getActivity())
                .load(EndPoints.URL_get_image+ID)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        Bitmap bmp = ((BitmapDrawable)resource).getBitmap();

                        String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/"+ID+"/";

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
//                String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/"+ID+"/";
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

        String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/"+ID+".png";

        Uri imageUri = Uri.fromFile(new File(path));


        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(imageUri)
                .apply(requestOptions)

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



            client.post(getActivity(), EndPoints.URL_get_image2, entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            System.out.println(new String(responseBody));
                            Bitmap bmp = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);

//                            EmbryoImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, EmbryoImage.getWidth(),
//                                    EmbryoImage.getHeight(), false));



                            String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/";

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
//                        String path = Environment.getExternalStorageDirectory().toString()+"/Embryo Images/";
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
//                        File imgFile = new  File(Environment.getExternalStorageDirectory().toString()+"/Embryo Images/"+ID+".png");
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
