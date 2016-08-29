package com.example.bitlinker.translator.domain;

import com.example.bitlinker.translator.daoapi.sqllitedb.IDaoApi;
import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.ITranslateApi;

import java.util.List;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bitlinker on 22.08.2016.
 */

public class MainInteractor implements IMainInteractor {
    private static final String TAG = "MainInteractor";
    private ITranslateApi mTranslateApi;
    private IDaoApi mDaoApi;

    public MainInteractor(ITranslateApi translateApi, IDaoApi daoApi) {
        mTranslateApi = translateApi;
        mDaoApi = daoApi;
    }

    // TODO: language abstraction
    public static final String DEST_LANG = "ru";

    @Override
    public Single<List<TranslatedText>> getTranslatedItems(String filter) {
        // TODO: errors processing
        // TODO: separate filtered and unfiltered states
        return mDaoApi.getEntriesList(filter)
                .toObservable()
                /*.onErrorResumeNext(throwable -> Observable.error(
                        new IllegalArgumentException("todotodo", throwable)))*/
                .toSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<TranslatedText> translateAndAddItem(String text) {
        Single<TranslatedText> translated = mTranslateApi.translate(text, DEST_LANG);
        return translated
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap(value -> mDaoApi.addEntry(value))
            .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> deleteItem(TranslatedText entry) {
        return mDaoApi.deleteEntry(entry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
