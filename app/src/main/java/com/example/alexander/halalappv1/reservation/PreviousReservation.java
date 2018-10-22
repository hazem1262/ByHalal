package com.example.alexander.halalappv1.reservation;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.alexander.halalappv1.model.Restaurant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviousReservation implements Parcelable {

    @SerializedName("bookingData")
    @Expose
    private BookingData bookingData;

    @SerializedName("userData")
    @Expose
    private UserData userData;

    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;

    public BookingData getBookingData() {
        return bookingData;
    }

    public void setBookingData(BookingData bookingData) {
        this.bookingData = bookingData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bookingData, flags);
        dest.writeParcelable(this.userData, flags);
        dest.writeParcelable(this.restaurant, flags);
    }

    public PreviousReservation() {
    }

    protected PreviousReservation(Parcel in) {
        this.bookingData = in.readParcelable(BookingData.class.getClassLoader());
        this.userData = in.readParcelable(UserData.class.getClassLoader());
        this.restaurant = in.readParcelable(Restaurant.class.getClassLoader());
    }

    public static final Parcelable.Creator<PreviousReservation> CREATOR = new Parcelable.Creator<PreviousReservation>() {
        @Override
        public PreviousReservation createFromParcel(Parcel source) {
            return new PreviousReservation(source);
        }

        @Override
        public PreviousReservation[] newArray(int size) {
            return new PreviousReservation[size];
        }
    };
}
