package com.steveq.linguist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.steveq.linguist.R;
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

}
