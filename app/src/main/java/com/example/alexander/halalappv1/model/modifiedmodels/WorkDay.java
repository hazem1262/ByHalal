
package com.example.alexander.halalappv1.model.modifiedmodels;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkDay implements Parcelable
{

    @SerializedName("dayName")
    @Expose
    private String dayName;
    @SerializedName("dayNameFr")
    @Expose
    private String dayNameFr;
    @SerializedName("workingHours")
    @Expose
    private List<String> workingHours = null;
    @SerializedName("periods")
    @Expose
    private List<String> periods = null;
    public final static Creator<WorkDay> CREATOR = new Creator<WorkDay>() {


        @SuppressWarnings({
            "unchecked"
        })
        public WorkDay createFromParcel(Parcel in) {
            return new WorkDay(in);
        }

        public WorkDay[] newArray(int size) {
            return (new WorkDay[size]);
        }

    }
    ;

    protected WorkDay(Parcel in) {
        this.dayName = in.readString();
        this.dayNameFr = in.readString();
        this.workingHours = in.createStringArrayList();
        this.periods = in.createStringArrayList();
    }

    public WorkDay() {
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayNameFr() {
        return dayNameFr;
    }

    public void setDayNameFr(String dayNameFr) {
        this.dayNameFr = dayNameFr;
    }

    public List<String> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<String> workingHours) {
        this.workingHours = workingHours;
    }

    public List<String> getPeriods() {
        return periods;
    }

    public void setPeriods(List<String> periods) {
        this.periods = periods;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dayName);
        dest.writeString(this.dayNameFr);
        dest.writeStringList(this.workingHours);
        dest.writeStringList(this.periods);
    }

    public int describeContents() {
        return  0;
    }

}
