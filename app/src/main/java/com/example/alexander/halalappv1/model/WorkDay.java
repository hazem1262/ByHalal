package com.example.alexander.halalappv1.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkDay implements Parcelable {

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dayName);
        dest.writeString(this.dayNameFr);
        dest.writeStringList(this.workingHours);
        dest.writeStringList(this.periods);
    }

    public WorkDay() {
    }

    protected WorkDay(Parcel in) {
        this.dayName = in.readString();
        this.dayNameFr = in.readString();
        this.workingHours = in.createStringArrayList();
        this.periods = in.createStringArrayList();
    }

    public static final Parcelable.Creator<WorkDay> CREATOR = new Parcelable.Creator<WorkDay>() {
        @Override
        public WorkDay createFromParcel(Parcel source) {

            return new WorkDay(source);
        }

        @Override
        public WorkDay[] newArray(int size) {
            return new WorkDay[size];
        }
    };

//    @SerializedName("dayName")
//    @Expose
//    private String dayName;
//    @SerializedName("start")
//    @Expose
//    private String start;
//    @SerializedName("end")
//    @Expose
//    private String end;
//
//    public String getDayName() {
//        return dayName;
//    }
//
//    public void setDayName(String dayName) {
//        this.dayName = dayName;
//    }
//
//    public String getStart() {
//        return start;
//    }
//
//    public void setStart(String start) {
//        this.start = start;
//    }
//
//    public String getEnd() {
//        return end;
//    }
//
//    public void setEnd(String end) {
//        this.end = end;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.dayName);
//        dest.writeString(this.start);
//        dest.writeString(this.end);
//    }
//
//    public WorkDay() {
//    }
//
//    protected WorkDay(Parcel in) {
//        this.dayName = in.readString();
//        this.start = in.readString();
//        this.end = in.readString();
//    }
//
//    public static final Parcelable.Creator<WorkDay> CREATOR = new Parcelable.Creator<WorkDay>() {
//        @Override
//        public WorkDay createFromParcel(Parcel source) {
//            return new WorkDay(source);
//        }
//
//        @Override
//        public WorkDay[] newArray(int size) {
//            return new WorkDay[size];
//        }
//    };
}
