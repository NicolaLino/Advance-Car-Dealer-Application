package com.birzeit.advancecardealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class NavigationAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_admin);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout= findViewById(R.id.drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.username);
        TextView emailTextView = headerView.findViewById(R.id.email);

        // Get user email. Replace 'userEmail' with the actual user's email.
        String userEmail = LoginPage.emailStr; // Replace this with the email used to login

        // Fetch user details from the database
        LoginDBHelper dbHelper = new LoginDBHelper(this);
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


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new HomeAdminFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.admin_nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new HomeAdminFragment()).commit();
        } else if (itemId == R.id.admin_nav_delete) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new DeleteCustomerFragment()).commit();
        } else if (itemId == R.id.admin_nav_add) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new AddAdminFragment()).commit();
        } else if (itemId == R.id.admin_nav_view_reserves) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ViewReservesFragment()).commit();
        } else if (itemId == R.id.admin_nav_logout) {
            Intent returnToMain = new Intent(NavigationAdmin.this, LoginPage.class);
            startActivity(returnToMain);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

}
