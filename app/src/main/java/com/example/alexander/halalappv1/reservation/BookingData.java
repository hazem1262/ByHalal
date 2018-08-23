package com.example.alexander.halalappv1.reservation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BookingData implements Parcelable {

    @SerializedName("reservationId")
    @Expose
    private int reservationId;
    @SerializedName("numberOfPeople")
    @Expose
    private String numberOfPeople;
    @SerializedName("numberOfPeoplString")
    @Expose
    private String numberOfPeoplString;
    @SerializedName("reservationDayName")
    @Expose
    private String reservationDayName;
    @SerializedName("reservationDate")
    @Expose
    private String reservationDate;
    @SerializedName("reservationTime")
    @Expose
    private String reservationTime;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getNumberOfPeoplString() {
        return numberOfPeoplString;
    }

    public void setNumberOfPeoplString(String numberOfPeoplString) {
        this.numberOfPeoplString = numberOfPeoplString;
    }

    public String getReservationDayName() {
        return reservationDayName;
    }

    public void setReservationDayName(String reservationDayName) {
        this.reservationDayName = reservationDayName;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.reservationId);
        dest.writeString(this.numberOfPeople);
        dest.writeString(this.numberOfPeoplString);
        dest.writeString(this.reservationDayName);
        dest.writeString(this.reservationDate);
        dest.writeString(this.reservationTime);
    }

    public BookingData() {
    }

    protected BookingData(Parcel in) {
        this.reservationId = in.readInt();
        this.numberOfPeople = in.readString();
        this.numberOfPeoplString = in.readString();
        this.reservationDayName = in.readString();
        this.reservationDate = in.readString();
        this.reservationTime = in.readString();
    }

    public static final Parcelable.Creator<BookingData> CREATOR = new Parcelable.Creator<BookingData>() {
        @Override
        public BookingData createFromParcel(Parcel source) {
            return new BookingData(source);
        }

        @Override
        public BookingData[] newArray(int size) {
            return new BookingData[size];
        }
    };
}
