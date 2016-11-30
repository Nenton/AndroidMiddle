package com.nenton.androidmiddle.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.components.AppComponent;
import com.nenton.androidmiddle.di.components.DaggerAppComponent;
import com.nenton.androidmiddle.di.modules.AppModule;
import com.nenton.androidmiddle.di.modules.PicassoCacheModule;
import com.nenton.androidmiddle.di.modules.RootModule;
import com.nenton.androidmiddle.mortar.ScreenScoper;
import com.nenton.androidmiddle.ui.activities.DaggerRootActivity_Root_Component;
import com.nenton.androidmiddle.ui.activities.RootActivity;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class AndroidMiddleAplication extends Application {

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static AppComponent sAppComponent;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
    private MortarScope mMortarScope;
    private MortarScope mRootActivityScope;
    private RootActivity.RootComponent mRootActivityRootComponent;

    @Override
    public Object getSystemService(String name) {
        return mMortarScope.hasService(name) ? mMortarScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        createAppComponent();
        createRootActivityComponent();

        mMortarScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, sAppComponent)
                .build("Root");
        mRootActivityScope = mMortarScope.buildChild()
                .withService(DaggerService.SERVICE_NAME, mRootActivityRootComponent)
                .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                .build(RootActivity.class.getName());

        ScreenScoper.registerScope(mMortarScope);
        ScreenScoper.registerScope(mRootActivityScope);
    }

    private void createRootActivityComponent() {
        mRootActivityRootComponent = DaggerRootActivity_Root_Component.builder()
                .appComponent(sAppComponent)
                .rootModule(new RootModule())
                .picassoCacheModule(new PicassoCacheModule())
                .build();
    }

    private void createAppComponent() {
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
