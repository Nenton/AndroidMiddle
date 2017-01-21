package com.nenton.androidmiddle.di.modules;

import android.content.Context;

import com.nenton.androidmiddle.data.managers.PreferencesManager;
import com.nenton.androidmiddle.data.managers.RealmManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalModule extends FlavorLocalModule{

    @Provides
    @Singleton
    PreferencesManager provideAppPrefManager(Context context){
        return new PreferencesManager(context);
    }

}
