package com.example.bitlinker.translator.daoapi.sqllitedb;

import android.content.Context;
import android.text.TextUtils;

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
public class StorioDAO implements IDaoApi {
    private StorIOSQLite mStorio;

    public StorioDAO(Context context) {
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

    private Query getQuery(String filter) {
        Query query;
        if (TextUtils.isEmpty(filter)) {
            query = Query.builder()
                    .table(TranslationsTable.TABLE)
                    .build();
        } else {
            final String wildcard = "%" + filter + "%";
            query = Query.builder()
                    .table(TranslationsTable.TABLE)
                    .where("LOWER(" + TranslationsTable.COLUMN_ORIGINAL_TEXT + ") LIKE LOWER(?) OR LOWER(" + TranslationsTable.COLUMN_TRANSLATED_TEXT + ") LIKE LOWER(?)")
                    .whereArgs(new ArrayList<Object>() {{add(wildcard); add(wildcard);}})
                    .build();
        }
        return query;
    }

    @Override
    public Single<List<TranslatedText>> getEntriesList(String filter) {
        return mStorio
                .get()
                .listOfObjects(TranslatedTextEntry.class)
                .withQuery(getQuery(filter))
                .prepare()
                .asRxSingle()
                .map(value-> mapList(value));
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
                    return deleteResult.numberOfRowsDeleted() > 0;
                });
    }
}
