package com.example.lirikophadmin;

import androidx.annotation.NonNull;

import java.util.List;

public class Artists {
    private String id;
    private String imageUrl;
    private String bandName;
    private String date;
    private String place;
    private List<SeatPrice> price;

    public Artists() {
    }

    public Artists(String imageUrl, String bandName, String date, String place, List<SeatPrice> price) {
        this.imageUrl = imageUrl;
        this.bandName = bandName;
        this.date = date;
        this.place = place;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBandName() {
        return bandName;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public List<SeatPrice> getPrice() {
        return price;
    }

    public void setPrice(List<SeatPrice> price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return bandName;
    }
}
