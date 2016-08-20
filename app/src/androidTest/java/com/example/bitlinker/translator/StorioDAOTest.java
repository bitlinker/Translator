package com.example.bitlinker.translator;

import android.test.AndroidTestCase;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.sqllitedb.StorioDAO;
import com.example.bitlinker.translator.sqllitedb.TranslatedTextEntry;

import org.junit.Test;

import java.util.List;


/**
 * Created by bitlinker on 20.08.2016.
 */
public class StorioDAOTest extends AndroidTestCase {
    @Test
    public void testGetAll() throws Exception {
        StorioDAO dao = new StorioDAO(getContext());
        try {
            List<TranslatedTextEntry> items = dao.getAll();
            assertNotNull(items);
        } finally {
            dao.close();
        }
    }

    @Test
    public void testPutDelete() throws Exception {
        StorioDAO dao = new StorioDAO(getContext());
        try {
            List<TranslatedTextEntry> items = dao.getAll();
            assertNotNull(items);
            int prevSize = items.size();

            TranslatedText text = new TranslatedText("Hello", "Привет", "ru");
            TranslatedTextEntry entry = TranslatedTextEntry.fromTranslatedText(text);
            dao.put(entry);

            items = dao.getAll();
            assertNotNull(items);
            assertTrue(items.size() == (prevSize + 1));

            TranslatedTextEntry foundEntry = dao.getByTextOrTranslation("при");
            assertNotNull(foundEntry);

            dao.delete(foundEntry);

            items = dao.getAll();
            assertNotNull(items);
            assertTrue(items.size() == prevSize);
        } finally {
            dao.close();
        }
    }
}