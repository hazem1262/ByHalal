
package com.example.alexander.halalappv1.model.newModels.menues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuItem {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private String price;

    private int quantity = 0;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public MenuItem() {
    }

    /**
     * 
     * @param id
     * @param price
     * @param description
     * @param name
     */
    public MenuItem(Integer id, String name, String description, String price) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
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

}
