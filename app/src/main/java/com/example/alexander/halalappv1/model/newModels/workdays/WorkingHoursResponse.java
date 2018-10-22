
package com.example.alexander.halalappv1.model.newModels.workdays;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkingHoursResponse {

    @SerializedName("promotion")
    @Expose
    private Integer promotion;
    @SerializedName("workingDays")
    @Expose
    private List<WorkingDay> workingDays = null;

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }

    public List<WorkingDay> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(List<WorkingDay> workingDays) {
        this.workingDays = workingDays;
    }

}
