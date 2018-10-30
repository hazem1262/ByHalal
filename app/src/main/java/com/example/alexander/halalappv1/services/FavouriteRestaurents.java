package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Hazem on 9/14/2018.
 */

public interface FavouriteRestaurents {
	// My Favourites
	@POST(ConstantsHelper.MY_FAVOURITES_FEED)
	@FormUrlEncoded
	Call<ArrayList<Restaurant>> getFavouriteRestaurants(@Field("userId") int userId);
}
