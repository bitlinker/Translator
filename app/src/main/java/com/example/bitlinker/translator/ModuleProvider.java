package com.example.bitlinker.translator;

import android.support.annotation.NonNull;

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
public class ModuleProvider {

    @Provides
    @NonNull
    public TranslateApi provideTranslationApi() {
        return new YandexTranslateApi();
    }
}
