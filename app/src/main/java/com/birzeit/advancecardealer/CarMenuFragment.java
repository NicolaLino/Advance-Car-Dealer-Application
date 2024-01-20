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
        priceFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adapter.filterByPrice();
            }
        });

        return view;
    }

}