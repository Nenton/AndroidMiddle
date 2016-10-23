package com.nenton.androidmiddle.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AndroidMiddleAplication extends Application {

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }
}
