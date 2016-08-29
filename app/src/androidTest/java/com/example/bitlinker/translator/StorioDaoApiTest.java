package com.example.bitlinker.translator;

import android.test.AndroidTestCase;

import com.example.bitlinker.translator.daoapi.IDaoApi;
import com.example.bitlinker.translator.daoapi.sqllitedb.StorioDaoApi;
import com.example.bitlinker.translator.model.TranslatedText;

import org.junit.Test;

import java.util.List;

import rx.Single;


/**
 * Created by bitlinker on 20.08.2016.
 */
public class StorioDaoApiTest extends AndroidTestCase {

    private void addTestEntry(IDaoApi daoApi) {
        TranslatedText text = new TranslatedText("hello", "привет", "en");
        Single<TranslatedText> items = daoApi.addEntry(text);
        assertNotNull(items.toBlocking().value());
    }

    @Test
    public void testPut() throws Exception {

        StorioDaoApi daoApi = new StorioDaoApi(getContext());
        try {
            addTestEntry(daoApi);
        } finally {
            daoApi.close();
        }
    }

    @Test
    public void testGetEntriesList() throws Exception {
        StorioDaoApi daoApi = new StorioDaoApi(getContext());
        try {
            addTestEntry(daoApi);
            Single<List<TranslatedText>> itemsSingle = daoApi.getEntriesList();
            List<TranslatedText> items = itemsSingle.toBlocking().value();
            assert(items.size() > 0);
        } finally {
            daoApi.close();
        }
    }

    @Test
    public void testGetEntriesListFiltered() throws Exception {
        StorioDaoApi daoApi = new StorioDaoApi(getContext());
        try {
            addTestEntry(daoApi);
            Single<List<TranslatedText>> itemsSingle = daoApi.getEntriesListFiltered("прив");
            List<TranslatedText> items = itemsSingle.toBlocking().value();
            assertNotNull(items.size() > 1);
        } finally {
            daoApi.close();
        }
    }

    @Test
    public void testDelete() throws Exception {
        StorioDaoApi daoApi = new StorioDaoApi(getContext());
        try {
            addTestEntry(daoApi);
            Single<List<TranslatedText>> itemsSingle = daoApi.getEntriesList();
            List<TranslatedText> items = itemsSingle.toBlocking().value();
            assertNotNull(items);
            int prevSize = items.size();

            Single<Boolean> resSingle = daoApi.deleteEntry(items.get(0));
            Boolean res = resSingle.toBlocking().value();
            assert(res);

            itemsSingle = daoApi.getEntriesList();
            items = itemsSingle.toBlocking().value();
            assertNotNull(items);
            assertTrue(items.size() == (prevSize - 1));
        } finally {
            daoApi.close();
        }
    }
}