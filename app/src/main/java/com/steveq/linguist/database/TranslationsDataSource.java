package com.steveq.linguist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.steveq.linguist.R;
import com.steveq.linguist.model.response.Phrase;
import com.steveq.linguist.model.response.TranslationResponse;

public class TranslationsDataSource {

    private TranslationDatabaseHelper mTranslationDatabaseHelper;
    private Context mContext;

    public TranslationsDataSource(Context context){
        mContext = context;
        mTranslationDatabaseHelper = new TranslationDatabaseHelper(context);
    }

    public void initDB(){
        fillLanguages();
    }

    public void fillLanguages(){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        String[] langs = mContext.getResources().getStringArray(R.array.langs);

        for(String el : langs){
            ContentValues languagesValues = new ContentValues();
            languagesValues.put(mTranslationDatabaseHelper.COLUMN_LANGUAGES_LANGUAGE, el);
            db.insert(mTranslationDatabaseHelper.LANGUAGES_TABLE,
                    null,
                    languagesValues);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void insertTranslation(Phrase phrase){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        ContentValues translationValues = new ContentValues();
        translationValues.put(mTranslationDatabaseHelper.COLUMN_TRANSLATIONS_WORD, phrase.getText());
        translationValues.put(mTranslationDatabaseHelper.COLUMN_TRANSLATIONS_LANGUAGE_FK, getLanguageId(phrase.getLanguageLong()));
        db.insert(mTranslationDatabaseHelper.TRANSLATIONS_TABLE,
                null,
                translationValues);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void insertWord(TranslationResponse translationResponse){
//        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
//        db.beginTransaction();
//
//        ContentValues wordValues = new ContentValues();
//        wordValues.put(mTranslationDatabaseHelper.COLUMN_WORDS_WORD, translationResponse.getPhrase());
//        wordValues.put(mTranslationDatabaseHelper.COLUMN_TRANSLATIONS_LANGUAGE_FK, getTranslationId(translationResponse.get))
//
//        db.setTransactionSuccessful();
//        db.endTransaction();
//        db.close();
    }

    public int getLanguageId(String language){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        int lanId = -1;
        Cursor cursor = db.rawQuery("SELECT * FROM " + mTranslationDatabaseHelper.LANGUAGES_TABLE +
                                    " WHERE " + mTranslationDatabaseHelper.COLUMN_LANGUAGES_LANGUAGE +
                                    "=" + "\"" + language + "\"" + ";", null);
//        Cursor cursor = db.query(mTranslationDatabaseHelper.LANGUAGES_TABLE,
//                new String[]{BaseColumns._ID, mTranslationDatabaseHelper.COLUMN_LANGUAGES_LANGUAGE},
//                mTranslationDatabaseHelper.COLUMN_LANGUAGES_LANGUAGE + "=" + language,
//                null,
//                null,
//                null,
//                null,
//                null);
        if(cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(BaseColumns._ID);
            lanId = cursor.getInt(index);
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return lanId;
    }
}
