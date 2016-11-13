package com.nenton.androidmiddle.di.components;

import android.content.Context;

import com.nenton.androidmiddle.di.modules.AppModule;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}
