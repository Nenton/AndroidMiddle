package com.nenton.androidmiddle.di.modules;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.nenton.androidmiddle.utils.App;
import com.nenton.androidmiddle.utils.AppConfig;

import dagger.Module;
import dagger.Provides;

/**
 * Created by serge on 01.02.2017.
 */
@Module
public class FlavorModelModule {

    @Provides
    JobManager provideJobManager(){
        Configuration configuration = new Configuration.Builder(App.getContext())
                .minConsumerCount(AppConfig.MIN_CONSUMER_COUNT)
                .maxConsumerCount(AppConfig.MAX_CONSUMER_COUNT)
                .loadFactor(AppConfig.LOAD_FACTOR)
                .consumerKeepAlive(AppConfig.KEEP_ALIVE)
                .build();
        return new JobManager(configuration);
    }
}
