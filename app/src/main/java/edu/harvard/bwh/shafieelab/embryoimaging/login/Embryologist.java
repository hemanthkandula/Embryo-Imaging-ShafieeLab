package edu.harvard.bwh.shafieelab.embryoimaging.login;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.harvard.bwh.shafieelab.embryoimaging.R;

public class Embryologist extends AppCompatActivity {


    Typeface fonts1;
    CheckBox remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);


        remember = findViewById(R.id.remember);

        fonts1 = Typeface.createFromAsset(Embryologist.this.getAssets(),
                "fonts/Lato-Light.ttf");

        TextView t4 = findViewById(R.id.remember);
        t4.setTypeface(fonts1);


    }
}
