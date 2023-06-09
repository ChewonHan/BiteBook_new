package com.example.bitebook_new;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class FirebaseHelper {
    private String uid;
    private DatabaseReference ref;
    FirebaseUser user;


    public static String generateRandomString() {
        long time = System.currentTimeMillis();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random(time);

        for (int i = 0; i < 20; i++) {
            int index = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }

        return sb.toString();
    }


    public static void createEntry(Context context, Entry entry, Bitmap bitmap) {

        String uid = getCurrentUserUID(context);

        if (uid != null) {
            FirebaseDatabase database = DatabaseSingleton.getInstance().getDBInstance();;
            DatabaseReference myRef = database.getReference(uid + "/entries");
            DatabaseReference newRef = myRef.push();
            newRef.setValue(entry);
            // set id
            newRef.child("id").setValue(newRef.getKey());
            // remove latlng
            newRef.child("latlng").removeValue();

        }
    }

    public static void deleteEntry(Context context, String id) {
        String uid = getCurrentUserUID(context);

        if (uid != null) {
            FirebaseDatabase database = DatabaseSingleton.getInstance().getDBInstance();
            DatabaseReference myRef = database.getReference(uid + "/entries/" + id);
            myRef.removeValue();
        }
    }

    public static void updateEntry(Context context, Entry newEntry, Bitmap bitmap){
        String uid = getCurrentUserUID(context);

        if (uid != null) {
            FirebaseDatabase database = DatabaseSingleton.getInstance().getDBInstance();;
            DatabaseReference myRef = database.getReference(uid + "/entries/" + newEntry.getId());
            myRef.setValue(newEntry);
        }
    }




    public static String getCurrentUserUID(Context context) {
        String uid = null;
        FirebaseUser user = AuthSingleton.getInstance().getCurrentUser();
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(context);

        if (gAccount != null) {
            uid = gAccount.getId();
        } else if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();
                return uid;
            }
        } else {
            System.out.println("no user found");
        }
        return uid;
    }

}
