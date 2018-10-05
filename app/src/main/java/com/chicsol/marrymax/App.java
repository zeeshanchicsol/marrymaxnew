package com.chicsol.marrymax;

import android.app.Application;

import com.chicsol.marrymax.utils.FontsOverride;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Redz on 10/22/2016.
 */

public class App extends Application {
    private static App mInstance;
    public static String package_name;
    @Override
    public void onCreate() {
        super.onCreate();

        package_name = getPackageName();

        Fabric.with(this, new Crashlytics());
        mInstance = this;

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/centurygothic.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/centurygothic.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/centurygothic.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/centurygothic.ttf");

    }

    public static synchronized App getInstance() {
        return mInstance;
    }


   /* public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }*/


}
