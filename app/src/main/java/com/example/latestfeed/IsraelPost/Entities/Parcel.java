package com.example.latestfeed.IsraelPost.Entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

@Entity(tableName = "package_table")
public class Parcel implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String trackingNumber;
    private String title;
    private int status;                                                                     //0: ERROR, 1: ON WAY, 2: COMPLETE
    private int color;
    private Date updateTime;
    private ArrayList<ArrayList<String>> infoLines;

    public Parcel(@NonNull String trackingNumber, String title, int color, ArrayList<ArrayList<String>> infoLines) {
        this.trackingNumber = trackingNumber;
        this.title = title;
        this.color = color;
        this.updateTime = new Date();
        this.infoLines = infoLines;
        setStatus();
    }

    @NonNull
    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(@NonNull String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public ArrayList<ArrayList<String>> getInfoLines() {
        return infoLines;
    }

    public void setInfoLines(ArrayList<ArrayList<String>> infoLines) {
        this.infoLines = infoLines;
    }

    public String getLastStatus() {
        String lastStatus = "";
        if (infoLines != null) {
            if (infoLines.get(0).size() > 1) {
                lastStatus = infoLines.get(0).get(1);
            } else {
                lastStatus = infoLines.get(0).get(0);
            }
        }
        return lastStatus;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Parcel object = (Parcel) obj;
        return (this.getTrackingNumber().equalsIgnoreCase(object.getTrackingNumber()));
//        return (this.getTrackingNumber().equalsIgnoreCase(object.getTrackingNumber()) && this.getTitle().equalsIgnoreCase(object.getTitle()));
    }
}
