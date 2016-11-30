package com.nenton.androidmiddle.di.components;

import com.nenton.androidmiddle.di.modules.PicassoCacheModule;
import com.nenton.androidmiddle.di.sqopes.RootScope;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PicassoCacheModule.class)
@RootScope
public interface PicassoCacheComponent {
    Picasso getPicasso();
}
