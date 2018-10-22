package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.newModels.RestaurantProfile;
import com.example.alexander.halalappv1.model.newModels.workdays.WorkingHoursResponse;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WorkingHours {
    @POST(ConstantsHelper.RESTAURENT_WORKING_HOURS)
    @FormUrlEncoded
    Call<WorkingHoursResponse> getRestaurentWorkingHours(@Field("restaurantId") int restaurantId);

}
