package com.example.lirikophadmin;

public class BookingModel {
    public String id, name, email, phone, ticketType, payment, total, artistId, bandName, date, place;

    public BookingModel() {}

    public BookingModel(String id, String name, String email, String phone, String ticketType,
                        String payment, String total, String artistId, String bandName, String date, String place) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.ticketType = ticketType;
        this.payment = payment;
        this.total = total;
        this.artistId = artistId;
        this.bandName = bandName;
        this.date = date;
        this.place = place;
    }
}

