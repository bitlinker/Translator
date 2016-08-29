package com.example.bitlinker.translator.model;

import android.content.Context;

import com.example.bitlinker.translator.R;

/**
 * Created by bitlinker on 29.08.2016.
 */
public class TranslationError {

    public TranslationError() {
    }

    public String getMessage(Context context) {
        // TODO
        return context.getString(R.string.error_cant_translate);
    }
}
