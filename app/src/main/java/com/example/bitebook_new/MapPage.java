package com.example.bitebook_new;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapPage extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    private MapView mapView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // Get a reference to the MapView and set it up
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        // Set up the Google Maps API client
        if (getContext() != null) {
            MapsInitializer.initialize(getContext());
            mapView.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // default
        gMap = googleMap;
        LatLng sutd = new LatLng(1.3413, 103.9638);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sutd));

        // get all the long, lat, name
        String uid = FirebaseHelper.getCurrentUserUID(getContext());
        Context context = getContext();

        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://bitebook-380210-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(uid + "/entries");

        gMap.getUiSettings().setZoomControlsEnabled(true);

        // Read the data from Firebase and populate the RecyclerView
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Entry> entries = new ArrayList<>();
                for (DataSnapshot entrySnapshot : dataSnapshot.getChildren()) {
                    Entry entry = entrySnapshot.getValue(Entry.class);
//                    entries.add(entry);
                    LatLng latlng = entry.getLatlng();
                    gMap.addMarker(new MarkerOptions().position(latlng).title(entry.getResName()).snippet(entry.getMenName()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error", "loadPost:onCancelled", error.toException());
            }
        });
    }
}