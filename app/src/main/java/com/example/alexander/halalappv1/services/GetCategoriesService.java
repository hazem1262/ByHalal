package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetCategoriesService {
    @GET(ConstantsHelper.GET_ALL_CATEGORIES_API)
    Call<JsonArray> getCategories();
}
