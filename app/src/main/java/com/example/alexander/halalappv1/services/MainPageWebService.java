package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.modifiedmodels.RestaurantsList1;
import com.example.alexander.halalappv1.model.newModels.RestaurantsList;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MainPageWebService {
	// Home Page
	@POST(ConstantsHelper.NEW_MAIN_PAGE)
	@FormUrlEncoded
	Call<RestaurantsList> getTablesList(@Field("userId") int userId,
										@Field("latitude") double latitude,
										@Field("longitude") double longitude);
}
