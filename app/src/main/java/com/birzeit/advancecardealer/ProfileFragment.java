package com.birzeit.advancecardealer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;


public class ProfileFragment extends Fragment {

    EditText etFirstName, etLastName,etTelNumber, etPassword, etConfPassword;
    Button btnUpdate;
    LoginDBHelper loginDBHelper;
    Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,12}$");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        etFirstName = view.findViewById(R.id.firstNameProfile);
        etLastName = view.findViewById(R.id.lastNameProfile);
        etPassword = view.findViewById(R.id.passwordProfile);
        etConfPassword = view.findViewById(R.id.confirmPasswordProfile);
        etTelNumber=view.findViewById(R.id.phoneNumberProfile);
        btnUpdate = view.findViewById(R.id.button_save);

        loginDBHelper = new LoginDBHelper(getActivity());

        // Replace with the actual logged-in user's email
        String email = LoginPage.emailStr;

        Cursor userDetails = loginDBHelper.getUserDetails(email);
        if (userDetails.moveToFirst()) {
            etFirstName.setText(userDetails.getString(userDetails.getColumnIndex(LoginDBHelper.COL_FIRSTNAME)));
            etLastName.setText(userDetails.getString(userDetails.getColumnIndex(LoginDBHelper.COL_LASTNAME)));
            etTelNumber.setText(userDetails.getString(userDetails.getColumnIndex(LoginDBHelper.COL_PHONE)));
            //etPassword.setText(userDetails.getString(userDetails.getColumnIndex(LoginDBHelper.COL_PASSWORD)));
        }



        btnUpdate.setOnClickListener(v -> {
            String newFirstName = etFirstName.getText().toString();
            String newLastName = etLastName.getText().toString();
            String newPhoneNumber = etTelNumber.getText().toString();
            String newPassword = etPassword.getText().toString();
            String newPasswordConf = etConfPassword.getText().toString();

            if (!newFirstName.isEmpty() && !newLastName.isEmpty() && !newPhoneNumber.isEmpty() && !newPassword.isEmpty()) {

                if (newFirstName.length() < 3 || newFirstName.length() > 10) {
                    Toast.makeText(getActivity(), "First name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
                }else if (newLastName.length() < 3 || newLastName.length() > 10) {
                    Toast.makeText(getActivity(), "Last name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
                }else if (!passwordPattern.matcher(newPassword).matches()) {
                    Toast.makeText(getActivity(), "Password must contain at least one number, one lowercase letter, and one uppercase letter, and be between 6 and 12 characters", Toast.LENGTH_SHORT).show();
                } else if(newPhoneNumber.length() < 7 || newPhoneNumber.length() > 10){
                    Toast.makeText(getActivity(), "Phone number must be between 7 and 10 digits", Toast.LENGTH_SHORT).show();
                }else if(!newPassword.equals(newPasswordConf)){
                    Toast.makeText(getActivity(), "Passwords do not match!!", Toast.LENGTH_SHORT).show();
                } else {
                    loginDBHelper.updateUserDetails(email, newFirstName, newLastName, newPhoneNumber, newPassword);
                    Toast.makeText(getActivity(), "User details changed successfully!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please fill all fields!!", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}