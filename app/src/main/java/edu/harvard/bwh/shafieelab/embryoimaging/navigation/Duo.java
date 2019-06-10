package edu.harvard.bwh.shafieelab.embryoimaging.navigation;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;


import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.fragments.Clean_Fragment;
import edu.harvard.bwh.shafieelab.embryoimaging.smartphone.SmartphoneActivity_x;
import edu.harvard.bwh.shafieelab.embryoimaging.standalone.StandaloneActivity;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class Duo extends AppCompatActivity implements DuoMenuView.OnMenuClickListener  {
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;

    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new Clean_Fragment(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        finish();
//        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }

    public void goToFragment(Fragment fragment, boolean addToBackStack) {

//        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.container)).add(R.id.container, fragment).commit();;



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        Fragment fragment1= getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment1!=null){
transaction.remove(fragment1);}
        transaction.add(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {

            case 0:
                goToFragment(new Clean_Fragment(), false);
                getFragmentManager().popBackStack();


                break;
            case 1:
                startActivity(new Intent(getApplicationContext(), StandaloneActivity.class));
                getFragmentManager().popBackStack();


                break;
            case 2:
                startActivity(new Intent(getApplicationContext(), SmartphoneActivity_x.class));
                getFragmentManager().popBackStack();
                break;
            case 3:
                goToFragment(new Clean_Fragment(), false);
                getFragmentManager().popBackStack();

                break;
            case 4:
                goToFragment(new Clean_Fragment(), false);
                getFragmentManager().popBackStack();
                break;

            case 5:
                goToFragment(new Clean_Fragment(), false);
                getFragmentManager().popBackStack();

                break;


            default:
                goToFragment(new Clean_Fragment(), false);
                getFragmentManager().popBackStack();

                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    public void gotofrag(Fragment fragment, int position,Boolean addstack){

        setTitle(mTitles.get(position));
        mMenuAdapter.setViewSelected(position, true);

        goToFragment(fragment,addstack);

    }

    public void SetMenuTitle(int s){


        setTitle(mTitles.get(s));


    }


    @Override
    public void onBackPressed() {

        Fragment currentFragment= getSupportFragmentManager().findFragmentById(R.id.container);
Log.d("debugged",currentFragment+"");
        setTitle(mTitles.get(0));


        if (currentFragment instanceof Clean_Fragment) {
            Log.v("debugged", "done");
        }else
        {
            gotofrag(new Clean_Fragment(),0,false);

        }


//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//
//            Log.d("debugged","popped");
//
//        } else {
//Log.d("Back stack2","popped");
////            getFragmentManager().popBackStack();
//
//        }

    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }
}
