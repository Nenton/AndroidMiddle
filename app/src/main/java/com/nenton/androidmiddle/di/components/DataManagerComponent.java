package com.nenton.androidmiddle.di.components;

import com.nenton.androidmiddle.data.managers.DataManager;
import com.nenton.androidmiddle.di.modules.LocalModule;
import com.nenton.androidmiddle.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {LocalModule.class, NetworkModule.class})
@Singleton
public interface DataManagerComponent {
    void inject(DataManager dataManager);
}
