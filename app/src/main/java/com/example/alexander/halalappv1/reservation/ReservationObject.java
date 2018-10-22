package com.example.alexander.halalappv1.reservation;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.alexander.halalappv1.model.newModels.reservation.Reservation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReservationObject {

    @SerializedName("upcomingBookings")
    @Expose
    private List<Reservation> upComingReservations = null;

    @SerializedName("prevBookings")
    @Expose
    private List<Reservation> previousReservations = null;

    public List<Reservation> getUpComingReservations() {
        return upComingReservations;
    }

    public void setUpComingReservations(List<Reservation> upComingReservations) {
        this.upComingReservations = upComingReservations;
    }

    public List<Reservation> getPreviousReservations() {
        return previousReservations;
    }

    public void setPreviousReservations(List<Reservation> previousReservations) {
        this.previousReservations = previousReservations;
    }




    public ReservationObject() {
    }

}
