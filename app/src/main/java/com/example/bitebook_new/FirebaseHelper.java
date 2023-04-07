package com.example.bitebook_new;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {
    private String uid;
    private DatabaseReference ref;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public static void createEntry(Context context, Entry entry) {

        String uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(context);

        if (gAccount != null) {
            uid = gAccount.getId();
        } else if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();
            }
        } else {
            System.out.println("no user found");
        }

        if (uid != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://bitebook-380210-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference(uid + "/entries");
            DatabaseReference newRef = myRef.push();
            newRef.setValue(entry);
        }
    }

    public static String getCurrentUser(Context context) {
        String uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(context);

        if (gAccount != null) {
            uid = gAccount.getId();
        } else if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();
            }
        } else {
            System.out.println("no user found");
        }

        return uid;
    }

    public static void getEntries(Context context, ArrayList<Entry> entries) {

        String uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(context);


        if (gAccount != null) {
            uid = gAccount.getId();
        } else if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();
            }
        } else {
            System.out.println("no user found");
        }

        if (uid != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://bitebook-380210-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference(uid + "/entries");
//            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (!task.isSuccessful()) {
//                        Log.e("firebase", "Error getting data", task.getException());
//                    }
//                    else {
//                        DataSnapshot result = task.getResult();
//                        for (DataSnapshot entry : result.getChildren()) {
//                            Entry e = entry.getValue(Entry.class);
//                            entries.add(e);
//                            System.out.println(e);
//
//                        }
//
//                    }
//                }
//            });

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
//                Post post = dataSnapshot.getValue(Post.class);
                    // ..
                    Entry e = dataSnapshot.getValue(Entry.class);
                    System.out.println(e);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("Error", "loadPost:onCancelled", databaseError.toException());
                }
            };
            myRef.addValueEventListener(postListener);

        }
    }
}
