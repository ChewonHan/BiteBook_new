package com.example.bitebook_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HomePage extends Fragment {
    RecyclerView homeRecycler;
    RecyclerView.Adapter adapter;
    TextView noEntry;
    ArrayList<Entry> entries = new ArrayList<>();


    public HomePage() {}


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        String uid = FirebaseHelper.getCurrentUser(getContext());
        Context context = container.getContext();
        homeRecycler = (RecyclerView) view.findViewById(R.id.homeRecycler);

        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://bitebook-380210-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(uid + "/entries");

        // Read the data from Firebase and populate the RecyclerView
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                entries.clear();

                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    Entry entry = entrySnapshot.getValue(Entry.class);
                    entries.add(entry);
                }

                // reverse order
                Collections.reverse(entries);

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                homeRecycler.setLayoutManager(layoutManager);
                adapter = new HomeCardAdapter(context, entries);
                homeRecycler.setAdapter(adapter);

                noEntry = view.findViewById(R.id.noEntry);

                if (entries.size() != 0){
                    noEntry.setVisibility(View.GONE);
                }
                else{
                    noEntry.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error", "loadPost:onCancelled", error.toException());
            }
        });



        return view;
    }
}
