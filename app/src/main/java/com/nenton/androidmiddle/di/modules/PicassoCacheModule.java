package com.nenton.androidmiddle.di.modules;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.nenton.androidmiddle.di.sqopes.RootScope;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PicassoCacheModule {

    @Provides
    @RootScope
    Picasso providePicasso(Context context) {
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(context);
        Picasso picasso = new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .debugging(true)
                .build();
        Picasso.setSingletonInstance(picasso);
        return picasso;
    }
}
