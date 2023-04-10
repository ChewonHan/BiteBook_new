package com.example.bitebook_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
import java.util.List;
import java.util.Random;
import java.util.Random;

public class DecidePage extends Fragment {

    RecyclerView decideRecycler;
    RecyclerView.Adapter adapter;
    Spinner areaSpinner;
    Spinner cuisineSpinner;
    Spinner priceSpinner;
    Button generateButton;
    String cuisine;
    String price;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<Entry> filteredEntries = new ArrayList<>();


    public DecidePage() {
        // require a empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decide_page, container, false);

        areaSpinner = view.findViewById(R.id.areaSpinner);
        cuisineSpinner = view.findViewById(R.id.cuisineSpinner);
        priceSpinner = view.findViewById(R.id.priceSpinner);


        // set up the spinners
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getContext(), R.array.areaSpinner, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getContext(), R.array.cuisineSpinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                getContext(), R.array.priceSpinner, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(adapter3);


        decideRecycler = (RecyclerView) view.findViewById(R.id.decideRecycler);
        Context context = container.getContext();


        // getting data from the spinners
        cuisineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cuisine = adapterView.getItemAtPosition(i).toString();
                updateDecideCards();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cuisine = null;
            }
        });

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateDecideCards();
                price = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                price = null;
            }
        });


        // first we want to get the data from firebase
        String uid = FirebaseHelper.getCurrentUser(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://bitebook-380210-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(uid + "/entries");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    Entry entry = entrySnapshot.getValue(Entry.class);
                    entries.add(entry);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error", "loadPost:onCancelled", error.toException());
            }
        });

        generateButton = view.findViewById(R.id.generateButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int menuNumber = RandomNumberGenerator();
            }
        });

        return view;
    }

    public void updateDecideCards() {
        filteredEntries.clear();

        for (Entry e : entries) {
            if (e.getCuisine().equals(cuisine)) {
                filteredEntries.add(e);
            }
        }
        showCards();
    }

    public void showCards() {
        System.out.println(filteredEntries.size());
        Context context = getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        decideRecycler.setLayoutManager(layoutManager);
        adapter = new DecideCardAdapter(context, filteredEntries);
        decideRecycler.setAdapter(adapter);
    }

    public int RandomNumberGenerator() {
        // Create a new instance of the Random class
        Random rand = new Random();

        // Generate a random integer between 0 and total number of the Entry object
        int randomNumber = rand.nextInt(entries.size());

        return randomNumber;
    }

}
