package com.example.bitebook_new;

import androidx.annotation.NonNull;

public class Entry {
    String resName;
    String menName;
    String price;
    String area;
    float rating;
    String review;
    String cuisine;

    public Entry(String resName, String menName, String price, String area, String cuisine, float rating, String review) {
        this.resName = resName;
        this.menName = menName;
        this.price = price;
        this.area = area;
        this.rating = rating;
        this.review = review;
        this.cuisine = cuisine;
    }

    public Entry getEntry(){
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return ("Restaurant Name: " + this.resName + " / Menu Name: " + this.menName + " / Price: " + this.price +
                " / Area: " + this.area + " / Cuisine Type: " + this.cuisine + " / Rating: " + this.rating + " / Review: "+ this.review);
    }
}
