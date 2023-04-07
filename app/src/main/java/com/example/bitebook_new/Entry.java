package com.example.bitebook_new;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Entry {
    String resName;
    static ArrayList<String> areaList = new ArrayList<>();
    String menName;
    String price;
    static ArrayList<String> priceList = new ArrayList<>();
    String area;
    float rating;
    String review;
    String cuisine;
    static ArrayList<String> cuisineList = new ArrayList<>();
    private static int numberOfObject = 0;
    static Bitmap foodImage;

    public Entry(String resName, String menName, String price, String area, String cuisine, float rating, String review) {
        this.resName = resName;
        areaList.add(resName);
        this.menName = menName;
        this.price = price;
        priceList.add(price);
        this.area = area;
        this.rating = rating;
        this.review = review;
        this.cuisine = cuisine;
        cuisineList.add(cuisine);
        numberOfObject++;
    }

    public static ArrayList<String> getAreaList() {
        return areaList;
    }

    public static int getNumberOfObject() {
        return numberOfObject;
    }

    public static void setFoodImage(Bitmap  image){
        foodImage = image;
    }

    public Entry getEntry(){
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return ("Restaurant Name: " + this.resName + " / Menu Name: " + this.menName + " / Price: " + this.price +
                " / Area: " + this.area + " / Cuisine Type: " + this.cuisine + " / Rating: " + this.rating +
                " / Review: "+ this.review + " / Image: " + this.foodImage);
    }
}
