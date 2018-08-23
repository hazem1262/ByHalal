
package com.example.alexander.halalappv1.model.modifiedmodels;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantsList1 implements Parcelable
{

    @SerializedName("listId")
    @Expose
    private Integer listId;
    @SerializedName("listNameEn")
    @Expose
    private String listNameEn;
    @SerializedName("listNameFr")
    @Expose
    private String listNameFr;
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = null;
    public final static Creator<RestaurantsList1> CREATOR = new Creator<RestaurantsList1>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RestaurantsList1 createFromParcel(Parcel in) {
            return new RestaurantsList1(in);
        }

        public RestaurantsList1 [] newArray(int size) {
            return (new RestaurantsList1[size]);
        }

    }
    ;

    protected RestaurantsList1(Parcel in) {
        this.listId = in.readInt();
        this.listNameEn = in.readString();
        this.listNameFr = in.readString();
        this.restaurants = in.createTypedArrayList(Restaurant.CREATOR);
    }

    public RestaurantsList1() {
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.listId);
        dest.writeString(this.listNameEn);
        dest.writeString(this.listNameFr);
        dest.writeTypedList(this.restaurants);
    }

    public int describeContents() {
        return  0;
    }

}
