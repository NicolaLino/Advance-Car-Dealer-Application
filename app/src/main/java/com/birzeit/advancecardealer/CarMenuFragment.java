package com.birzeit.advancecardealer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class CarMenuFragment extends Fragment {

    public CarMenuFragment() {
        // Required empty public constructor
    }

    CM_RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_car_menu, container, false);
        ArrayList<Car> carsRetrievedDetails = MainActivity.carDetails;
        if(carsRetrievedDetails != null){
            Log.d("TAG", "onCreateView: ArrayList is not empty");
            RecyclerView recyclerView = view.findViewById(R.id.mRecyclerView);
            adapter = new CM_RecyclerViewAdapter(getContext(), carsRetrievedDetails);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        }

        MaterialButton priceFilterBtn = view.findViewById(R.id.priceFilterBtn);
        MaterialButton allFilterBtn = view.findViewById(R.id.allFilterBtn);
        MaterialButton mileageFilterBtn = view.findViewById(R.id.mileageFilterBtn);
        MaterialButton transmissionFilterBtn = view.findViewById(R.id.transmissionTypeFilterBtn);
        MaterialButton fuelTypeFilterBtn = view.findViewById(R.id.fuelTypeFilterBtn);


        priceFilterBtn.setOnClickListener(v -> {
            if (adapter != null) {
                adapter.filterByPrice();
                priceFilterBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                priceFilterBtn.setTextColor(getResources().getColor(R.color.red));
            }
        });


        mileageFilterBtn.setOnClickListener(v -> {
            if (adapter != null) {
                adapter.showMileageFilterDialog();
                mileageFilterBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                mileageFilterBtn.setTextColor(getResources().getColor(R.color.red));
            }
        });


        transmissionFilterBtn.setOnClickListener(v -> {
            if (adapter != null) {
                adapter.showTransmissionTypeFilterDialog();
                transmissionFilterBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                transmissionFilterBtn.setTextColor(getResources().getColor(R.color.red));
            }
        });

        fuelTypeFilterBtn.setOnClickListener(v -> {
            if (adapter != null) {
                adapter.showFuelTypeFilterDialog();
                fuelTypeFilterBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                fuelTypeFilterBtn.setTextColor(getResources().getColor(R.color.red));
            }
        });

        allFilterBtn.setOnClickListener(v -> {
            if (adapter != null) {
                adapter.showAllCars();
                priceFilterBtn.setBackgroundColor(getResources().getColor(R.color.black));
                priceFilterBtn.setTextColor(getResources().getColor(R.color.white));
                mileageFilterBtn.setBackgroundColor(getResources().getColor(R.color.black));
                mileageFilterBtn.setTextColor(getResources().getColor(R.color.white));
                transmissionFilterBtn.setBackgroundColor(getResources().getColor(R.color.black));
                transmissionFilterBtn.setTextColor(getResources().getColor(R.color.white));
                fuelTypeFilterBtn.setBackgroundColor(getResources().getColor(R.color.black));
                fuelTypeFilterBtn.setTextColor(getResources().getColor(R.color.white));
            }

        });

        return view;
    }



}