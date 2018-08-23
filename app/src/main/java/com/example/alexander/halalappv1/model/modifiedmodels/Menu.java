
package com.example.alexander.halalappv1.model.modifiedmodels;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menu implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("restaurant_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("menuItems")
    @Expose
    private List<MenuItem> menuItems = null;
    public final static Creator<Menu> CREATOR = new Creator<Menu>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        public Menu[] newArray(int size) {
            return (new Menu[size]);
        }

    }
    ;

    protected Menu(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.restaurantId = in.readInt();
        this.menuItems = in.createTypedArrayList(MenuItem.CREATOR);
    }

    public Menu() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.restaurantId);
        dest.writeTypedList(this.menuItems);
    }

    public int describeContents() {
        return  0;
    }

}
