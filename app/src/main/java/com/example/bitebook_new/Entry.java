package com.example.bitebook_new;

public class Entry {
    String name;
    String location;
    int rating;
    String review;
    String cuisine;

    public Entry(String name, String location, int rating, String review, String cuisine) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.review = review;
        this.cuisine = cuisine;
    }

    public Entry getEntry(){
        return this;
    }



}
