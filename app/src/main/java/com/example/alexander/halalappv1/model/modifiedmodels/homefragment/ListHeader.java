package com.example.alexander.halalappv1.model.modifiedmodels.homefragment;

/**
 * Created by Hazem on 9/7/2018.
 */

public class ListHeader {
    private String sectionHeader;
    private String restaurentName;
    private String restaurentType;
    private int restaurentDiscount;
    private String restaurentImage;
    public ListHeader(String sectionHeader, String restaurentName, String restaurentType, String restaurentImage, int restaurentDiscount) {
        this.sectionHeader = sectionHeader;
        this.restaurentName = restaurentName;
        this.restaurentType = restaurentType;
        this.restaurentImage = restaurentImage;
        this.restaurentDiscount = restaurentDiscount;
    }

    public String getSectionHeader() {
        return sectionHeader;
    }

    public void setSectionHeader(String sectionHeader) {
        this.sectionHeader = sectionHeader;
    }

    public String getRestaurentName() {
        return restaurentName;
    }

    public void setRestaurentName(String restaurentName) {
        this.restaurentName = restaurentName;
    }

    public String getRestaurentType() {
        return restaurentType;
    }

    public void setRestaurentType(String restaurentType) {
        this.restaurentType = restaurentType;
    }

    public int getRestaurentDiscount() {
        return restaurentDiscount;
    }

    public void setRestaurentDiscount(int restaurentDiscount) {
        this.restaurentDiscount = restaurentDiscount;
    }

    public String getRestaurentImage() {
        return restaurentImage;
    }

    public void setRestaurentImage(String restaurentImage) {
        this.restaurentImage = restaurentImage;
    }

    @Override
    public String toString() {
        return "ListHeader{" +
                "sectionHeader='" + sectionHeader + '\'' +
                ", restaurentName='" + restaurentName + '\'' +
                ", restaurentType='" + restaurentType + '\'' +
                ", restaurentDiscount=" + restaurentDiscount +
                ", restaurentImage='" + restaurentImage + '\'' +
                '}';
    }
}
