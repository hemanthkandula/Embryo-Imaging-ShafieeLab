package edu.harvard.bwh.shafieelab.embryoimaging.samples.loaders;

import android.content.Context;

import com.contentful.vault.Vault;

import java.util.List;

import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.Gallery;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.GallerySpace;

public class GalleryListLoader extends AbsLoader<List<Gallery>> {

    Context context;

    public GalleryListLoader(Context context) {
        this.context = context;
    }

    @Override
    protected List<Gallery> performLoad() {
        return Vault.with(context, GallerySpace.class).fetch(Gallery.class).all();
    }
}
