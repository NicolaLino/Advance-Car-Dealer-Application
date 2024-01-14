package com.birzeit.advancecardealer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AddAdminFragment extends Fragment {

    public AddAdminFragment() {
        // Required empty public constructor
    }
    TextView textViewPhoneAddAdmin;
    Spinner countrySpinner, citySpinner, genderSpinner;
    EditText emailEditText, firstNameEditText, lastNameEditText, passwordEditText, confirmPasswordEditText, phoneNumberEditText;
    Button buttonAddAdmin;
    LoginDBHelper db;
    GradientDrawable errorDrawable, normalDrawable;


    private Map<String, List<String>> citiesByCountry;
    private Map<String, String> phonePrefixByCountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);
        db = new LoginDBHelper(getActivity());
        countrySpinner = view.findViewById(R.id.countrySignupAddAdmin);
        citySpinner = view.findViewById(R.id.citySignupAddAdmin);
        genderSpinner = view.findViewById(R.id.genderSignupAddAdmin);
        emailEditText = view.findViewById(R.id.emailSignupAddAdmin);
        firstNameEditText = view.findViewById(R.id.firstNameSignupAddAdmin);
        lastNameEditText = view.findViewById(R.id.lastNameSignupAddAdmin);
        passwordEditText = view.findViewById(R.id.passwordSignupAddAdmin);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordSignupAddAdmin);
        phoneNumberEditText = view.findViewById(R.id.phoneNumberSignupAddAdmin);
        buttonAddAdmin = view.findViewById(R.id.buttonSignupActionAddAdmin);
        textViewPhoneAddAdmin = view.findViewById(R.id.textViewPhoneAddAdmin);

        String[] genderList = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Initialize drawables
        errorDrawable = new GradientDrawable();
        errorDrawable.setShape(GradientDrawable.RECTANGLE);
        errorDrawable.setStroke(8, Color.parseColor("#FFE80000")); // Stroke width and color
        errorDrawable.setCornerRadius(50);  // Corner radius

        normalDrawable = new GradientDrawable();
        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setStroke(8, Color.parseColor("#111414")); // Stroke width and color
        normalDrawable.setCornerRadius(50);  // Corner radius

        initializeData();

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<>(citiesByCountry.keySet()));
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = countrySpinner.getSelectedItem().toString();
                List<String> cities = citiesByCountry.get(selectedCountry);

                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cities);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);
                String selectedCountryPhone = countrySpinner.getSelectedItem().toString();
                String phonePrefix = phonePrefixByCountry.get(selectedCountryPhone);

                textViewPhoneAddAdmin.setText(phonePrefix);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String country = countrySpinner.getSelectedItem().toString();
                String city = citySpinner.getSelectedItem().toString();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                boolean isAdmin = true;


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
                        Toast.makeText(getActivity(), "Signup successful", Toast.LENGTH_SHORT).show();
                        // Clear all EditText fields
                        emailEditText.setText("");
                        firstNameEditText.setText("");
                        lastNameEditText.setText("");
                        passwordEditText.setText("");
                        confirmPasswordEditText.setText("");
                        phoneNumberEditText.setText("");

                        genderSpinner.setSelection(0);
                        countrySpinner.setSelection(0);
                        citySpinner.setSelection(0);
                    } else {
                        Toast.makeText(getActivity(), "Signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return view;
    }

    private boolean isValid(String email, String firstName, String lastName, String password, String confirmPassword, String phoneNumber) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,12}$");
        boolean returnValue = true;
        if (db.emailExist(email)) {
            Toast.makeText(getActivity(), "Email Already Exist", Toast.LENGTH_SHORT).show();
            emailEditText.setBackground(errorDrawable);
            returnValue = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Invalid email format", Toast.LENGTH_SHORT).show();
            emailEditText.setBackground(errorDrawable);
            returnValue = false;
        } else {
            emailEditText.setBackground(normalDrawable);
        }
        if (firstName.length() < 3 || firstName.length() > 10) {
            Toast.makeText(getActivity(), "First name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
            firstNameEditText.setBackground(errorDrawable);
            returnValue = false;
        } else {
            firstNameEditText.setBackground(normalDrawable);
        }
        if (lastName.length() < 3 || lastName.length() > 10) {
            Toast.makeText(getActivity(), "Last name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
            lastNameEditText.setBackground(errorDrawable);
            returnValue = false;
        } else {
            lastNameEditText.setBackground(normalDrawable);
        }
        if (!passwordPattern.matcher(password).matches()) {
            Toast.makeText(getActivity(), "Password must contain at least one number, one lowercase letter, and one uppercase letter, and be between 6 and 12 characters", Toast.LENGTH_SHORT).show();
            passwordEditText.setBackground(errorDrawable);
            returnValue = false;
        } else {
            passwordEditText.setBackground(normalDrawable);
        }
        if (!password.equals(confirmPassword) || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Password and Confirm Password must match", Toast.LENGTH_SHORT).show();
            confirmPasswordEditText.setBackground(errorDrawable);
            returnValue = false;
        } else {
            confirmPasswordEditText.setBackground(normalDrawable);
        }
        if (phoneNumber.length() < 7 || phoneNumber.length() > 10) {
            Toast.makeText(getActivity(), "Phone number must be between 7 and 10 digits", Toast.LENGTH_SHORT).show();
            phoneNumberEditText.setBackground(errorDrawable);
            returnValue = false;
        } else {
            phoneNumberEditText.setBackground(normalDrawable);
        }
        return returnValue;
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

}