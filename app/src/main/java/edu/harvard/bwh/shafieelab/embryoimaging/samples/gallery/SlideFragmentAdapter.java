package edu.harvard.bwh.shafieelab.embryoimaging.samples.gallery;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import edu.harvard.bwh.shafieelab.embryoimaging.samples.fragments.SlideFragment;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.Gallery;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.Image;

public class SlideFragmentAdapter extends FragmentPagerAdapter {
    private final Gallery data;

    private final Map<Image, Fragment> fragments;

    public SlideFragmentAdapter(Context context, FragmentManager fm, Gallery gallery) {
        super(fm);
        this.data = gallery;
        this.fragments = new HashMap<>();

        for (Image image : gallery.images()) {
            fragments.put(image, SlideFragment.newSlide(context, image));
        }
    }

    public Image getImage(int position) {
        return data.images().get(position);
    }

    public SlideFragment fragmentForPosition(int position) {
        return (SlideFragment) fragments.get(getImage(position));
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentForPosition(position);
    }

    @Override
    public int getCount() {
        return data.images().size();
    }
}
