package com.example.bitebook_new;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;

public class Entry {
    private String id;

    private String resName;
    private String menName;
    private float price;
    private String area;
    private float rating;
    private String review;
    private String cuisine;
    private String image;
    private static ArrayList<String> favList = new ArrayList<>();

    private String date;
    private int mYear;
    private String mMonth;
    private int mDay;

    public Entry() {
    }


    public Entry(String resName, String menName, float price, String area, float rating, String review, String cuisine, String image) {
        this.resName = resName;
        this.menName = menName;
        this.price = price;
        this.area = area;
        this.rating = rating;
        this.review = review;
        this.cuisine = cuisine;
        this.image = image;
        this.date = setDate();
    }

    public Entry(String id, String resName, String menName, float price, String area, float rating, String review, String cuisine, String image) {
        this.id = id;
        this.resName = resName;
        this.menName = menName;
        this.price = price;
        this.area = area;
        this.rating = rating;
        this.review = review;
        this.cuisine = cuisine;
        this.image = image;
        this.date = setDate();
    }

    private String setDate(){
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);

        String[] months = new String[] {"JAN", "FEB", "MAR", "APR",
                "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        mMonth = months[calendar.MONTH + 1]; // Get the current month name
        System.out.println(mMonth);

        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return (mDay + " / " + mMonth + " / " + mYear);
    }

    public String getDate() {
        return date;
    }

    public float getPrice() {
        return price;
    }

    public String getMenName() {
        return menName;
    }
    public String getId() {
        return id;
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

    public String getImage() {
        return image;
    }

    public static void setFavList(String food) {
        Entry.favList.add(food);
    }

    public static void removeFavList(String food) {
        Entry.favList.remove(food);
    }

    public static ArrayList<String> getFavList() {
        return favList;
    }

    @NonNull
    @Override
    public String toString() {
        return (menName);
    }
}
