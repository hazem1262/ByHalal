package com.example.alexander.halalappv1.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuItem implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("quantity")
    @Expose
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.price);
        dest.writeString(this.picture);
        dest.writeString(this.categoryId);
        dest.writeInt(this.quantity);
    }

    public MenuItem() {
    }

    protected MenuItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.price = in.readString();
        this.picture = in.readString();
        this.categoryId = in.readString();
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel source) {
            return new MenuItem(source);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };






























//    @SerializedName("id")
//    @Expose
//    private int id;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("description")
//    @Expose
//    private String description;
//    @SerializedName("price")
//    @Expose
//    private String price;
//    @SerializedName("picture")
//    @Expose
//    private String picture;
//    @SerializedName("category_id")
//    @Expose
//    private String categoryId;
//
//    private int quantity = 0;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getPicture() {
//        return picture;
//    }
//
//    public void setPicture(String picture) {
//        this.picture = picture;
//    }
//
//    public String getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(String categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.id);
//        dest.writeString(this.name);
//        dest.writeString(this.description);
//        dest.writeString(this.price);
//        dest.writeString(this.picture);
//        dest.writeString(this.categoryId);
//        dest.writeInt(this.quantity);
//    }
//
//    public MenuItem() {
//    }
//
//    protected MenuItem(Parcel in) {
//        this.id = in.readInt();
//        this.name = in.readString();
//        this.description = in.readString();
//        this.price = in.readString();
//        this.picture = in.readString();
//        this.categoryId = in.readString();
//        this.quantity = in.readInt();
//    }
//
//    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
//        @Override
//        public MenuItem createFromParcel(Parcel source) {
//            return new MenuItem(source);
//        }
//
//        @Override
//        public MenuItem[] newArray(int size) {
//            return new MenuItem[size];
//        }
//    };
}
