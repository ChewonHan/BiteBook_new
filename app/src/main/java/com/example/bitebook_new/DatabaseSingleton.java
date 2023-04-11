package com.example.bitebook_new;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseSingleton {

    private static FirebaseDatabase mDatabase;

    private static DatabaseSingleton instance;

    public static DatabaseSingleton getInstance() {
        if (instance == null) {
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    private DatabaseSingleton() {
        // Initialize the database reference
        mDatabase = FirebaseDatabase.getInstance("https://bitebook-380210-default-rtdb.asia-southeast1.firebasedatabase.app/");
    }

    public FirebaseDatabase getDBInstance() {
        return mDatabase;
    }
}

