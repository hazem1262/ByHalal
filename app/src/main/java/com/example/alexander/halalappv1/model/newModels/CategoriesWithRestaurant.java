
package com.example.alexander.halalappv1.model.newModels;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriesWithRestaurant {

    @SerializedName("catId")
    @Expose
    private Integer catId;
    @SerializedName("catName")
    @Expose
    private String catName;
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = null;

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

}
