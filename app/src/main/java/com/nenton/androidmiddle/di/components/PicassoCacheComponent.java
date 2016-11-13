package com.nenton.androidmiddle.di.components;

import com.nenton.androidmiddle.di.modules.PicassoCacheModule;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PicassoCacheModule.class)
@Singleton
public interface PicassoCacheComponent {
    Picasso getPicasso();
}
