package com.steveq.linguist.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class TranslationsDataSource {

    private TranslationDatabaseHelper mTranslationDatabaseHelper;
    private Context mContext;

    public TranslationsDataSource(Context context){
        mContext = context;
        mTranslationDatabaseHelper = new TranslationDatabaseHelper(context);
    }

    public void initDB(){
        mTranslationDatabaseHelper.getReadableDatabase();
        mTranslationDatabaseHelper.close();
    }
}
