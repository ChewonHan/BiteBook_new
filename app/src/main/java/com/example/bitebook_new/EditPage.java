package com.example.bitebook_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EditPage extends Fragment {

    ArrayList<Entry> entries;
    EditText restaurantName, menuName, price, foodMemo;
    TextView cancel;
    RatingBar rate;
    ImageView pictures;
    Button addPictures, update;
    Spinner areaSpinner, cuisineSpinner;
    TextView addPicDes;
    String area, cuisine;
    String image_url = null;
    Bitmap bitmap;
    private Context content;
    private Entry data;
    private int areaIdx;
    int cuisineIdx;
    LatLng latLng = new LatLng(0.2, 0.3);


    public EditPage(Context content, Entry data) {
        this.content = content;
        this.data = data;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_page, container, false);

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
        update = view.findViewById(R.id.updateButton);
        areaSpinner = view.findViewById(R.id.areaSpinner);
        cuisineSpinner = view.findViewById(R.id.cuisineSpinner);
        addPicDes = view.findViewById(R.id.addPicDes);
        cancel = view.findViewById(R.id.cancel);

        // show the inputs exist already
        restaurantName.setText(data.getResName());
        menuName.setText(data.getMenName());
        price.setText(Integer.toString((int) data.getPrice()));
        rate.setRating(data.getRating());
        foodMemo.setText(data.getReview());

        // set image depends on the current input
        if (data.getImage() == null){ }
        else{
            Picasso.get().load(data.getImage()).into(pictures);
        }

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
                addPicDes.setText("");
            }
        });

        // TODO doesn't work -> getting the index, although they are the same they returns false
        // get the index of the area to set the area in the spinner
        String[] areaList = getResources().getStringArray(R.array.areaSpinner);

        System.out.println("area " + data.getArea());
        for (int i =0; i < areaList.length; i ++ ){
            if (areaList[i].equals(data.getArea())){
                 areaIdx = i;
                 break;
            }
        }
        System.out.println(areaIdx);

        // get the index of the cuisine to set the cuisine in the spinner
        String[] cuisineList = getResources().getStringArray(R.array.cuisineSpinner);
        System.out.println("cuisine " + data.getCuisine());
        for (int i =0; i < cuisineList.length; i ++ ){
            if (cuisineList[i].equals(data.getCuisine())){
                cuisineIdx = i;
                break;
            }
        }
        System.out.println(cuisineIdx);

        areaSpinner.setSelection(areaIdx);
        cuisineSpinner.setSelection(cuisineIdx);

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get String input from the element
                String resName = restaurantName.getText().toString();
                String menName = menuName.getText().toString();
                float pri = Float.parseFloat(price.getText().toString());
                float rat = rate.getRating();
                String fooMemo = foodMemo.getText().toString();
                String id = data.getId();

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

                    // change the data in Entry
                    Entry entry = new Entry(id, resName, menName, pri, area, rat, fooMemo, cuisine, image_url, latLng);
                    FirebaseHelper.updateEntry(content, entry, bitmap);
                    Toast.makeText(getActivity(), "YUMMY ! Successfully updated your food", Toast.LENGTH_LONG).show();

                    // go back to homePage
                    HomePage homePage = new HomePage();
                    FragmentManager fragmentManager = ((AppCompatActivity) content).getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.drawer_layout, homePage).commit();

                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to homePage
                HomePage homePage = new HomePage();
                FragmentManager fragmentManager = ((AppCompatActivity) content).getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.drawer_layout, homePage).commit();
            }
        });

        return view;
    }


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
                // Save the Bitmap to the MyObject instance
//                Entry.setFoodImage(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

