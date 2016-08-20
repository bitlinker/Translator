package com.example.bitlinker.translator.sqllitedb;

import android.support.annotation.NonNull;

import com.example.bitlinker.translator.model.TranslatedText;

/**
 * Created by bitlinker on 20.08.2016.
 */
public class TranslationsTable {
    public static final String TABLE = "translations";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_ORIGINAL_TEXT= "original_text";
    public static final String COLUMN_TRANSLATED_TEXT = "translated_text";
    public static final String COLUMN_LANGUAGE = "language";

    private TranslationsTable() {
    }

    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ORIGINAL_TEXT + " TEXT NOT NULL, "
                + COLUMN_TRANSLATED_TEXT + " TEXT NOT NULL, "
                + COLUMN_LANGUAGE + " TEXT NOT NULL"
                + ");";
    }

    public static String getDropTableQuery() {
        return "DROP TABLE " + TABLE + ";";
    }
}
