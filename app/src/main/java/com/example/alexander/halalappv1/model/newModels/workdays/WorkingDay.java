
package com.example.alexander.halalappv1.model.newModels.workdays;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkingDay {

    @SerializedName("dayName")
    @Expose
    private String dayName;

    @SerializedName("periods")
    @Expose
    private List<Period> periods = null;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

}
