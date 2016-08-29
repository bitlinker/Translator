package com.example.bitlinker.translator.daoapi;

import com.example.bitlinker.translator.model.TranslatedText;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import rx.Single;

/**
 * Created by bitlinker on 22.08.2016.
 */

public interface IDaoApi extends Closeable {
    Single<List<TranslatedText>> getEntriesList();
    Single<List<TranslatedText>> getEntriesListFiltered(String filter);

    Single<TranslatedText> addEntry(TranslatedText entry);
    Single<Boolean> deleteEntry(TranslatedText entry);
}
