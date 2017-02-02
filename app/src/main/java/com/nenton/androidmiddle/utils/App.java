package com.nenton.androidmiddle.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.nenton.androidmiddle.di.DaggerService;
import com.nenton.androidmiddle.di.components.AppComponent;
import com.nenton.androidmiddle.di.components.DaggerAppComponent;
import com.nenton.androidmiddle.di.modules.AppModule;
import com.nenton.androidmiddle.di.modules.PicassoCacheModule;
import com.nenton.androidmiddle.di.modules.RootModule;
import com.nenton.androidmiddle.mortar.ScreenScoper;
import com.nenton.androidmiddle.ui.activities.DaggerRootActivity_RootComponent;
import com.nenton.androidmiddle.ui.activities.RootActivity;

import io.realm.Realm;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class App extends Application {

    private static SharedPreferences sSharedPreferences;
    private static Context sContext;
    private static AppComponent sAppComponent;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
    private MortarScope mMortarScope;
    private MortarScope mRootActivityScope;
    private static RootActivity.RootComponent mRootActivityRootComponent;

    @Override
    public Object getSystemService(String name) {
        return mMortarScope.hasService(name) ? mMortarScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
//        .build());

        sContext = getApplicationContext();
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
        mRootActivityRootComponent = DaggerRootActivity_RootComponent.builder()
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

//    public static SharedPreferences getSharedPreferences() {
//        return sSharedPreferences;
//    }

    public static Context getContext() {
        return sContext;
    }

    public static RootActivity.RootComponent getRootActivityRootComponent() {
        return mRootActivityRootComponent;
    }
}
