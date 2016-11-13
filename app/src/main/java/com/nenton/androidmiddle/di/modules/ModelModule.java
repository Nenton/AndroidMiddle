package com.nenton.androidmiddle.di.modules;

import com.nenton.androidmiddle.data.managers.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {
    @Provides
    @Singleton
    DataManager provideDataManager(){
        return new DataManager();
    }
}
