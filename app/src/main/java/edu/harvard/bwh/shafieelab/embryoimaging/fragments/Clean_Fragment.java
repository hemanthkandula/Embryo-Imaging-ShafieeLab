package edu.harvard.bwh.shafieelab.embryoimaging.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.navigation.Duo;
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


                ((Duo)getActivity()).gotofrag(new Clean_Fragment(),4,false);
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


        LinearLayout Results = view.findViewById(R.id.results);
        Results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((Duo)getActivity()).gotofrag(new Clean_Fragment(),5,false);
                getActivity().getFragmentManager().popBackStack();


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


        return view;



    }


}
