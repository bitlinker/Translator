package com.example.bitlinker.translator.domain;

import android.util.Log;

import com.example.bitlinker.translator.App;
import com.example.bitlinker.translator.daoapi.sqllitedb.IDaoApi;
import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.ITranslateApi;
import com.example.bitlinker.translator.translateapi.TranslateException;

import java.util.IllegalFormatException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.Subscriber;
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

//    public void translateAndAddText(String text) throws TranslateException {
//        //TranslatedText translatedText = mTranslateApi.translate(text, DEST_LANG);
//
//        // TODO: duplicated exception
//        //mDaoApi.put();
//
//
////        mDaoApi.getEntriesList("test")
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(List<TranslatedText> -> )
////        )
//    }

    @Override
    public Single<List<TranslatedText>> getTranslatedList(String filter) {
        return mDaoApi.getEntriesList(filter)
                .toObservable()
                .onErrorResumeNext(throwable -> Observable.error(
                        new IllegalArgumentException("todotodo", throwable)))
                .toSingle();
    }

    @Override
    public Single<Boolean> translateAndAddItem(String text) {
        /*Single<TranslatedText>
        TranslatedText ent
        return mDaoApi.addEntry();*/
        // TODO

        Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "Completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Error: " + e.toString());
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean translatedText) {
                //Log.e(TAG, "Next: " + translatedText.getTranslatedText());
                this.unsubscribe();
            }
        };

        Single<TranslatedText> translated = mTranslateApi.translate(text, DEST_LANG);
        translated
            .flatMap(value -> mDaoApi.addEntry(value))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);
        return null;
    }
}
