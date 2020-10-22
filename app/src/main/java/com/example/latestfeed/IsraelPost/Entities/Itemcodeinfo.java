package com.example.latestfeed.IsraelPost.Entities;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Itemcodeinfo {

    @SerializedName("RowCount")
    private int rowCount;
    @SerializedName("ColCount")
    private int colCount;
    @Embedded
    @SerializedName("ColumnHeaders")
    private List<String> columnHeaders = null;
    @Embedded
    @SerializedName("InfoLines")
    private ArrayList<ArrayList<String>> infoLines = null;

    public Itemcodeinfo() {
        columnHeaders = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        infoLines = new ArrayList<>();
        x.add("ERROR");
        infoLines.add(x);
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public void setColumnHeaders(List<String> columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public ArrayList<ArrayList<String>> getInfoLines() {
        return infoLines;
    }

    public void setInfoLines(ArrayList<ArrayList<String>> infoLines) {
        this.infoLines = infoLines;
    }
}