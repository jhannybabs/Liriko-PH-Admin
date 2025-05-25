package com.example.lirikophadmin;

public class SeatPrice {
    private String type;
    private int price;

    public SeatPrice() {
    }

    public SeatPrice(String type, int price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }
}
