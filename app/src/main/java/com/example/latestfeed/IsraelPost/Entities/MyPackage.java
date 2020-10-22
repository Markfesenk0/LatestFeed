package com.example.latestfeed.IsraelPost.Entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

public class MyPackage implements Serializable {

    @SerializedName("ReturnCode")
    private int returnCode;
    @SerializedName("ErrorMessage")
    private String errorMessage;
    @SerializedName("Result")
    private Result result;

    private String trackingNumber;
    private String title;
    private int status;                                                                     //0: ERROR, 1: ON WAY, 2: COMPLETE
    private int color;
    private Date updateTime;

    public MyPackage() {
        result = new Result();
        updateTime = new Date();
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus() {
        HashSet<String> set = new HashSet<String>();
        String lastUpdate = getLastStatus();
        set.add("נמסר ליעדו");
        set.add("נמסר לנציג הנמען");
        set.add("Delivered to addressee");
        set.add("Delivered to a representative of the addressee");
        if (set.contains(lastUpdate)) {
            status = 2;
        } else if (lastUpdate.startsWith("There is no information") || lastUpdate.startsWith("אין נתונים על דבר הדואר")) {
            status = 0;
        } else {
            status = 1;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastStatus() {
        String lastStatus;
        if (result.getItemcodeinfo().getInfoLines().get(0).size() > 1) {
            lastStatus = result.getItemcodeinfo().getInfoLines().get(0).get(1);
        } else {
            lastStatus = result.getItemcodeinfo().getInfoLines().get(0).get(0);
        }
        return lastStatus;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        MyPackage object = (MyPackage) obj;
        return (this.getResult().getBarcode() == object.getResult().getBarcode());
    }
}