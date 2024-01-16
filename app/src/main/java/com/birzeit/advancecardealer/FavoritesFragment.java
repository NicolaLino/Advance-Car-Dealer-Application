package com.birzeit.advancecardealer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private CarDBHelper carDB;
    private ArrayList<Car> carsRetrievedDetails;
    private ArrayList<Car> favCars;
    private String userEmail;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carDB = new CarDBHelper(requireContext());
        userEmail = LoginPage.emailStr;
        carsRetrievedDetails = MainActivity.carDetails;
        favCars = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_menu, container, false);

        if (carsRetrievedDetails != null) {
            for (int i = 0; i < carsRetrievedDetails.size(); i++) {
                int carId = carsRetrievedDetails.get(i).getId();
                if (carDB.isFavorite(userEmail, carId)) {
                    favCars.add(carsRetrievedDetails.get(i));
                }
            }
            Log.d("TAG", "onCreateView: ArrayList is not empty");
            RecyclerView recyclerView = view.findViewById(R.id.mRecyclerView);
            CM_RecyclerViewAdapter adapter = new CM_RecyclerViewAdapter(getContext(), favCars);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        }

        return view;
    }
}
