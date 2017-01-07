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

    private void fillLanguages(){
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

    public synchronized long insertWord(TranslationResponse translationResponse){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        long id;

        ContentValues wordValues = new ContentValues();
        wordValues.put(mTranslationDatabaseHelper.COLUMN_WORDS_WORD, translationResponse.getPhrase());
        wordValues.put(mTranslationDatabaseHelper.COLUMN_WORDS_LANGUAGE_FK, getLanguageId(translationResponse.getFrom()));
        id = db.insert(mTranslationDatabaseHelper.WORDS_TABLE,
                null,
                wordValues);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return id;
    }

    public synchronized  long insertWord(Phrase phraseObj){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        long id;
        ContentValues wordValues = new ContentValues();
        wordValues.put(mTranslationDatabaseHelper.COLUMN_WORDS_WORD, phraseObj.getText());
        wordValues.put(mTranslationDatabaseHelper.COLUMN_WORDS_LANGUAGE_FK, getLanguageId(phraseObj.getLanguageLong()));
        id = db.insert(mTranslationDatabaseHelper.WORDS_TABLE,
                null,
                wordValues);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return id;
    }

    public synchronized  void insertTranslation(long idPrimaryWord, long idTranslationWord){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        ContentValues translationValues = new ContentValues();
        translationValues.put(mTranslationDatabaseHelper.COLUMN_TRANSLATIONS_WORD_FK, idPrimaryWord);
        translationValues.put(mTranslationDatabaseHelper.COLUMN_TRANSLATIONS_TRANSLATION_FK, idTranslationWord);
        db.insert(mTranslationDatabaseHelper.TRANSLATIONS_TABLE,
                null,
                translationValues);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public synchronized  long getWordId(String word){
        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();

        long id = -1;

        Cursor cursor = db.rawQuery("select * from " + mTranslationDatabaseHelper.WORDS_TABLE +
                                    " where " + mTranslationDatabaseHelper.COLUMN_WORDS_WORD +
                                    " = " +
                                    "\"" + word + "\" ;", null);
        if(cursor.moveToFirst()){
            id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        }


        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return id;
    }

    public String getTranslation(String word, String lang){

        SQLiteDatabase db = mTranslationDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        String result = null;
        Cursor cursor = db.rawQuery("SELECT * " +
                                    "FROM ("+
                                        "SELECT t2.prim_word AS word, t2.tran_word AS translation, languages_table.language AS language " +
                                        "FROM ( " +
                                            "SELECT t.primary_word AS prim_word, words_table.word AS tran_word, words_table.language_fk AS lan_fk " +
                                            "FROM (" +
                                                        "(" +
                                                        "SELECT words_table.word AS primary_word, translations_table.translation_fk " +
                                                         "FROM translations_table " +
                                                        "INNER JOIN words_table " +
                                                        "ON words_table._id = translations_table.word_fk " +
                                                        ") AS t " +
                                                    "INNER JOIN words_table " +
                                                    "ON words_table._id = t.translation_fk" +
                                                    ") AS t2 " +
                                            "INNER JOIN languages_table " +
                                            "ON t2.language_fk = languages_table._id " +
                                        ") AS t2 " +
                                        "INNER JOIN languages_table " +
                                        "ON t2.lan_fk = languages_table._id " +
                                    ") AS t3 " +
                                    "WHERE t3.word = " + "\"" + word + "\";",
                                    null);

        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex("language")).equals(lang)){
                    result = cursor.getString(cursor.getColumnIndex("translation"));
                }
            } while (cursor.moveToNext());
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return result;
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
