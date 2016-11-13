package com.nenton.androidmiddle.di.modules;

import android.content.Context;

import com.nenton.androidmiddle.data.managers.PreferencesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalModule {

    @Provides
    @Singleton
    PreferencesManager provideAppPrefManager(Context context){
        return new PreferencesManager(context);
    }

//    @Provides
//    DataBaseManager provideDBManager(Context context){
//        return new DataBaseManager(context);
//    }
}
