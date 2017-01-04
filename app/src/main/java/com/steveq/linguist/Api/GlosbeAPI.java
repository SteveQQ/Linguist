package com.steveq.linguist.Api;

import com.steveq.linguist.model.response.TranslationResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


public interface GlosbeAPI {
    @GET("gapi/translate")
    Call<TranslationResponse> loadTranslation(@QueryMap Map<String,String> phrase);
}
