package com.example.bitlinker.translator.translateapi;

import com.example.bitlinker.translator.model.TranslatedText;

import rx.Single;

/**
 * Created by bitlinker on 20.08.2016.
 */
public interface ITranslateApi {
    Single<TranslatedText> translate(String text, String destLang);
}
