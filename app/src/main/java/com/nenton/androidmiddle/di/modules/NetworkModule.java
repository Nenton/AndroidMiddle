package com.nenton.androidmiddle.di.modules;

import com.google.gson.Gson;
import com.nenton.androidmiddle.data.network.RestService;
import com.nenton.androidmiddle.utils.AppConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(){
        return createClient();
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(AppConfig.MAX_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(AppConfig.MAX_READ_TIMEOUT,TimeUnit.MILLISECONDS)
                .writeTimeout(AppConfig.MAX_WRITE_TIMEOUT,TimeUnit.MILLISECONDS)
                .build();
        // TODO: 04.11.2016 add interceptors
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(){
        return createRetrofit();
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(createConverterFactory())
                .build();
    }

    private Converter.Factory createConverterFactory() {
        return GsonConverterFactory.create(new Gson());
    }

    @Provides
    @Singleton
    RestService provideRestService(Retrofit retrofit){
        return retrofit.create(RestService.class);
    }
}
