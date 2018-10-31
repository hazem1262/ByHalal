
package com.example.alexander.halalappv1.model.newModels.menues;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuResponse {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("menuItems")
    @Expose
    private ArrayList<MenuItem> menuItems = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MenuResponse() {
    }

    /**
     * 
     * @param id
     * @param name
     * @param menuItems
     */
    public MenuResponse(Integer id, String name, ArrayList<MenuItem> menuItems) {
        super();
        this.id = id;
        this.name = name;
        this.menuItems = menuItems;
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

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}
