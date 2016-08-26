package com.example.bitlinker.translator.di.app;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bitlinker on 20.08.2016.
 */

@Module
@Singleton
public class AppModule {
    private final Context mAppContext;

    public AppModule(@NonNull Context context) {
        mAppContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mAppContext;
    }
}