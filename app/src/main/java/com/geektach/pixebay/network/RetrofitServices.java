package com.geektach.pixebay.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServices {

    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://pixabay.com/")
                    .addConverterFactory(GsonConverterFactory
                    .create())
                    .build();

    public PixebayApi getApi() {
        return retrofit.create(PixebayApi.class);
    }

}
