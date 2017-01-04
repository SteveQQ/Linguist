package com.steveq.linguist.Api;

import com.steveq.linguist.model.TranslateOutput;
import com.steveq.linguist.model.Translation;
import com.steveq.linguist.model.TranslationResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface TransltrAPI {
    @GET("gapi/translate")
    Call<TranslationResponse> loadTranslation(@QueryMap Map<String,String> phrase);
}
