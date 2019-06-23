package edu.harvard.bwh.shafieelab.embryoimaging.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.navigation.Duo;

public class Admin extends AppCompatActivity {


    Typeface fonts1;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        remember = findViewById(R.id.remember);

        fonts1 = Typeface.createFromAsset(Admin.this.getAssets(),
                "fonts/Lato-Light.ttf");

        TextView t4 = findViewById(R.id.remember);
        t4.setTypeface(fonts1);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        findViewById(R.id.signin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Admin.this, Duo.class));
                finish();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
