package com.steveq.linguist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TranslationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "translations.db";
    private static final int DB_VERSION = 1;

    //ATTRIBUTES:
    //WORDS TABLE
    public static final String WORDS_TABLE = "words_table";
    public static final String COLUMN_WORDS_WORD = "word";
    public static final String COLUMN_WORDS_TRANSLATIONS_FK = "translations_fk";

    //ATTRIBUTES:
    //TRANSLATIONS TABLE
    public static final String TRANSLATIONS_TABLE = "translations_table";
    public static final String COLUMN_TRANSLATIONS_WORD = "word";
    public static final String COLUMN_TRANSLATIONS_LANGUAGE_FK = "languages_fk";

    //ATTRIBUTES:
    //LANGUAGES TABLE
    public static final String LANGUAGES_TABLE = "languages_table";
    public static final String COLUMN_LANGUAGES_LANGUAGE = "language";

    public TranslationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //DDL
    //CREATE WORD TABLE
    private static String CREATE_WORDS_TABLE =
            "CREATE TABLE " + WORDS_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORDS_WORD + " TEXT NOT NULL, " +
                    COLUMN_WORDS_TRANSLATIONS_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_WORDS_TRANSLATIONS_FK + ") REFERENCES TRANSLATIONS(_id), " +
                    "UNIQUE(" + COLUMN_WORDS_WORD + ", " + COLUMN_WORDS_TRANSLATIONS_FK + "));";

    //CREATE TRANSLATIONS TABLE
    private static String CREATE_TRANSLATIONS_TABLE =
            "CREATE TABLE " + TRANSLATIONS_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TRANSLATIONS_WORD + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_TRANSLATIONS_LANGUAGE_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_TRANSLATIONS_LANGUAGE_FK + ") REFERENCES LANGUAGES(_id));";

    //CREATE LANGUAGES TABLE
    private static String CREATE_LANGUAGES_TABLE =
            "CREATE TABLE " + LANGUAGES_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LANGUAGES_LANGUAGE + " TEXT NOT NULL UNIQUE);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDS_TABLE);
        db.execSQL(CREATE_TRANSLATIONS_TABLE);
        db.execSQL(CREATE_LANGUAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
