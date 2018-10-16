package com.example.alexander.halalappv1.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City implements Parcelable {

    @SerializedName("cityId")
    @Expose
    private int cityId;

    @SerializedName("name")
    @Expose
    private String cityNameFr;

    @SerializedName("longitude")
    @Expose
    private String cityLongitude;
    @SerializedName("latitude")
    @Expose
    private String cityLatitude;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    public String getCityNameFr() {
        return cityNameFr;
    }

    public void setCityNameFr(String cityNameFr) {
        this.cityNameFr = cityNameFr;
    }

    public String getCityLongitude() {
        return cityLongitude;
    }

    public void setCityLongitude(String cityLongitude) {
        this.cityLongitude = cityLongitude;
    }

    public String getCityLatitude() {
        return cityLatitude;
    }

    public void setCityLatitude(String cityLatitude) {
        this.cityLatitude = cityLatitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cityId);
        dest.writeString(this.cityNameFr);
        dest.writeString(this.cityLongitude);
        dest.writeString(this.cityLatitude);
    }

    public City() {
    }

    protected City(Parcel in) {
        this.cityId = in.readInt();
        this.cityNameFr = in.readString();
        this.cityLongitude = in.readString();
        this.cityLatitude = in.readString();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
