package com.s.video.musicas.scooby.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class Application extends android.app.Application {
    private static Application mInstance;
    private SharedPreferences sharedPreferences;
    private Activity activity;


    public static Application getmInstance() {
        return mInstance;
    }

    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }
}
