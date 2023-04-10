package com.example.bitebook_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bitebook_new.adapter.PlaceAutoSuggestAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddPage extends Fragment {

    // use the elements created to get the user inputs and save it to variables
    // initialize elements
    ArrayList<Entry> entries;
    AutoCompleteTextView restaurantName;
    PlaceAutoSuggestAdapter adapter;
    TextView responseView;
    PlacesClient placesClient;
    EditText menuName;
    EditText price;
    RatingBar rate;
    ImageView pictures;
    TextView addPictures;
    EditText foodMemo;
    Button upload;
    Spinner areaSpinner;
    String area;
    Spinner cuisineSpinner;
    String cuisine;
    Bitmap bitmap;
    TextView addPicDes;
    String image_url = null;
    LatLng latLng;

    @SuppressLint("MissingInflatedId")
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
        addPicDes = view.findViewById(R.id.addPicDes);

        //set up the autofill response
        String apiKey = BuildConfig.KEY;
        System.out.println(apiKey);
        if (apiKey.isEmpty()) {
            responseView.setText("Error");
            return null;
        }

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), apiKey);
        }

        placesClient = Places.createClient(getContext());
        initPlaceAutoSuggestAdapter();

        // set up the spinners
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getContext(), R.array.areaSpinner, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getContext(), R.array.cuisineSpinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpinner.setAdapter(adapter2);

        addPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                // set the height of the picture
                int sizeInPixels = getResources().getDimensionPixelSize(R.dimen.picture_height);
                pictures.getLayoutParams().height = sizeInPixels;

                startActivityForResult(intent, 1);
            }
        });


        // save user inputs from spinners
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                area = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                area = null;
            }
        });

        cuisineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cuisine = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cuisine = null;
            }
        });



        // when the upload button is clicked the string inputs in each element will be saved in to specific var
        upload.setOnClickListener(new View.OnClickListener() {

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
                        price.getText().toString().isEmpty() ||
                        rat == 0.0 ||
                        area == null ||
                        cuisine == null) {
                    // if the resName is empty then show a message
                    Toast.makeText(getActivity(), "Please fill in the blanks", Toast.LENGTH_LONG).show();
                } else {
                    // if food memo is empty then save null instead
                    if (fooMemo.isEmpty()) {
                        fooMemo = null;
                    }

                    // save the user inputs as an object called Entry

                    Entry entry = new Entry(resName, menName, Float.parseFloat(pri), area, rat, fooMemo, cuisine, image_url, latLng);
                    FirebaseHelper.createEntry(getContext(), entry, bitmap);

                    // After uploading the food, show the message through the toast
                    Toast.makeText(getActivity(), "YUMMY ! Successfully recorded your food", Toast.LENGTH_LONG).show();

                    // After uploading the food, make all the fill-in blanks as empty
                    restaurantName.setText("");
                    menuName.setText("");
                    price.setText("");
                    rate.setRating(0);
                    foodMemo.setText("");
                    pictures.setImageDrawable(null);
                    areaSpinner.setSelection(0);
                    cuisineSpinner.setSelection(0);
                    pictures.getLayoutParams().height = 0;
                    addPictures.getLayoutParams().height = 150;
                    pictures.requestLayout();
                }
            }
        });

        return view;
    }


    //this method is called when the user inputs text into the AutoComplete Text field
    private void initPlaceAutoSuggestAdapter(){
        restaurantName.setThreshold(1);
        restaurantName.setOnItemClickListener(autocompleteClickListener);
        adapter = new PlaceAutoSuggestAdapter(getContext(), placesClient);
        restaurantName.setAdapter(adapter);
    }

    //this method is the on click listener for the autofill text field
    private final AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
//                            responseView.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                            latLng = task.getPlace().getLatLng();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            responseView.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // This method is called when the user selects an image from their gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            // Get the Uri of the selected image
            Uri selectedImage = data.getData();

            try {
                image_url = FirebaseHelper.generateRandomString();
                // Use the ContentResolver to get a Bitmap from the Uri
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                // Set the Bitmap to the ImageView
                pictures.setImageBitmap(bitmap);
//                pictures.setLayoutParams(
//                        new ViewGroup.LayoutParams(
//                                // or ViewGroup.LayoutParams.WRAP_CONTENT
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                // or ViewGroup.LayoutParams.WRAP_CONTENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT ) );
                pictures.getLayoutParams().height = 600;
                addPictures.getLayoutParams().height = 0;

//                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(same,500);
                pictures.requestLayout();

                // Save the Bitmap to the MyObject instance
//                Entry.setFoodImage(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] pictureData = baos.toByteArray();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();
                // Create a child reference
                // imagesRef now points to "images"
                StorageReference imagesRef = storageRef.child(image_url);

                // Child references can also take paths
                // spaceRef now points to "images/space.jpg
                // imagesRef still points to "images"
                UploadTask uploadTask = imagesRef.putBytes(pictureData);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                image_url = uri.toString();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                });



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}