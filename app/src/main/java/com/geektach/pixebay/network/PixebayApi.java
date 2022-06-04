package com.geektach.pixebay.network;

import com.geektach.pixebay.network.model.PixabayModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixebayApi {

    @GET("api/")
    Call<PixabayModel> getImages(@Query("key") String key,
                                 @Query("q") String query,
                                 @Query("page") int page,
                                 @Query("per_page") int parePage);
}
