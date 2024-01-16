package com.birzeit.advancecardealer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ReservationsFragment extends Fragment {

    public ReservationsFragment() {
        // Required empty public constructor
    }

    RF_RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reservations, container, false);
        ArrayList<Car> carsRetrievedDetails = MainActivity.carDetails;
        if(carsRetrievedDetails != null){
            androidx.recyclerview.widget.RecyclerView recyclerView = view.findViewById(R.id.reservationRecyclerView);
            adapter = new RF_RecyclerViewAdapter(getContext(), carsRetrievedDetails);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        }

        return view;
    }
}