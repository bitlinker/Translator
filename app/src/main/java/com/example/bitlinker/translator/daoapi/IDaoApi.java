package com.example.bitlinker.translator.daoapi;

import android.support.annotation.NonNull;

import com.example.bitlinker.translator.model.TranslatedText;

import java.io.Closeable;
import java.util.List;

import rx.Single;

/**
 * Created by bitlinker on 22.08.2016.
 */

public interface IDaoApi extends Closeable {
    @NonNull Single<List<TranslatedText>> getEntriesList();
    @NonNull Single<List<TranslatedText>> getEntriesListFiltered(@NonNull String filter);

    @NonNull Single<TranslatedText> addEntry(@NonNull TranslatedText entry);
    @NonNull Single<Boolean> deleteEntry(@NonNull TranslatedText entry);
}
