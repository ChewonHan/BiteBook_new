package com.example.bitebook_new;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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


//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_map);
//        // Obtain the SupportFragment and get notified when the nap is ready to be used
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
////        mapFragment.getMapAsync( onMapReadyCallback.this);
//    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        LatLng sutd = new LatLng(1.3413, 103.9638);
        gMap.addMarker(new MarkerOptions().position(sutd).title("Merker at SUTD"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sutd));
    }

}