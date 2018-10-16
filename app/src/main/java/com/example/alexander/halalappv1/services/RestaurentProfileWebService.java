package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.newModels.RestaurantProfile;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestaurentProfileWebService {
	@POST(ConstantsHelper.RESTAURENT_DETAILS)
	@FormUrlEncoded
	Call<RestaurantProfile> getRestaurentProfile(@Field("restaurantId") int restaurantId,
												 @Field("userId") int userId);
}
