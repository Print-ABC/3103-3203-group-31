package com.ncshare.ncshare;

import android.app.Application;
import android.content.Context;

public class NCShare extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        NCShare.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return NCShare.context;
    }
}
