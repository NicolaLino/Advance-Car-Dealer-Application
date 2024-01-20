package com.birzeit.advancecardealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class NavigationCustomer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_customer);

        // Initialize DBHelper
        LoginDBHelper dbHelper = new LoginDBHelper(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.username);
        TextView emailTextView = headerView.findViewById(R.id.email);

        // Get user email. Replace 'userEmail' with the actual user's email.
        String userEmail = LoginPage.emailStr; // Replace this with the email used to login

        // Fetch user details from the database
        Cursor cursor = dbHelper.getUserDetails(userEmail);

        if (cursor != null && cursor.moveToFirst()) {
            int firstNameColIndex = cursor.getColumnIndex(LoginDBHelper.COL_FIRSTNAME);
            int lastNameColIndex = cursor.getColumnIndex(LoginDBHelper.COL_LASTNAME);
            int emailColIndex = cursor.getColumnIndex(LoginDBHelper.COL_EMAIL);

            if (firstNameColIndex != -1 && lastNameColIndex != -1 && emailColIndex != -1) {
                String firstName = cursor.getString(firstNameColIndex);
                String lastName = cursor.getString(lastNameColIndex);
                String email = cursor.getString(emailColIndex);

                // Set the values to the TextViews
                usernameTextView.setText(String.format("%s %s", firstName, lastName));
                emailTextView.setText(email);
            }
            cursor.close();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
          getSupportActionBar().setTitle("Home");
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeCustomerFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        String fragmentTitle = "";

        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeCustomerFragment()).commit();
            fragmentTitle = "Home";
        } else if (itemId == R.id.nav_car_menu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarMenuFragment()).commit();
            fragmentTitle = "Car Menu";
        } else if (itemId == R.id.nav_favorites) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoritesFragment()).commit();
            fragmentTitle = "Favorites";
        } else if (itemId == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            fragmentTitle = "Profile";
        } else if (itemId == R.id.nav_reservations) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReservationsFragment()).commit();
            fragmentTitle = "Reservations";
        } else if (itemId == R.id.nav_special_offers) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpecialOffersFragment()).commit();
            fragmentTitle = "Special Offers";
        } else if (itemId == R.id.nav_call_us) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CallUsFragment()).commit();
            fragmentTitle = "Call Us";
        } else if (itemId == R.id.nav_logout) {
            Intent returnToLogin = new Intent(NavigationCustomer.this, LoginPage.class);
            startActivity(returnToLogin);
        }

        // Set the toolbar title based on the selected fragment
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(fragmentTitle);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}