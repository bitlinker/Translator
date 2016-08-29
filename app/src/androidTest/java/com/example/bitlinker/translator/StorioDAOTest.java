package com.example.bitlinker.translator;

import android.test.AndroidTestCase;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.daoapi.sqllitedb.StorioDAO;
import com.example.bitlinker.translator.daoapi.sqllitedb.TranslatedTextEntry;

import org.junit.Test;

import java.util.List;

import rx.Single;


/**
 * Created by bitlinker on 20.08.2016.
 */
public class StorioDAOTest extends AndroidTestCase {
    @Test
    public void testGetAll() throws Exception {
        StorioDAO dao = new StorioDAO(getContext());
        try {
            Single<List<TranslatedText>> items = dao.getEntriesList("");
            assertNotNull(items.toBlocking().value());
        } finally {
            dao.close();
        }
    }


    // TODO
//    @Test
//    public void testPutDelete() throws Exception {
//        StorioDAO dao = new StorioDAO(getContext());
//        try {
//            List<TranslatedTextEntry> items = dao.getAll();
//            assertNotNull(items);
//            int prevSize = items.size();
//
//            TranslatedText text = new TranslatedText(-1, "Hello", "Привет", "ru");
//            TranslatedTextEntry entry = TranslatedTextEntry.fromTranslatedText(text);
//            dao.put(entry);
//
//            items = dao.getAll();
//            assertNotNull(items);
//            assertTrue(items.size() == (prevSize + 1));
//
////            // TODO
////            TranslatedTextEntry foundEntry = dao.getByTextOrTranslation("при");
////            assertNotNull(foundEntry);
////
////            dao.delete(foundEntry);
////
////            items = dao.getAll();
////            assertNotNull(items);
////            assertTrue(items.size() == prevSize);
//        } finally {
//            dao.close();
//        }
//    }
}