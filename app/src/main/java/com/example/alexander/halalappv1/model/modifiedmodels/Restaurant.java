
package com.example.alexander.halalappv1.model.modifiedmodels;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cuisineId")
    @Expose
    private Integer cuisineId;
    @SerializedName("cuisineNameEn")
    @Expose
    private String cuisineNameEn;
    @SerializedName("cuisineNameFr")
    @Expose
    private String cuisineNameFr;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("cityNameEn")
    @Expose
    private String cityNameEn;
    @SerializedName("cityNameFr")
    @Expose
    private String cityNameFr;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("favourite")
    @Expose
    private String favourite;
    @SerializedName("alcohol")
    @Expose
    private String alcohol;
    @SerializedName("certification")
    @Expose
    private String certification;
    @SerializedName("visitors")
    @Expose
    private String visitors;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("excutiveChef")
    @Expose
    private String excutiveChef;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("workDays")
    @Expose
    private List<WorkDay> workDays = null;
    @SerializedName("menus")
    @Expose
    private List<Menu> menus = null;
    @SerializedName("gallery")
    @Expose
    private String gallery;
    public final static Creator<Restaurant> CREATOR = new Creator<Restaurant>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return (new Restaurant[size]);
        }

    }
            ;

    protected Restaurant(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.cuisineId = in.readInt();
        this.cuisineNameEn = in.readString();
        this.cuisineNameFr = in.readString();
        this.capacity = in.readString();
        this.cityId = in.readString();
        this.cityNameEn = in.readString();
        this.cityNameFr = in.readString();
        this.rate = in.readInt();
        this.image = in.readString();
        this.favourite = in.readString();
        this.alcohol = in.readString();
        this.certification = in.readString();
        this.visitors = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.excutiveChef = in.readString();
        this.address = in.readString();
        this.country = in.readString();
        this.phone = in.readString();
        this.website = in.readString();
        this.notes = in.readString();
        this.description = in.readString();
        this.workDays = in.createTypedArrayList(WorkDay.CREATOR);
        this.menus = in.createTypedArrayList(Menu.CREATOR);
        this.gallery = in.readString();
    }

    public Restaurant() {
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

    public Integer getCuisineId() {
        return cuisineId;
    }

    public void setCuisineId(Integer cuisineId) {
        this.cuisineId = cuisineId;
    }

    public String getCuisineNameEn() {
        return cuisineNameEn;
    }

    public void setCuisineNameEn(String cuisineNameEn) {
        this.cuisineNameEn = cuisineNameEn;
    }

    public String getCuisineNameFr() {
        return cuisineNameFr;
    }

    public void setCuisineNameFr(String cuisineNameFr) {
        this.cuisineNameFr = cuisineNameFr;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityNameEn() {
        return cityNameEn;
    }

    public void setCityNameEn(String cityNameEn) {
        this.cityNameEn = cityNameEn;
    }

    public String getCityNameFr() {
        return cityNameFr;
    }

    public void setCityNameFr(String cityNameFr) {
        this.cityNameFr = cityNameFr;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getVisitors() {
        return visitors;
    }

    public void setVisitors(String visitors) {
        this.visitors = visitors;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getExcutiveChef() {
        return excutiveChef;
    }

    public void setExcutiveChef(String excutiveChef) {
        this.excutiveChef = excutiveChef;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WorkDay> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(List<WorkDay> workDays) {
        this.workDays = workDays;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.cuisineId);
        dest.writeString(this.cuisineNameEn);
        dest.writeString(this.cuisineNameFr);
        dest.writeString(this.capacity);
        dest.writeString(this.cityId);
        dest.writeString(this.cityNameEn);
        dest.writeString(this.cityNameFr);
        dest.writeInt(this.rate);
        dest.writeString(this.image);
        dest.writeString(this.favourite);
        dest.writeString(this.alcohol);
        dest.writeString(this.certification);
        dest.writeString(this.visitors);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.excutiveChef);
        dest.writeString(this.address);
        dest.writeString(this.country);
        dest.writeString(this.phone);
        dest.writeString(this.website);
        dest.writeString(this.notes);
        dest.writeString(this.description);
        dest.writeTypedList(this.workDays);
        dest.writeTypedList(this.menus);
        dest.writeString(this.gallery);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
