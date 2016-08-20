package com.example.bitlinker.translator.translateapi.yandex.exceptions;

import com.example.bitlinker.translator.translateapi.TranslateException;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class YandexTranslateException extends TranslateException {
    public YandexTranslateException(String detailMessage) {
        super(detailMessage);
    }

    public YandexTranslateException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
