package edu.harvard.bwh.shafieelab.embryoimaging.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.navigation.Duo;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.activities.MainActivity;
import edu.harvard.bwh.shafieelab.embryoimaging.smartphone.SmartphoneActivity;
import edu.harvard.bwh.shafieelab.embryoimaging.standalone.StandaloneActivity;


public class Clean_Fragment extends Fragment {





    private View view;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.clean_fragment, container, false);


        LinearLayout samplehistory = view.findViewById(R.id.samples);
        samplehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                ((Duo)getActivity()).gotofrag(new Clean_Fragment(),4,false);
//                getActivity().getFragmentManager().popBackStack();


                startActivity(new Intent(getActivity(), MainActivity.class));

                getActivity().getFragmentManager().popBackStack();

            }
        });

        LinearLayout camera = view.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((Duo)getActivity()).gotofrag(new Clean_Fragment(),3,false);
                getActivity().getFragmentManager().popBackStack();


            }
        });


        LinearLayout Results = view.findViewById(R.id.ip);
        Results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlert();




            }
        });

        LinearLayout about  = view.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((Duo)getActivity()).gotofrag(new Clean_Fragment(),6,false);
                getActivity().getFragmentManager().popBackStack();


            }
        });







        ImageView imageView_hba = view.findViewById(R.id.image_standalone);

        imageView_hba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), StandaloneActivity.class));

                getActivity().getFragmentManager().popBackStack();

            }
        });



        ImageView imageView_dna = view.findViewById(R.id.image_smartphone);

        imageView_dna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(getActivity(), SmartphoneActivity.class));
                getActivity().getFragmentManager().popBackStack();



            }
        });


        SharedPreferences pref = getActivity().getSharedPreferences("IP", 0); // 0 - for private mode
        String IP = pref.getString("IP", null); // getting String
        if (IP == null || IP.equals("")) {
            showAlert();


        }



        return view;



    }


    private void showAlert() {

        SharedPreferences pref = getActivity().getSharedPreferences("IP", 0); // 0 - for private mode
        String IP = pref.getString("IP", null); // getting String


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter IP address:");
        if (IP != null) {
            builder.setMessage("Current IP address is:" + IP);
        }

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String IP = input.getText().toString();


                if (!IP.equals("")) {

                    SharedPreferences pref = getActivity().getSharedPreferences("IP", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("IP", IP); // Storing string
                    editor.apply(); // commit changes

                } else showAlert();


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
