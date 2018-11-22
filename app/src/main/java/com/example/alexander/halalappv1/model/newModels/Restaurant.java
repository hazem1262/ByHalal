
package com.example.alexander.halalappv1.model.newModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {


  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("picture")
  @Expose
  private String picture;
  @SerializedName("cuisineName")
  @Expose
  private String cuisineName;
  @SerializedName("promotionAmount")
  @Expose
  private Integer promotionAmount;

  public Restaurant(int id, String name, String picture, String cuisine, int promotion){
    this.id = id;
    this.name = name;
    this.picture = picture;
    this.cuisineName = cuisine;
    this.promotionAmount = promotion;
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

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getCuisineName() {
    return cuisineName;
  }

  public void setCuisineName(String cuisineName) {
    this.cuisineName = cuisineName;
  }

  public Integer getPromotionAmount() {
    return promotionAmount;
  }

  public void setPromotionAmount(Integer promotionAmount) {
    this.promotionAmount = promotionAmount;
  }

}
