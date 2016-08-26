package com.example.bitlinker.translator.domain;

import com.example.bitlinker.translator.model.TranslatedText;

import java.util.List;

import rx.Single;

/**
 * Created by bitlinker on 22.08.2016.
 */

public interface IMainInteractor {
    Single<List<TranslatedText>> getTranslatedList(String filter);
    Single<Boolean> translateAndAddItem(String text);
}
