package com.example.bitlinker.translator.translateapi.yandex.exceptions;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class YandexQuotaExceededException extends YandexTranslateException {
    public YandexQuotaExceededException() {
        super("Quota exceeded");
    }
}
