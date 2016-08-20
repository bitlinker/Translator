package com.example.bitlinker.translator;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.yandex.YandexTranslateApi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by bitlinker on 20.08.2016.
 */

public class YandexTranslateApiTest {
    @Test
    public void TranslateWorks() throws Exception {
        YandexTranslateApi api = new YandexTranslateApi();
        TranslatedText translated = api.translate("Hello", "ru");
        assertEquals(translated.getTranslatedText(), "Привет");
    }
}
