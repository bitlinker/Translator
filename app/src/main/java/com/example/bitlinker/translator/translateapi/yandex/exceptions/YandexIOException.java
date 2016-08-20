package com.example.bitlinker.translator.translateapi.yandex.exceptions;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class YandexIOException extends YandexTranslateException {
    public YandexIOException(Throwable throwable) {
        super("IO exception", throwable);
    }
}
