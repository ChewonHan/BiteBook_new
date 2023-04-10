package com.example.bitebook_new;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView foodNumber, favNumber, userName, userId, logoutText;
    ImageView logoutIcon, logoutButton;
    ArrayList<Entry> entries = new ArrayList<>();
    Entry entry;
    private FirebaseAuth auth;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the Firebase Realtime Database
        String uid = FirebaseHelper.getCurrentUser(this);
        Context context = getApplicationContext();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://bitebook-380210-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference(uid + "/entries");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entries.clear();

                for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                    entry = entrySnapshot.getValue(Entry.class);
                    entries.add(entry);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error", "loadPost:onCancelled", error.toException());
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.drawerMenu);
        navigationView.setNavigationItemSelectedListener(this);

        // show the navigation drawer for the profile info
        findViewById(R.id.profileButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // when the button is clicked, open the slide menu
                drawerLayout.openDrawer(GravityCompat.START);

                // set the user name and ID
                userName = findViewById(R.id.userName);
                userId = findViewById(R.id.userId);
                // if the user used google account:
                GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(context);
                String username = gAccount != null ? gAccount.getDisplayName() : FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String email = gAccount != null ? gAccount.getEmail() : FirebaseAuth.getInstance().getCurrentUser().getEmail();

                userName.setText(username);
                userId.setText(email);


                // show the number of food tried and
                foodNumber = findViewById(R.id.foodNumber);
                favNumber = findViewById(R.id.favNumber);

                foodNumber.setText("I have eaten " + Integer.toString(entries.size()) + " different foods");
                favNumber.setText(favFoodNum() + "  menus have received 5 stars");


            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        auth = FirebaseAuth.getInstance();
        builder = new AlertDialog.Builder(context);


        GoogleSignInOptions gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient gClient = GoogleSignIn.getClient(this, gOptions);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        Toast.makeText(MainActivity.this, "Logout successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        // bottom nav bar
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

    }

    // from here, the code below is for the bottom navigation bar
    HomePage homePage = new HomePage();
    AddPage addPage = new AddPage();
    DecidePage decidePage = new DecidePage();
    MapPage mapPage = new MapPage();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, addPage).commit();
                return true;

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, homePage).commit();
                return true;

            case R.id.decide:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, decidePage).commit();
                return true;

            case R.id.map:
                getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, mapPage).commit();
                return true;
        }
        return false;
    }

    public int favFoodNum(){
        ArrayList<Entry> favFoods = new ArrayList<>();
        for (Entry e : entries){
            if (e.getRating() == 5.0){
                favFoods.add(e);
            }
        }
        return favFoods.size();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
