package com.example.bitlinker.translator.daoapi.sqllitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class SqlLiteDb extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    private static final String DB_NAME = "translations.db";

    public SqlLiteDb(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TranslationsTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TranslationsTable.getDropTableQuery());
        onCreate(db);
    }
}
