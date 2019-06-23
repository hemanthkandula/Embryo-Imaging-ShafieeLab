package edu.harvard.bwh.shafieelab.embryoimaging;


import android.app.Application;

import com.contentful.vault.SyncConfig;
import com.contentful.vault.Vault;

import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.ClientProvider;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.GallerySpace;


public class App extends Application {
    private static App instance;

    public static App get() {
        return instance;
    }

    public static void requestSync() {
        requestSync(false);
    }

    public static void requestSync(boolean invalidate) {
        Vault.with(get(), GallerySpace.class).requestSync(
                SyncConfig.builder()
                        .setClient(ClientProvider.get())
                        .setInvalidate(invalidate)
                        .build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Fresco.initialize(this);

        instance = this;

    }
}