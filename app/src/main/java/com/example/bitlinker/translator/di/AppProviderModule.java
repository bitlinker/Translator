package com.example.bitlinker.translator.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.bitlinker.translator.App;
import com.example.bitlinker.translator.sqllitedb.StorioDAO;
import com.example.bitlinker.translator.translateapi.TranslateApi;
import com.example.bitlinker.translator.translateapi.yandex.YandexTranslateApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bitlinker on 20.08.2016.
 */

@Module
@Singleton
public class AppProviderModule {
    @Provides
    @NonNull
    public TranslateApi provideTranslateApi() {
        return new YandexTranslateApi();
    }

    // TODO: interface here
    @Provides
    @NonNull
    public StorioDAO provideDAO(Context context) {
        return new StorioDAO(context);
    }

    @Provides
    @NonNull
    public Context provideContext() {
        return App.getContext();
    }
}