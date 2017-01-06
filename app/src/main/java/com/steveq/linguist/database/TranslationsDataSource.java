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
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        ContentValues wordValues = new ContentValues();
        wordValues.put(mTranslationDatabaseHelper.COLUMN_WORDS_WORD, translationResponse.getPhrase());
        wordValues.put(mTranslationDatabaseHelper.COLUMN_WORDS_TRANSLATIONS_FK, getTranslationId(translationResponse.getTuc().get(0).getPhrase().getText()));
        db.insert(mTranslationDatabaseHelper.WORDS_TABLE,
                null,
                wordValues);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private int getTranslationId(String text) {
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        int transId = -1;
        Cursor cursor = db.rawQuery("SELECT * FROM " + mTranslationDatabaseHelper.TRANSLATIONS_TABLE +
                " WHERE " + mTranslationDatabaseHelper.COLUMN_TRANSLATIONS_WORD +
                "=" + "\"" + text + "\"" + ";", null);
        if(cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(BaseColumns._ID);
            transId = cursor.getInt(index);
            cursor.close();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return transId;
    }

    private int getLanguageId(String language){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        int lanId = -1;
        Cursor cursor = db.rawQuery("SELECT * FROM " + mTranslationDatabaseHelper.LANGUAGES_TABLE +
                                    " WHERE " + mTranslationDatabaseHelper.COLUMN_LANGUAGES_LANGUAGE +
                                    "=" + "\"" + language + "\"" + ";", null);
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
