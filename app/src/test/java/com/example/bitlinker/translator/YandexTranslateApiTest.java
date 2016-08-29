package com.example.bitlinker.translator;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.yandex.YandexTranslateApi;

import org.junit.Test;

import rx.Single;

import static org.junit.Assert.assertEquals;

/**
 * Created by bitlinker on 20.08.2016.
 */

public class YandexTranslateApiTest {
    @Test
    public void testTranslate() throws Exception {
        YandexTranslateApi api = new YandexTranslateApi();
        Single<TranslatedText> translated = api.translate("Hello", "ru");
        TranslatedText translatedText = translated.toBlocking().value();
        assertEquals(translatedText.getTranslatedText(), "Привет");
    }
}
