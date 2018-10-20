package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.newModels.menues.MenuResponse;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MenuResponseWebService {
    @POST(ConstantsHelper.RESTAURENT_MENU)
    @FormUrlEncoded
    Call<ArrayList<MenuResponse>> getRestaurantMenu(@Field("restaurantId") int restaurantId);

}
