package com.birzeit.advancecardealer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ViewReservesFragment extends Fragment {
    ADR_RecyclerViewAdapter adapter;

    private CarDBHelper carDB;
    private ArrayList<Car> carsRetrievedDetails;
    private ArrayList<Car> reservationsList;
    private String userEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carDB = new CarDBHelper(requireContext());
        carsRetrievedDetails = MainActivity.carDetails;
        reservationsList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_view_reserves, container, false);
        if (carsRetrievedDetails != null) {
            for (int i = 0; i < carsRetrievedDetails.size(); i++) {
                int carId = carsRetrievedDetails.get(i).getId();
                if (carDB.isCarReserved(carId)) {
                    reservationsList.add(carsRetrievedDetails.get(i));
                }
            }
            RecyclerView recyclerView = view.findViewById(R.id.reservationRecyclerView);
            adapter = new ADR_RecyclerViewAdapter(getContext(), reservationsList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        }

        return view;
    }
}