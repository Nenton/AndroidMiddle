package com.nenton.androidmiddle.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nenton.androidmiddle.di.components.AppComponent;
import com.nenton.androidmiddle.di.components.DaggerAppComponent;
import com.nenton.androidmiddle.di.modules.AppModule;

public class AndroidMiddleAplication extends Application {

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static AppComponent sAppComponent;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        createComponent();
    }

    private void createComponent() {
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getContext() {
        return sContext;
    }
}
