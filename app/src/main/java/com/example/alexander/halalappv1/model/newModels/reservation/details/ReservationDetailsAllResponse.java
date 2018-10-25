
package com.example.alexander.halalappv1.model.newModels.reservation.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationDetailsAllResponse {

    @SerializedName("reservation")
    @Expose
    private ReservationDetails reservation;

    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;

    public ReservationDetails getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDetails reservation) {
        this.reservation = reservation;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
