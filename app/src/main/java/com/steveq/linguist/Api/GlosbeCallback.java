package com.steveq.linguist.Api;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.database.TranslationsDataSource;
import com.steveq.linguist.model.response.Phrase;
import com.steveq.linguist.model.response.TranslationResponse;
import com.steveq.linguist.ui.activities.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlosbeCallback implements Callback<TranslationResponse> {

    private TranslatesAdapter mAdapter;
    private MainActivity mActivity;
    private TranslationsDataSource mDataSource;

    public GlosbeCallback(TranslatesAdapter adapter, TranslationsDataSource dataSource, Activity activity){
        mAdapter = adapter;
        mActivity = (MainActivity)activity;
        mDataSource = dataSource;
    }

    @Override
    public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
        final TranslationResponse responseBody = response.body();
        final Phrase phrase = responseBody.getTuc().get(0).getPhrase();
        String translatedText = responseBody.getTuc().get(0).getPhrase().getText();
        String destLan = responseBody.getTuc().get(0).getPhrase().getLanguage();

        for (int i = 0; i < mAdapter.getOutputs().size(); i++) {
            String destLanKeep = mAdapter.getOutputs().get(i).getLanguageCropped();
            if (destLan.equals(destLanKeep)) {
                mAdapter.getOutputs().get(i).setText(translatedText);
            }
        }

        Thread insertionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long primaryWordId = mDataSource.insertWord(responseBody);
                long translationWordId = mDataSource.insertWord(phrase);
                if(primaryWordId < 0) {
                    primaryWordId = mDataSource.getWordId(responseBody.getPhrase());
                }
                if(translationWordId < 0){
                    translationWordId = mDataSource.getWordId(phrase.getText());
                }
                mDataSource.insertTranslation(primaryWordId, translationWordId);
            }
        });
        insertionThread.start();

        mAdapter.notifyDataSetChanged();
        mActivity.mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Call<TranslationResponse> call, Throwable t) {
        Toast.makeText(mActivity, t.getMessage(), Toast.LENGTH_LONG).show();
        mActivity.mProgressBar.setVisibility(View.GONE);
    }
}
