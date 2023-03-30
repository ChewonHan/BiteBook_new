package com.example.bitebook_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.add);

    }
    HomePage homePage = new HomePage();
    AddPage addPage = new AddPage();
    DecidePage decidePage = new DecidePage();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, addPage).commit();
                return true;

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homePage).commit();
                return true;

            case R.id.decide:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, decidePage).commit();
                return true;
        }
        return false;
    }
}
