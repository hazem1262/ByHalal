package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CategoryRestaurents {
	@POST(ConstantsHelper.Category_Restaurents)
	@FormUrlEncoded
	Call<ArrayList<Restaurant>> getCategoryRestaurents(@Field("listId") int listId);
}
