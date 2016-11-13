package com.nenton.androidmiddle.di.components;

import com.nenton.androidmiddle.di.modules.ModelModule;
import com.nenton.androidmiddle.mvp.models.AbstractModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ModelModule.class)
@Singleton
public interface ModelComponent {
    void inject(AbstractModel abstractModel);
}
