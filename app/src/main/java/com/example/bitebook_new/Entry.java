package com.example.bitebook_new;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Entry {
    private String resName;
    private String menName;
    private Integer price;
    private String area;
    private float rating;
    private String review;
    private String cuisine;

    public Entry() {
    }


    public Entry(String resName, String menName, Integer price, String area, float rating, String review, String cuisine) {
        this.resName = resName;
        this.menName = menName;
        this.price = price;
        this.area = area;
        this.rating = rating;
        this.review = review;
        this.cuisine = cuisine;
    }

    public Integer getPrice() {
        return price;
    }

    public String getMenName() {
        return menName;
    }

    public String getArea() {
        return area;
    }

    public String getReview() {
        return review;
    }

    public String getCuisine() {
        return cuisine;
    }

    public float getRating() {
        return rating;
    }

    public String getResName() {
        return resName;
    }


    @NonNull
    @Override
    public String toString() {
        return ("asdfsa");
    }
}
