package com.example.latestfeed.IsraelPost.Entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("Barcode")
    private String barcode;
    @SerializedName("Lang")
    private String lang;
    @SerializedName("data_number")
    private int data_number;
    @SerializedName("data_type")
    private String data_type;
    @SerializedName("typeName")
    private String typeName;
    @SerializedName("hasImage")
    private int hasImage;
    @SerializedName("hasSignImage")
    private int hasSignImage;
    @SerializedName("hazmana")
    private int hazmana;
    @SerializedName("sHeader1")
    private String sHeader1;
    @SerializedName("sHeader2")
    private String sHeader2;
    @Embedded
    @SerializedName("itemcodeinfo")
    private Itemcodeinfo itemcodeinfo;
    @SerializedName("ReturnCode")
    private int resultReturnCode;
    @SerializedName("ErrorDescription")
    private String errorDescription;
    @SerializedName("Success")
    private boolean success;

    public Result() {
//        barcode = lang = data_type = typeName = sHeader1 = sHeader2 = errorDescription = "";
//        data_number = hasImage = hasSignImage = hazmana = resultReturnCode = 0;
//        success = false;
//        itemcodeinfo = null;
        itemcodeinfo = new Itemcodeinfo();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getData_number() {
        return data_number;
    }

    public void setData_number(int data_number) {
        this.data_number = data_number;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getHasImage() {
        return hasImage;
    }

    public void setHasImage(int hasImage) {
        this.hasImage = hasImage;
    }

    public int getHasSignImage() {
        return hasSignImage;
    }

    public void setHasSignImage(int hasSignImage) {
        this.hasSignImage = hasSignImage;
    }

    public int getHazmana() {
        return hazmana;
    }

    public void setHazmana(int hazmana) {
        this.hazmana = hazmana;
    }

    public String getSHeader1() {
        return sHeader1;
    }

    public void setSHeader1(String sHeader1) {
        this.sHeader1 = sHeader1;
    }

    public String getSHeader2() {
        return sHeader2;
    }

    public void setSHeader2(String sHeader2) {
        this.sHeader2 = sHeader2;
    }

    public Itemcodeinfo getItemcodeinfo() {
        return itemcodeinfo;
    }

    public void setItemcodeinfo(Itemcodeinfo itemcodeinfo) {
        this.itemcodeinfo = itemcodeinfo;
    }

    public int getResultReturnCode() {
        return resultReturnCode;
    }

    public void setResultReturnCode(int returnCode) {
        this.resultReturnCode = returnCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @NonNull
    @Override
    public String toString() {
        return barcode;
        //        return super.toString();
    }
}