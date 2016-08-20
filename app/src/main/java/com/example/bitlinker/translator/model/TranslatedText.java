package com.example.bitlinker.translator.model;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class TranslatedText {
    private final String mOriginalText;
    private final String mTranslatedText;
    private final String mLanguage;

    public TranslatedText(String originalText, String translatedText, String language) {
        mOriginalText = originalText;
        mTranslatedText = translatedText;
        mLanguage = language;
    }

    public String getOriginalText() {
        return mOriginalText;
    }

    public String getTranslatedText() {
        return mTranslatedText;
    }

    public String getLanguage() {
        return mLanguage;
    }
}
