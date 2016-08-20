package com.example.bitlinker.translator.translateapi;

import com.example.bitlinker.translator.model.TranslatedText;

/**
 * Created by bitlinker on 20.08.2016.
 */
public interface TranslateApi {
    TranslatedText translate(String text, String destLang) throws TranslateException;
}
