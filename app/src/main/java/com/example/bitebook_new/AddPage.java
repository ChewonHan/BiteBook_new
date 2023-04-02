package com.example.bitebook_new;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddPage extends Fragment {

    // use the elements created to get the user inputs and save it to variables
    // initialize elements
    ArrayList<Entry> entries;
    EditText restaurantName;
    EditText menuName;
    EditText price;
    RatingBar rate;
    ImageView pictures;
    Button addPictures;
    EditText foodMemo;
    Button upload;
    Spinner areaSpinner;
    String area;
    Spinner cuisineSpinner;
    String cuisine;

    // all inputs will be saved into this HashMap
    public HashMap<String,List<String>> inputs = new HashMap<>();


    // TODO 3 try to get image from the gallery with the limit of images
    // TODO <ADDITIONAL> change the app icon image

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_page, container, false);

        // initialize each key for further inputs from users
        entries = new ArrayList<>();

        // elements' ids with the elements in fragment
        restaurantName = view.findViewById(R.id.restaurantName);
        menuName = view.findViewById(R.id.menuName);
        price = view.findViewById(R.id.price);
        rate = view.findViewById(R.id.ratingBar);
        pictures = view.findViewById(R.id.imageAdd);
        addPictures = view.findViewById(R.id.addPicture);
        foodMemo = view.findViewById(R.id.foodMemo);
        upload = view.findViewById(R.id.upload);
        areaSpinner = view.findViewById(R.id.areaSpinner);
        cuisineSpinner = view.findViewById(R.id.cuisineSpinner);

        // set up the spinners
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getContext(), R.array.areaSpinner, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getContext(), R.array.cuisineSpinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(adapter2);



        // save user inputs from spinners
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cuisine = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cuisine = null;
            }
        });

        cuisineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                area = null;
            }
        });



        // when the upload button is clicked the string inputs in each element will be saved in to specific var
        upload.setOnClickListener(new View.OnClickListener() {


            // TODO 3.1 add onClick for adding pictures -> show the pictures added

            @Override
            public void onClick(View view) {
                // get String input from the element
                String resName = restaurantName.getText().toString();
                String menName = menuName.getText().toString();
                String pri = price.getText().toString();
                float rat = rate.getRating();
                String fooMemo = foodMemo.getText().toString();

                // check any of necessary inputs are empty/ missing
                if (resName.isEmpty() ||
                menName.isEmpty() ||
                pri.isEmpty() ||
                rat == 0.0 ||
                area == null ||
                cuisine == null){
                    // if the resName is empty then show a message
                    Toast.makeText(getActivity(), "Please fill in the blanks", Toast.LENGTH_LONG).show();
                }
                else{
                    // if food memo is empty then save null instead
                    if (fooMemo.isEmpty()){
                        fooMemo = null;
                    }

                    // save the user inputs as an object called Entry
                    Entry food = new Entry(resName, menName, pri, area, cuisine, rat, fooMemo);
                    entries.add(food);

                    System.out.println(food);
                }
            }
        });

        return view;
    }
}
