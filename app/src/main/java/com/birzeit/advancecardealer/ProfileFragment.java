package com.birzeit.advancecardealer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    ImageView profilePic;
    EditText etFirstName, etLastName, etTelNumber, etPassword, etConfPassword;
    Button btnUpdate;
    LoginDBHelper loginDBHelper;
    Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,12}$");
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri profileImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePic = view.findViewById(R.id.profile_image_view);
        etFirstName = view.findViewById(R.id.firstNameProfile);
        etLastName = view.findViewById(R.id.lastNameProfile);
        etPassword = view.findViewById(R.id.passwordProfile);
        etConfPassword = view.findViewById(R.id.confirmPasswordProfile);
        etTelNumber = view.findViewById(R.id.phoneNumberProfile);
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

            // Load the saved image URI if available
            String imageUrl = userDetails.getString(userDetails.getColumnIndex(LoginDBHelper.COL_IMAGE_URL));
            profileImageUri=Uri.parse(imageUrl);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                //profileImageUri = Uri.parse(imageUrl);
                profilePic.setImageURI(profileImageUri);
                Picasso.get().load(imageUrl).resize(0, 60).into(profilePic);
                //profilePic.setImageURI(profileImageUri);
            }

//                profileImageUri= Uri.parse("https://media.istockphoto.com/id/1221023970/photo/small-red-brick-house-with-green-grass.webp?s=612x612&w=is&k=20&c=eENSwiZty5RuN7ex1oCsCXzgbsHSCoT9s1VeoJyjGtU=");
//                Picasso.get().load(profileImageUri).resize(0, 60).into(profilePic);

        }

        profilePic.setOnClickListener(v -> openFileChooser());

        btnUpdate.setOnClickListener(v -> {
            String newFirstName = etFirstName.getText().toString();
            String newLastName = etLastName.getText().toString();
            String newPhoneNumber = etTelNumber.getText().toString();
            String newPassword = etPassword.getText().toString();
            String newPasswordConf = etConfPassword.getText().toString();

            if (!newFirstName.isEmpty() && !newLastName.isEmpty() && !newPhoneNumber.isEmpty() && !newPassword.isEmpty()) {

                if (newFirstName.length() < 3 || newFirstName.length() > 10) {
                    Toast.makeText(getActivity(), "First name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
                } else if (newLastName.length() < 3 || newLastName.length() > 10) {
                    Toast.makeText(getActivity(), "Last name must be between 3 and 10 characters", Toast.LENGTH_SHORT).show();
                } else if (!passwordPattern.matcher(newPassword).matches()) {
                    Toast.makeText(getActivity(), "Password must contain at least one number, one lowercase letter, and one uppercase letter, and be between 6 and 12 characters", Toast.LENGTH_SHORT).show();
                } else if (newPhoneNumber.length() < 7 || newPhoneNumber.length() > 10) {
                    Toast.makeText(getActivity(), "Phone number must be between 7 and 10 digits", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(newPasswordConf)) {
                    Toast.makeText(getActivity(), "Passwords do not match!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (profileImageUri != null) {
                        // Save the image URI in the database
                        // Convert URI to string before saving if necessary
                        String imageUriString = profileImageUri.toString();
                        loginDBHelper.updateUserDetailsWithImage(email, newFirstName, newLastName, newPhoneNumber, newPassword, imageUriString);
                    } else {
                        // Save in the database without the image
                        loginDBHelper.updateUserDetails(email, newFirstName, newLastName, newPhoneNumber, newPassword);
                    }
                    Toast.makeText(getActivity(), "User details changed successfully!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please fill all fields!!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            // Handle the selected image, you may display it in the ImageView
            profilePic.setImageURI(profileImageUri);
        }
    }
}
