package com.example.bitlinker.translator.sqllitedb;

import android.content.Context;

import com.pushtorefresh.storio.StorIOException;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class StorioDAO {
    private StorIOSQLite mStorio;

    public StorioDAO(Context context) {
        SqlLiteDb helper = new SqlLiteDb(context, SqlLiteDb.DB_VERSION);

        mStorio = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(helper)
                .addTypeMapping(TranslatedTextEntry.class, new TranslatedTextEntrySQLiteTypeMapping())
                .build();
    }

    public void close() throws IOException {
        if (mStorio != null) {
            mStorio.close();
            mStorio = null;
        }
    }

    public void put(TranslatedTextEntry entry) {
        mStorio
                .put()
                .object(entry)
                .prepare()
                .executeAsBlocking();
    }

    public void delete(TranslatedTextEntry entry) {
        mStorio
                .delete()
                .object(entry)
                .prepare()
                .executeAsBlocking();
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

    // TODO: return list here?
    public TranslatedTextEntry getByTextOrTranslation(String value) {
        final String wildcard = "%" + value + "%";
        return mStorio
                .get()
                .object(TranslatedTextEntry.class)
                .withQuery(Query.builder()
                        .table(TranslationsTable.TABLE)
                        .where("LOWER(" + TranslationsTable.COLUMN_ORIGINAL_TEXT + ") LIKE LOWER(?) OR LOWER(" + TranslationsTable.COLUMN_TRANSLATED_TEXT + ") LIKE LOWER(?)")
                        .whereArgs(new ArrayList<Object>() {{add(wildcard); add(wildcard);}})
                        .build())
                .prepare()
                .executeAsBlocking();
    }
}
