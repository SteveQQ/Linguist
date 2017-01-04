package com.steveq.linguist.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlosbeClient {
    public static final String BASE_URL ="https://glosbe.com/";
    private static Retrofit retrofit = null;

    private GlosbeClient(){}

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
