package edu.harvard.bwh.shafieelab.embryoimaging.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.harvard.bwh.shafieelab.embryoimaging.R;

public class LoginActivity extends AppCompatActivity {
    TextView embryologist, admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        embryologist = findViewById(R.id.embryologist);
        admin = findViewById(R.id.admin);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, Admin.class);
                startActivity(it);

            }
        });


        embryologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, Embryologist.class);
                startActivity(it);


            }
        });

    }
}
