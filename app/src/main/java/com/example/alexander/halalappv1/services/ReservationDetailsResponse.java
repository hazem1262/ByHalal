package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.newModels.RestaurantsList;
import com.example.alexander.halalappv1.model.newModels.reservation.details.ReservationDetailsAllResponse;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ReservationDetailsResponse {
    // Home Page
    @POST(ConstantsHelper.RESERVATION_DETAILS)
    @FormUrlEncoded
    Call<ReservationDetailsAllResponse> getReservationDetails(@Field("reservationId") int reservationId);
}
