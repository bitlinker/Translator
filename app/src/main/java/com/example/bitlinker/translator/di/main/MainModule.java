package com.example.bitlinker.translator.di.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.bitlinker.translator.daoapi.IDaoApi;
import com.example.bitlinker.translator.daoapi.sqllitedb.StorioDaoApi;
import com.example.bitlinker.translator.domain.IMainInteractor;
import com.example.bitlinker.translator.domain.MainInteractor;
import com.example.bitlinker.translator.translateapi.ITranslateApi;
import com.example.bitlinker.translator.translateapi.yandex.YandexTranslateApi;
import com.example.bitlinker.translator.ui.presenter.IMainPresenter;
import com.example.bitlinker.translator.ui.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bitlinker on 22.08.2016.
 */

@Module
public class MainModule {
    @Provides
    @NonNull
    public ITranslateApi provideTranslateApi() {
        return new YandexTranslateApi();
    }

    @Provides
    @NonNull
    public IDaoApi provideDaoApi(Context context) {
        return new StorioDaoApi(context);
    }

    @Provides
    @NonNull
    public IMainInteractor provideMainInteractor(ITranslateApi translateApi, IDaoApi daoApi) {
        return new MainInteractor(translateApi, daoApi);
    }

    @Provides
    @NonNull
    public IMainPresenter provideMainPresenter(IMainInteractor interactor) {
        return new MainPresenter(interactor);
    }
}
