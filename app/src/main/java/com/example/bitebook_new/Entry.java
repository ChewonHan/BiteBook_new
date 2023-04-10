package com.example.bitebook_new;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

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
    private double lat;
    private double lng;

    public Entry() {
    }


    public Entry(String resName, String menName, float price, String area, float rating, String review, String cuisine, String image, LatLng latLng) {
        this.resName = resName;
        this.menName = menName;
        this.price = price;
        this.area = area;
        this.rating = rating;
        this.review = review;
        this.cuisine = cuisine;
        this.image = image;
        this.date = setDate();
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public Entry(String id, String resName, String menName, float price, String area, float rating, String review, String cuisine, String image, LatLng latLng) {
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
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
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

    public LatLng getLatlng() {
        return new LatLng(lat, lng);
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setMenName(String menName) {
        this.menName = menName;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setImage(String image) {
        this.image = image;
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
