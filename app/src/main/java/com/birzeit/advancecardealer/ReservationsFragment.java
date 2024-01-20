package com.birzeit.advancecardealer;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ReservationsFragment extends Fragment {

    public ReservationsFragment() {
        // Required empty public constructor
    }

    CM_RecyclerViewAdapter adapter;

    private CarDBHelper carDB;
    private ArrayList<Car> carsRetrievedDetails;
    private ArrayList<Car> reservationsList;
    private String userEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carDB = new CarDBHelper(requireContext());
        userEmail = LoginPage.emailStr;
        carsRetrievedDetails = MainActivity.carDetails;
        reservationsList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reservations, container, false);
        if (carsRetrievedDetails != null) {
            for (int i = 0; i < carsRetrievedDetails.size(); i++) {
                int carId = carsRetrievedDetails.get(i).getId();
                if (carDB.isReserved(userEmail, carId)) {
                    reservationsList.add(carsRetrievedDetails.get(i));
                }
            }
            RecyclerView recyclerView = view.findViewById(R.id.reservationRecyclerView);
            adapter = new CM_RecyclerViewAdapter(getContext(), reservationsList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        }

        return view;
    }
}