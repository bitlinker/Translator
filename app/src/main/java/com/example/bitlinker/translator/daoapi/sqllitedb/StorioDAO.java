package com.example.bitlinker.translator.daoapi.sqllitedb;

import android.content.Context;

import com.example.bitlinker.translator.model.TranslatedText;
import com.example.bitlinker.translator.translateapi.TranslateException;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import rx.Completable;
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

    public Completable put(TranslatedTextEntry entry) {
        return mStorio
                .put()
                .object(entry)
                .prepare()
                .asRxCompletable();
    }

    public Completable delete(TranslatedTextEntry entry) {
        return mStorio
                .delete()
                .object(entry)
                .prepare()
                .asRxCompletable();
    }

    public List<TranslatedTextEntry> getAll() {
        return mStorio
                .get()
                .listOfObjects(TranslatedTextEntry.class)
                .withQuery(Query.builder()
                        .table(TranslationsTable.TABLE)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    public Single<List<TranslatedTextEntry>> getByTextOrTranslation(String value) {
        final String wildcard = "%" + value + "%";
        return mStorio
                .get()
                .listOfObjects(TranslatedTextEntry.class)
                .withQuery(Query.builder()
                        .table(TranslationsTable.TABLE)
                        .where("LOWER(" + TranslationsTable.COLUMN_ORIGINAL_TEXT + ") LIKE LOWER(?) OR LOWER(" + TranslationsTable.COLUMN_TRANSLATED_TEXT + ") LIKE LOWER(?)")
                        .whereArgs(new ArrayList<Object>() {{add(wildcard); add(wildcard);}})
                        .build())
                .prepare()
                .asRxSingle();
    }

    @Override
    public Single<List<TranslatedText>> getEntriesList(String filter) {
        return Single.fromCallable(() -> new ArrayList<TranslatedText>(2)); // TODO
    }

    @Override
    public Single<Boolean> addEntry(TranslatedText entry) {
        return mStorio
                .put()
                .object(TranslatedTextEntry.fromTranslatedText(entry))
                .prepare()
                .asRxSingle()
                .map(putResult -> {
                    Boolean res = putResult.wasInserted() || putResult.wasUpdated();
                    return res;
                });
    }
}
