package com.example.bitlinker.translator.domain;

import android.text.TextUtils;

import com.example.bitlinker.translator.daoapi.IDaoApi;
import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.ITranslateApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Single;
import rx.schedulers.Schedulers;

/**
 * Created by bitlinker on 22.08.2016.
 */

public class MainInteractor implements IMainInteractor {
    public static final String DEST_LANG = "ru";

    private ITranslateApi mTranslateApi;
    private IDaoApi mDaoApi;

    public MainInteractor(ITranslateApi translateApi, IDaoApi daoApi) {
        mTranslateApi = translateApi;
        mDaoApi = daoApi;
    }



    @Override
    public Single<List<TranslatedText>> getTranslatedItems(String filter) {
        // TODO: errors processing

        Single<List<TranslatedText>> itemsSingle;
        if (TextUtils.isEmpty(filter)) {
            itemsSingle = mDaoApi.getEntriesList();
        } else {
            itemsSingle = mDaoApi.getEntriesListFiltered(filter);
        }

        return itemsSingle
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<TranslatedText> translateAndAddItem(String text) {
        Single<TranslatedText> translated = mTranslateApi.translate(text, DEST_LANG);
        return translated
            //.delay(3000, TimeUnit.MILLISECONDS) // TODO: DBG
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap(value -> mDaoApi.addEntry(value));
    }

    @Override
    public Single<Boolean> deleteItem(TranslatedText entry) {
        return mDaoApi.deleteEntry(entry)
                .subscribeOn(Schedulers.io());
    }
}
