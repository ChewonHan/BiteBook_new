package com.example.bitebook_new;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthSingleton {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static AuthSingleton instance;

    public static AuthSingleton getInstance() {
        if (instance == null) {
            instance = new AuthSingleton();
        }
        return instance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

}

