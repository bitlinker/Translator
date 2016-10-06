package com.example.bitlinker.translator.daoapi.sqllitedb;

import android.content.Context;

import com.example.bitlinker.translator.daoapi.IDaoApi;
import com.example.bitlinker.translator.model.TranslatedText;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Single;
import rx.exceptions.OnErrorThrowable;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class StorioDaoApi implements IDaoApi {
    private StorIOSQLite mStorio;

    public StorioDaoApi(Context context) {
        SqlLiteDb helper = new SqlLiteDb(context, SqlLiteDb.DB_VERSION);

        mStorio = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(helper)
                .addTypeMapping(TranslatedTextEntry.class, new TranslatedTextEntrySQLiteTypeMapping())
                .build();
    }

    @Override
    public void close() throws IOException {
        if (mStorio != null) {
            mStorio.close();
            mStorio = null;
        }
    }

    private List<TranslatedText> mapList(List<TranslatedTextEntry> entryList) {
        List<TranslatedText> textList = new ArrayList<>(entryList.size());
        for (TranslatedTextEntry entry : entryList) {
            textList.add(entry.toTranslatedText());
        }
        return textList;
    }

    private Single<List<TranslatedText>> getEntriesListWithQuery(Query query) {
        return mStorio
                .get()
                .listOfObjects(TranslatedTextEntry.class)
                .withQuery(query)
                .prepare()
                .asRxSingle()
                .map(value-> mapList(value));
    }

    @Override
    public Single<List<TranslatedText>> getEntriesList() {
        Query query = Query.builder()
                .table(TranslationsTable.TABLE)
                .orderBy(TranslationsTable.COLUMN_ID + " DESC")
                .build();

        return getEntriesListWithQuery(query);
    }

    public Single<List<TranslatedText>> getEntriesListFiltered(String filter) {
        final String wildcard = "%" + filter + "%";
        Query query = Query.builder()
                .table(TranslationsTable.TABLE)
                .where("LOWER(" + TranslationsTable.COLUMN_ORIGINAL_TEXT + ") LIKE LOWER(?) OR LOWER(" + TranslationsTable.COLUMN_TRANSLATED_TEXT + ") LIKE LOWER(?)")
                .whereArgs(new ArrayList<Object>() {{add(wildcard); add(wildcard);}})
                .orderBy(TranslationsTable.COLUMN_ID + " DESC")
                .build();
        return getEntriesListWithQuery(query);
    }

    @Override
    public Single<TranslatedText> addEntry(TranslatedText entry) {
        return mStorio
                .put()
                .object(TranslatedTextEntry.fromTranslatedText(entry))
                .prepare()
                .asRxSingle()
                .map(putResult -> {
                    if (!putResult.wasInserted() && !putResult.wasUpdated()) {
                        throw OnErrorThrowable.from(new IOException("Can't add entry to DB"));
                    }
                    return entry;
                });
    }

    @Override
    public Single<Boolean> deleteEntry(TranslatedText entry) {
        return mStorio
                .delete()
                .object(TranslatedTextEntry.fromTranslatedText(entry))
                .prepare()
                .asRxSingle()
                .map(deleteResult -> {
                    if (deleteResult.numberOfRowsDeleted() == 0) {
                        throw OnErrorThrowable.from(new IOException("Can't delete entry from DB"));
                    }
                    return true;
                });
    }
}
