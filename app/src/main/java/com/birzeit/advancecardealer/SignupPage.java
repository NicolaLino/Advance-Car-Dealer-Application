package com.birzeit.advancecardealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupPage extends AppCompatActivity {

    private Spinner countrySpinner, citySpinner, genderSpinner;
    private TextView phoneNumberEditText;
    private Map<String, List<String>> citiesByCountry;
    private Map<String, String> phonePrefixByCountry;
    private Button buttonBackToLogin, buttonSignupAction;
    EditText emailSignUp, firstNameSignUp, lastNameSignUp, passwordSignUp, confirmPasswordSignUp, phoneNumberSignup;
    GradientDrawable errorDrawable = new GradientDrawable();
    GradientDrawable normalDrawable = new GradientDrawable();
    LoginDBHelper db;
    LinearLayout phoneNumberLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        db = new LoginDBHelper(this);
        emailSignUp = findViewById(R.id.emailSignup);
        firstNameSignUp = findViewById(R.id.firstNameSignup);
        lastNameSignUp = findViewById(R.id.lastNameSignup);
        passwordSignUp = findViewById(R.id.passwordSignup);
        confirmPasswordSignUp = findViewById(R.id.confirmPasswordSignup);
        phoneNumberSignup = findViewById(R.id.phoneNumberSignup);

        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);
        buttonSignupAction = findViewById(R.id.buttonSignupAction);

        errorDrawable.setShape(GradientDrawable.RECTANGLE);
        errorDrawable.setStroke(8, Color.parseColor("#FFE80000")); // Stroke width and color
        errorDrawable.setCornerRadius(50);  // Corner radius
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);



        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setStroke(8, Color.parseColor("#111414")); // Stroke width and color
        normalDrawable.setCornerRadius(50);  // Corner radius


        // gender spinner
        genderSpinner = findViewById(R.id.genderSignup);
        String[] genderList = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        countrySpinner = findViewById(R.id.countrySignup);
        citySpinner = findViewById(R.id.citySignup);
        phoneNumberEditText = findViewById(R.id.textViewPhone);

        initializeData(); // initialize data for country and city spinner

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(citiesByCountry.keySet()));
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateCitySpinner();
                updatePhoneNumberPrefix();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to Login Activity
                Intent loginIntent = new Intent(SignupPage.this, LoginPage.class);
                startActivity(loginIntent);
            }
        });

        buttonSignupAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailSignUp.getText().toString().trim();
                String firstName = firstNameSignUp.getText().toString().trim();
                String lastName = lastNameSignUp.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();
                String password = passwordSignUp.getText().toString().trim();
                String confirmPassword = confirmPasswordSignUp.getText().toString().trim();
                String country = countrySpinner.getSelectedItem().toString();
                String city = citySpinner.getSelectedItem().toString();
                String phoneNumber = phoneNumberSignup.getText().toString().trim();
                boolean isAdmin = false;




                if (isValid(email, firstName, lastName, password, confirmPassword, phoneNumber)) {
                    Log.d("SignUpData", "Email: " + email);
                    Log.d("SignUpData", "First Name: " + firstName);
                    Log.d("SignUpData", "Last Name: " + lastName);
                    Log.d("SignUpData", "Gender: " + gender);
                    Log.d("SignUpData", "Password: " + password);
                    Log.d("SignUpData", "Confirm Password: " + confirmPassword);
                    Log.d("SignUpData", "Country: " + country);
                    Log.d("SignUpData", "City: " + city);
                    Log.d("SignUpData", "Phone Number: " + phoneNumber);
                    Log.d("SignUpData", "isAdmin: " + isAdmin);

                    if (db.addUser(email, firstName, lastName, gender, password, country, city, phoneNumber, isAdmin)) {
                        // Redirect to Login Activity
                        Intent loginIntent = new Intent(SignupPage.this, LoginPage.class);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(SignupPage.this, "Signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void initializeData() {
        citiesByCountry = new HashMap<>();
        citiesByCountry.put("Palestine", new ArrayList<String>() {{
            add("Ramallah");
            add("Bethlehem");
            add("Hebron");
        }});
        citiesByCountry.put("Jordan", new ArrayList<String>() {{
            add("Amman");
            add("Zarqa");
            add("Irbid");
        }});
        citiesByCountry.put("Egypt", new ArrayList<String>() {{
            add("Cairo");
            add("Alexandria");
            add("Giza");
        }});
        citiesByCountry.put("Lebanon", new ArrayList<String>() {{
            add("Beirut");
            add("Tripoli");
            add("Sidon");
        }});
        citiesByCountry.put("Syria", new ArrayList<String>() {{
            add("Damascus");
            add("Aleppo");
            add("Homs");
        }});

        phonePrefixByCountry = new HashMap<>();
        phonePrefixByCountry.put("Palestine", "+970");
        phonePrefixByCountry.put("Jordan", "+962");
        phonePrefixByCountry.put("Egypt", "+20");
        phonePrefixByCountry.put("Lebanon", "+961");
        phonePrefixByCountry.put("Syria", "+963");
    }
    private void updateCitySpinner() {
        String selectedCountry = countrySpinner.getSelectedItem().toString();
        List<String> cities = citiesByCountry.get(selectedCountry);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
    }

    private void updatePhoneNumberPrefix() {
        String selectedCountry = countrySpinner.getSelectedItem().toString();
        String phonePrefix = phonePrefixByCountry.get(selectedCountry);

        phoneNumberEditText.setText(phonePrefix);
    }

    private boolean isValid(String email, String firstName, String lastName, String password, String confirmPassword, String phoneNumber) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,12}$");
        boolean returnValue = true;
        if (db.emailExist(email)) {
            Toast.makeText(this, "Email Already Exist", Toast.LENGTH_SHORT).show();
            emailSignUp.setBackground(errorDrawable);
            returnValue = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            emailSignUp.setBackground(errorDrawable);
            returnValue = false;
        } else {
            emailSignUp.setBackground(normalDrawable);
        }
        if (firstName.length() < 3 || firstName.length() > 10) {
            Toast.makeText(this, "First name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
            firstNameSignUp.setBackground(errorDrawable);
            returnValue = false;
        } else {
            firstNameSignUp.setBackground(normalDrawable);
        }
        if (lastName.length() < 3 || lastName.length() > 10) {
            Toast.makeText(this, "Last name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
            lastNameSignUp.setBackground(errorDrawable);
            returnValue = false;
        } else {
            lastNameSignUp.setBackground(normalDrawable);
        }
        if (!passwordPattern.matcher(password).matches()) {
            Toast.makeText(this, "Password must contain at least one number, one lowercase letter, and one uppercase letter, and be between 6 and 12 characters", Toast.LENGTH_SHORT).show();
            passwordSignUp.setBackground(errorDrawable);
            returnValue = false;
        } else {
            passwordSignUp.setBackground(normalDrawable);
        }
        if (!password.equals(confirmPassword) || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Password and Confirm Password must match", Toast.LENGTH_SHORT).show();
            confirmPasswordSignUp.setBackground(errorDrawable);
            returnValue = false;
        } else {
            confirmPasswordSignUp.setBackground(normalDrawable);
        }
        if (phoneNumber.length() < 7 || phoneNumber.length() > 10) {
            Toast.makeText(this, "Phone number must be between 7 and 10 digits", Toast.LENGTH_SHORT).show();
            phoneNumberSignup.setBackground(errorDrawable);
            returnValue = false;
        } else {
            phoneNumberSignup.setBackground(normalDrawable);
        }
        return returnValue;
    }

}