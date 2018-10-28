package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.google.gson.JsonArray;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SearchRestaurantService {
    @POST(ConstantsHelper.SEARCH_RESTAURANT)
    @FormUrlEncoded
    Call<JsonArray> getSearchResponse(@Field("keyword") String keyword);
}
