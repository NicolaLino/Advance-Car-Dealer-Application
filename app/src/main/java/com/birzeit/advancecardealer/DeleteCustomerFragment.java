package com.birzeit.advancecardealer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteCustomerFragment extends Fragment {

    private LoginDBHelper dbHelper; // Make sure to initialize this variable
    private CarDBHelper carDBHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_customer, container, false);

        // Find the EditText and Button widgets
        EditText editTextEmailToDelete = view.findViewById(R.id.editTextEmailToDelete);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        // Initialize dbHelper (replace YourActivity with the actual name of your activity)
        dbHelper = new LoginDBHelper(getActivity());
        carDBHelper= new CarDBHelper(getActivity());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailToDelete = editTextEmailToDelete.getText().toString();
                boolean isDeleted = dbHelper.deleteUser(emailToDelete,carDBHelper);
                if (emailToDelete.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter an email", Toast.LENGTH_SHORT).show();
                } else {
                    if (isDeleted) {
                        Toast.makeText(getActivity(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "User not found or deletion failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }


}
