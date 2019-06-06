package edu.harvard.bwh.shafieelab.embryoimaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import edu.harvard.bwh.shafieelab.embryoimaging.fragments.Clean_Fragment;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        frameLayout = (FrameLayout) findViewById(R.id.framelayout);


        Fragment fragment = null;
        replace_fragment(new Clean_Fragment());
    }

    public void replace_fragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

}
