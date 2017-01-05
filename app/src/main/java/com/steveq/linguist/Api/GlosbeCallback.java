package com.steveq.linguist.Api;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.steveq.linguist.adapters.TranslatesAdapter;
import com.steveq.linguist.model.response.TranslationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlosbeCallback implements Callback<TranslationResponse> {

    private TranslatesAdapter mAdapter;
    private Context mContext;

    public GlosbeCallback(TranslatesAdapter adapter, Context context){
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
        String translatedText = response.body().getTuc().get(0).getPhrase().getText();
        String destLan = response.body().getTuc().get(0).getPhrase().getLanguage();
        for(int i=0; i < mAdapter.getOutputs().size(); i++){
            String destLanKeep = mAdapter.getOutputs().get(i).getLanguageCropped();

            if(destLan.equals(destLanKeep)){
                mAdapter.getOutputs().get(i).setText(translatedText);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<TranslationResponse> call, Throwable t) {
        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
