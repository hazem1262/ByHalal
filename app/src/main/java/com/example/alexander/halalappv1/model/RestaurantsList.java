package com.example.alexander.halalappv1.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fOKS ART on 2/21/2018.
 */

public class RestaurantsList implements Parcelable {

    @SerializedName("listId")
    @Expose
    private int listId;
    @SerializedName("listNameEn")
    @Expose
    private String listNameEn;
    @SerializedName("listNameFr")
    @Expose
    private String listNameFr;
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = null;

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getListNameEn() {
        return listNameEn;
    }

    public void setListNameEn(String listNameEn) {
        this.listNameEn = listNameEn;
    }

    public String getListNameFr() {
        return listNameFr;
    }

    public void setListNameFr(String listNameFr) {
        this.listNameFr = listNameFr;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.listId);
        dest.writeString(this.listNameEn);
        dest.writeString(this.listNameFr);
        dest.writeTypedList(this.restaurants);
    }

    public RestaurantsList() {
    }

    protected RestaurantsList(Parcel in) {
        this.listId = in.readInt();
        this.listNameEn = in.readString();
        this.listNameFr = in.readString();
        this.restaurants = in.createTypedArrayList(Restaurant.CREATOR);
    }

    public static final Parcelable.Creator<RestaurantsList> CREATOR = new Parcelable.Creator<RestaurantsList>() {
        @Override
        public RestaurantsList createFromParcel(Parcel source) {
            return new RestaurantsList(source);
        }

        @Override
        public RestaurantsList[] newArray(int size) {
            return new RestaurantsList[size];
        }
    };
}
