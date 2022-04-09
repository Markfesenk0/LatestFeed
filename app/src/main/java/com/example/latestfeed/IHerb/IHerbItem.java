package com.example.latestfeed.IHerb;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

@Entity(tableName = "iherb_database")
public class IHerbItem implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private int itemId;
    private String title;
    private String brand;
    private double standardPrice;
    private double minPrice;
    private double maxPrice;
    private double currentPrice;
    private int discount;
    private String currency;
    private boolean stock;       // OutOfStock, InStock
    private String dateAdded;
    private String urlUS;
    private String urlIL;
    private String imgUrl;
    private boolean refreshFail;

    public IHerbItem() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.dateAdded = formatter.format(date);
        this.refreshFail = false;
    }

    @Ignore
    public IHerbItem(String urlIL) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.dateAdded = formatter.format(date);
        this.urlIL = urlIL;
        this.urlUS = urlIL.replace("https://il.", "https://");
        this.refreshFail = false;
    }

    @Ignore
    public IHerbItem(@NonNull int itemId, String title, String brand, double standardPrice, double currentPrice, String urlIL, String urlUS, String imgUrl, boolean stock, String currency) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.itemId = itemId;
        this.title = title;
        this.brand = brand;
        this.maxPrice = this.standardPrice = standardPrice;
        this.currentPrice = this.minPrice = currentPrice;
        this.dateAdded = formatter.format(date);
        this.urlUS = urlUS;
        this.urlIL = urlIL;
        this.imgUrl = imgUrl;
        this.stock = stock;
        this.currency = currency;
        this.refreshFail = false;
        if (standardPrice != 0) {
            this.discount = 100 - (int) Math.round((currentPrice / standardPrice) * 100);
        } else {
            this.discount = 0;
        }
    }

    @NonNull
    public int getItemId() {
        return itemId;
    }

    public void setItemId(@NonNull int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(double price) {
        this.standardPrice = price;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double price) {
        this.minPrice = price;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double price) {
        this.maxPrice = price;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double price) {
        this.currentPrice = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String date) {
        this.dateAdded = date;
    }

    public String getUrlUS() {
        return urlUS;
    }

    public void setUrlUS(String urlUS) {
        this.urlUS = urlUS;
    }

    public String getUrlIL() {
        return urlIL;
    }

    public void setUrlIL(String urlIL) {
        this.urlIL = urlIL;
    }

    public boolean getStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (31 * hash + currentPrice);
        hash = 31 * hash + (null == String.valueOf(itemId) ? 0 : String.valueOf(itemId).hashCode());
        hash = 71 * hash + (int) getMinPrice();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        IHerbItem object = (IHerbItem) obj;
        return (this.getItemId() == object.getItemId() &&
                this.getMinPrice() == object.getMinPrice() &&
                this.getCurrentPrice() == object.getCurrentPrice() &&
                this.getStock() == object.getStock());
    }

    public boolean isRefreshFail() {
        return refreshFail;
    }

    public void setRefreshFail(boolean refreshFail) {
        this.refreshFail = refreshFail;
    }

    @Override
    public String toString() {
        return "IHerbItem{" +
                "itemId=" + itemId +
                ", title='" + title + '\'' +
                ", brand='" + brand + '\'' +
                ", standardPrice=" + standardPrice +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", currentPrice=" + currentPrice +
                ", discount=" + discount +
                ", currency='" + currency + '\'' +
                ", stock='" + stock + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
