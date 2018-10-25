
package com.example.alexander.halalappv1.model.newModels.reservation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reservation {

    @SerializedName("bookingId")
    @Expose
    private Integer bookingId;

    @SerializedName("restaurantName")
    @Expose
    private String restaurantName;

    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("guests")
    @Expose
    private Integer guests;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
