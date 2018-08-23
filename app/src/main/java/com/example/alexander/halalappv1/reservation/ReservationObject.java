package com.example.alexander.halalappv1.reservation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReservationObject implements Parcelable {

    @SerializedName("upComingReservations")
    @Expose
    private List<UpComingReservation> upComingReservations = null;

    @SerializedName("previousReservations")
    @Expose
    private List<PreviousReservation> previousReservations = null;

    public List<UpComingReservation> getUpComingReservations() {
        return upComingReservations;
    }

    public void setUpComingReservations(List<UpComingReservation> upComingReservations) {
        this.upComingReservations = upComingReservations;
    }

    public List<PreviousReservation> getPreviousReservations() {
        return previousReservations;
    }

    public void setPreviousReservations(List<PreviousReservation> previousReservations) {
        this.previousReservations = previousReservations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.upComingReservations);
        dest.writeTypedList(this.previousReservations);
    }

    public ReservationObject() {
    }

    protected ReservationObject(Parcel in) {
        this.upComingReservations = in.createTypedArrayList(UpComingReservation.CREATOR);
        this.previousReservations = in.createTypedArrayList(PreviousReservation.CREATOR);
    }

    public static final Parcelable.Creator<ReservationObject> CREATOR = new Parcelable.Creator<ReservationObject>() {
        @Override
        public ReservationObject createFromParcel(Parcel source) {
            return new ReservationObject(source);
        }

        @Override
        public ReservationObject[] newArray(int size) {
            return new ReservationObject[size];
        }
    };
}
