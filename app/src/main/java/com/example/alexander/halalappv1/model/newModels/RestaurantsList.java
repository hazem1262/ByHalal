
package com.example.alexander.halalappv1.model.newModels;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantsList {

  @SerializedName("restaurantOfTheWeek")
  @Expose
  private RestaurantOfTheWeek restaurantOfTheWeek;
  @SerializedName("categoriesWithRestaurants")
  @Expose
  private ArrayList<CategoriesWithRestaurant> categoriesWithRestaurants = null;
  @SerializedName("categories")
  @Expose
  private ArrayList<Category> categories = null;

  public RestaurantOfTheWeek getRestaurantOfTheWeek() {
    return restaurantOfTheWeek;
  }

  public void setRestaurantOfTheWeek(RestaurantOfTheWeek restaurantOfTheWeek) {
    this.restaurantOfTheWeek = restaurantOfTheWeek;
  }

  public ArrayList<CategoriesWithRestaurant> getCategoriesWithRestaurants() {
    return categoriesWithRestaurants;
  }

  public void setCategoriesWithRestaurants(ArrayList<CategoriesWithRestaurant> categoriesWithRestaurants) {
    this.categoriesWithRestaurants = categoriesWithRestaurants;
  }

  public ArrayList<Category> getCategories() {
    return categories;
  }

  public void setCategories(ArrayList<Category> categories) {
    this.categories = categories;
  }

}
