package com.example.lirikophadmin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        loadFragment(new Home()); // default tab

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                fragment = new Home();
            } else if (itemId == R.id.nav_artists) {
                fragment = new ArtistsList();
            } else if (itemId == R.id.nav_tickets) {
                fragment = new Tickets();
            } else if (itemId == R.id.nav_account) {
                fragment = new Account();
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }

//            String navigateTo = getIntent().getStringExtra("navigateTo");
//            if ("artists".equals(navigateTo)) {
//                bottomNavigation.setSelectedItemId(R.id.nav_artists);
//            }

            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }
}
