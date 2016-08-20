package com.example.bitlinker.translator.translateapi;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class TranslateException extends Exception {
    public TranslateException(String detailMessage) {
        super(detailMessage);
    }

    public TranslateException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
