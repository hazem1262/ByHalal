package com.example.alexander.halalappv1.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationOrder implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("quantity")
    @Expose
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.quantity);
    }

    public ReservationOrder() {}

    protected ReservationOrder(Parcel in) {
        this.id = in.readInt();
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<ReservationOrder> CREATOR = new Parcelable.Creator<ReservationOrder>() {
        @Override
        public ReservationOrder createFromParcel(Parcel source) {
            return new ReservationOrder(source);
        }

        @Override
        public ReservationOrder[] newArray(int size) {
            return new ReservationOrder[size];
        }
    };
}
