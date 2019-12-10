package com.pandacard.teavel.apps;

import android.app.Application;

import com.pandacard.teavel.https.HttpManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        HttpManager.getInstance();
    }
}
