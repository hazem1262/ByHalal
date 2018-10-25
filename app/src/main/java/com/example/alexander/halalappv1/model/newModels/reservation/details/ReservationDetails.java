
package com.example.alexander.halalappv1.model.newModels.reservation.details;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationDetails {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("guests")
    @Expose
    private Integer guests;

    @SerializedName("organizer")
    @Expose
    private String organizer;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("promotion")
    @Expose
    private Object promotion;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("dayName")
    @Expose
    private String dayName;
    @SerializedName("date2")
    @Expose
    private String date2;
    @SerializedName("time2")
    @Expose
    private String time2;
    @SerializedName("products")
    @Expose
    private ArrayList<Product> products = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Object getPromotion() {
        return promotion;
    }

    public void setPromotion(Object promotion) {
        this.promotion = promotion;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

}
