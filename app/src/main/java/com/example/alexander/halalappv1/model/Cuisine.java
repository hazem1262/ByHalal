package com.example.alexander.halalappv1.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cuisine implements Parcelable {

    @SerializedName("cuisineId")
    @Expose
    private int cuisineId;
    @SerializedName("cuisineNameEn")
    @Expose
    private String cuisineNameEn;
    @SerializedName("cuisineNameFr")
    @Expose
    private String cuisineNameFr;

    public int getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(int cuisineId) {
        this.cuisineId = cuisineId;
    }

    public String getCuisineNameEn() {
        return cuisineNameEn;
    }

    public void setCuisineNameEn(String cuisineNameEn) {
        this.cuisineNameEn = cuisineNameEn;
    }

    public String getCuisineNameFr() {
        return cuisineNameFr;
    }

    public void setCuisineNameFr(String cuisineNameFr) {
        this.cuisineNameFr = cuisineNameFr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cuisineId);
        dest.writeString(this.cuisineNameEn);
        dest.writeString(this.cuisineNameFr);
    }

    public Cuisine() {
    }

    protected Cuisine(Parcel in) {
        this.cuisineId = in.readInt();
        this.cuisineNameEn = in.readString();
        this.cuisineNameFr = in.readString();
    }

    public static final Parcelable.Creator<Cuisine> CREATOR = new Parcelable.Creator<Cuisine>() {
        @Override
        public Cuisine createFromParcel(Parcel source) {
            return new Cuisine(source);
        }

        @Override
        public Cuisine[] newArray(int size) {
            return new Cuisine[size];
        }
    };
}
