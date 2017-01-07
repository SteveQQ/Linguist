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
    public static final String COLUMN_WORDS_LANGUAGE_FK = "language_fk";

    //LANGUAGES TABLE
    public static final String LANGUAGES_TABLE = "languages_table";
    public static final String COLUMN_LANGUAGES_LANGUAGE = "language";

    //TRANSLATIONS TABLE
    public static final String TRANSLATIONS_TABLE = "translations_table";
    public static final String COLUMN_TRANSLATIONS_WORD_FK = "word_fk";
    public static final String COLUMN_TRANSLATIONS_TRANSLATION_FK = "translation_fk";

    public TranslationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //DDL
    //CREATE WORD TABLE
    private static String CREATE_WORDS_TABLE =
            "CREATE TABLE " + WORDS_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORDS_WORD + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_WORDS_LANGUAGE_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_WORDS_LANGUAGE_FK + ") REFERENCES TRANSLATIONS(_id));";

    //CREATE LANGUAGES TABLE
    private static String CREATE_LANGUAGES_TABLE =
            "CREATE TABLE " + LANGUAGES_TABLE + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LANGUAGES_LANGUAGE + " TEXT NOT NULL UNIQUE);";

    //CREATE TRANSLATIONS TABLE
    private static String CREATE_TRANSLATIONS_TABLE =
            "CREATE TABLE " + TRANSLATIONS_TABLE + " (" +
                    COLUMN_TRANSLATIONS_WORD_FK + " INTEGER NOT NULL, " +
                    COLUMN_TRANSLATIONS_TRANSLATION_FK + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_TRANSLATIONS_WORD_FK +  ") REFERENCES " + WORDS_TABLE + "(_id)," +
                    "FOREIGN KEY (" + COLUMN_TRANSLATIONS_TRANSLATION_FK +  ") REFERENCES " + WORDS_TABLE + "(_id)," +
                    "PRIMARY KEY (" + COLUMN_TRANSLATIONS_WORD_FK + ", " + COLUMN_TRANSLATIONS_TRANSLATION_FK + "));";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDS_TABLE);
        db.execSQL(CREATE_LANGUAGES_TABLE);
        db.execSQL(CREATE_TRANSLATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
