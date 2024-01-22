package com.birzeit.advancecardealer;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

class CM_RecyclerViewAdapter extends RecyclerView.Adapter<CM_RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<Car> carDetails;
    ArrayList<Car> copyCarDetails;
    ArrayList<Car> originalCarDetails;

    private AlertDialog alertDialog;
    CarDBHelper carDB;

    private ArrayList<Car> filteredList; // New variable for the filtered list


    public CM_RecyclerViewAdapter(Context context, ArrayList<Car> carDetails){
        this.context = context;
        this.carDetails = carDetails;
        this.copyCarDetails = new ArrayList<>(carDetails);
        this.originalCarDetails = new ArrayList<>(carDetails);
        this.carDB = new CarDBHelper(context);
    }



    @NotNull
    @Override
    public CM_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new CM_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull CM_RecyclerViewAdapter.MyViewHolder holder, int position){
        holder.carName.setText(copyCarDetails.get(position).getModel());
        holder.factoryName.setText(copyCarDetails.get(position).getFactoryName());
        holder.price.setText("$" + copyCarDetails.get(position).getPrice());
        String imageUrl = copyCarDetails.get(position).getUrl();
        Picasso.get().load(imageUrl).resize(0, 100).into(holder.listImage);

        holder.carName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showInformationDialog(copyCarDetails.get(adapterPosition));
                }
            }
        });

        if(carDB.isFavorite(LoginPage.emailStr, copyCarDetails.get(position).getId())) {
            holder.favoriteButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24));
        }


        // Implementing click listener for favoriteButton
        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Car car = copyCarDetails.get(adapterPosition);
                    String userEmail = LoginPage.emailStr;
                    int carId = car.getId();
                    if (!carDB.isFavorite(userEmail,carId)) {
                        if (carDB.addFavorite(userEmail, carId)) {
                            Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Was not added to favorites!!!!", Toast.LENGTH_SHORT).show();
                        }
                        holder.favoriteButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24));
                    }else{
                        if(carDB.removeFavorite(userEmail,carId)){
                            holder.favoriteButton.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_round_favorite_border_24));
                            Toast.makeText(context, "Removed from favorites!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Was not removed from favorites!!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });


        final boolean[] isPlay = {false};

        String userEmail = LoginPage.emailStr;
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        int carId = copyCarDetails.get(adapterPosition).getId();

        // Check if the car is already reserved by the current user
        if (carDB.isReserved(userEmail, carId)) {
            isPlay[0] = true;
            holder.reservationButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_bookmark_24));
        }
        holder.reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carDB.isReservedByOtherUser(userEmail, carId)) {
                    Toast.makeText(context, "This car is already reserved!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isPlay[0]) {
                    if (carDB.deleteReservation(userEmail, carId)) {
                        Toast.makeText(context, "Reservation deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete Reservation", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (showReservationDialog(copyCarDetails.get(adapterPosition))) {
                        Toast.makeText(context, "Reservation added!", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.reservationButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_bookmark_border_24));
                    }
                }

                isPlay[0] = !isPlay[0];

                int iconResourceId = isPlay[0] ? R.drawable.baseline_bookmark_24 : R.drawable.baseline_bookmark_border_24;
                holder.reservationButton.setIcon(ContextCompat.getDrawable(context, iconResourceId));
            }
        });

    }

    @Override
    public int getItemCount(){
        return copyCarDetails.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView listImage;
        MaterialButton favoriteButton;
        MaterialButton  reservationButton;
        TextView carName;
        TextView factoryName;
        TextView price;
        public MyViewHolder(@NotNull View itemView){
            super(itemView);
            listImage = itemView.findViewById(R.id.listImage3);
            favoriteButton = (MaterialButton) itemView.findViewById(R.id.favoriteButton);
            reservationButton = (MaterialButton) itemView.findViewById(R.id.reservationButton);
            carName = itemView.findViewById(R.id.carNameTextView);
            factoryName = itemView.findViewById(R.id.location1NameTextView);
            price = itemView.findViewById(R.id.priceTextView);
        }
    }

    private void showInformationDialog(Car car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.information_dialog, null);
        builder.setView(dialogView);

        ImageView informationImage = dialogView.findViewById(R.id.informationImage);
        TextView textCarNameInformation = dialogView.findViewById(R.id.textCarNameInformation);
        TextView textFactoryNameInformation = dialogView.findViewById(R.id.textFactoryNameInformation);
        TextView textPriceInformation = dialogView.findViewById(R.id.textPriceInformation);
        TextView textFuelTypeInformation = dialogView.findViewById(R.id.textFuelTypeInformation);
        TextView textTransmissionTypeInformation = dialogView.findViewById(R.id.textTransmissionTypeInformation);
        TextView textMileageInformation = dialogView.findViewById(R.id.textMileageInformation);
        String imageUrl = car.getUrl();
        Picasso.get().load(imageUrl).resize(0, 100).into(informationImage);
        textCarNameInformation.setText("Model: " + car.getModel());
        textFactoryNameInformation.setText("Car Company: "+ car.getFactoryName());
        textPriceInformation.setText("Price: $" + car.getPrice());
        textFuelTypeInformation.setText("Fuel Type: " + car.getFuelType());
        textTransmissionTypeInformation.setText("Transmission Type: " + car.getTransmissionType());
        textMileageInformation.setText("Mileage: " + car.getMileage());

        // Create and show the dialog
        alertDialog = builder.create();
        alertDialog.show();
    }


    private boolean showReservationDialog(Car car) {
        final boolean[] isPlay = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.popup_reservation, null);
        builder.setView(dialogView);

        ImageView reservationImage = dialogView.findViewById(R.id.reservationImage);
        TextView textCarNameReservation = dialogView.findViewById(R.id.textCarNameReservation);
        TextView textFactoryNameReservation = dialogView.findViewById(R.id.textFactoryNameReservation);
        TextView textPriceReservation = dialogView.findViewById(R.id.textPriceReservation);
        TextView textFuelTypeReservation = dialogView.findViewById(R.id.textFuelTypeReservation);
        TextView textTransmissionTypeReservation = dialogView.findViewById(R.id.textTransmissionTypeReservation);
        TextView textMileageReservation = dialogView.findViewById(R.id.textMileageReservation);
        TextView textReservationDate = dialogView.findViewById(R.id.textDateReservation);
        Button buttonReservation = dialogView.findViewById(R.id.submitButtonReservation);

        String imageUrl = car.getUrl();
        Picasso.get().load(imageUrl).resize(0, 100).into(reservationImage);

        textCarNameReservation.setText("Model: " + car.getModel());
        textFactoryNameReservation.setText("Car Company: "+ car.getFactoryName());
        textPriceReservation.setText("Price: $" + car.getPrice());
        textFuelTypeReservation.setText("Fuel Type: " + car.getFuelType());
        textTransmissionTypeReservation.setText("Transmission Type: " + car.getTransmissionType());
        textMileageReservation.setText("Mileage: " + car.getMileage());
        Date date = new Date();
        // without time
        // date with time
        String strDate = android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", date).toString();
        textReservationDate.setText("Reservation Date: " + strDate);

        buttonReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = LoginPage.emailStr;
                int carId = car.getId();
                if (carDB.addReservation(userEmail, carId, strDate)) {
                    // Show success message
                    isPlay[0] = true;
                    Toast.makeText(context, "Reservation added in date: !", Toast.LENGTH_SHORT).show();
                } else {
                    // Show error message
                    Toast.makeText(context, "Failed to add reservation", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });

        // Create and show the dialog
        alertDialog = builder.create();
        alertDialog.show();

        return isPlay[0];
    }

    public void filterByPrice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.price_filter_dialog, null);
        builder.setView(dialogView);

        // Add views and logic to set price range in the dialog
        EditText maxPriceEditText = dialogView.findViewById(R.id.maxPriceEditText);
        EditText minPriceEditText = dialogView.findViewById(R.id.minPriceEditText);

        MaterialButton applyFilterButton = dialogView.findViewById(R.id.applyPriceFilterButton);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Apply the price filter here
                // Call a method to update the adapter with the filtered data

                String minPrice = minPriceEditText.getText().toString();
                String maxPrice = maxPriceEditText.getText().toString();

                if (!minPrice.isEmpty() && !maxPrice.isEmpty()) {
                    // Call a method to filter the data based on the entered price range
                    applyPriceFilter(minPrice, maxPrice);
                } else {
                    // Handle the case where one or both of the price fields are empty
                    Toast.makeText(context, "Please enter both minimum and maximum prices", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void applyPriceFilter(String minPrice, String maxPrice) {
        // Convert minPrice and maxPrice to integers (or handle invalid input)
        int minPriceValue = Integer.parseInt(minPrice);
        int maxPriceValue = Integer.parseInt(maxPrice);

        // Check if minPrice is less than maxPrice
        if (minPriceValue <= maxPriceValue) {
            // Apply the filter logic based on the entered price range
            ArrayList<Car> filteredList = new ArrayList<>();

            for (Car car : copyCarDetails) {
                int carPrice = car.getPrice();

                if (carPrice >= minPriceValue && carPrice <= maxPriceValue) {
                    filteredList.add(car);
                }
            }

            // Update the adapter with the filtered data
            updateAdapter(filteredList);
        } else {
            // Show an error message or handle the case where minPrice is greater than maxPrice
            Toast.makeText(context, "Invalid price range", Toast.LENGTH_SHORT).show();
        }
    }

    public void showAllCars() {
        updateAdapter(originalCarDetails);
    }

    public void showMileageFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.mileage_filter_dialog, null);
        builder.setView(dialogView);

        EditText minMileageEditText = dialogView.findViewById(R.id.minMileageEditText);
        EditText maxMileageEditText = dialogView.findViewById(R.id.maxMileageEditText);

        MaterialButton applyFilterButton = dialogView.findViewById(R.id.applyMileageFilterButton);
        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Apply the mileage filter here
                // Call a method to update the adapter with the filtered data

                String minMileage = minMileageEditText.getText().toString();
                String maxMileage = maxMileageEditText.getText().toString();

                if (!minMileage.isEmpty() && !maxMileage.isEmpty()) {
                    // Call a method to filter the data based on the entered mileage range
                    applyMileageFilter(minMileage, maxMileage);
                } else {
                    // Handle the case where one or both of the mileage fields are empty
                    Toast.makeText(context, "Please enter both minimum and maximum mileage", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void applyMileageFilter(String minMileage, String maxMileage) {
        // Convert minMileage and maxMileage to integers (or handle invalid input)
        int minMileageValue = Integer.parseInt(minMileage);
        int maxMileageValue = Integer.parseInt(maxMileage);

        // Check if minMileage is less than maxMileage
        if (minMileageValue <= maxMileageValue) {
            // Apply the filter logic based on the entered mileage range
            ArrayList<Car> filteredList = new ArrayList<>();

            for (Car car : copyCarDetails) {
                int carMileage = extractMileage(car.getMileage());

                if (carMileage >= minMileageValue && carMileage <= maxMileageValue) {
                    filteredList.add(car);
                }
            }

            // Update the adapter with the filtered data
            updateAdapter(filteredList);
        } else {
            // Show an error message or handle the case where minMileage is greater than maxMileage
            Toast.makeText(context, "Invalid mileage range", Toast.LENGTH_SHORT).show();
        }
    }

    private int extractMileage(String mileageText) {
        // Extract the number from the mileage text (e.g., "30 mpg")
        String[] parts = mileageText.split(" ");
        if (parts.length > 0) {
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0; // Default mileage value
    }

    public void showTransmissionTypeFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.transmission_type_filter_dialog, null);
        builder.setView(dialogView);

        Spinner transmissionTypeSpinner = dialogView.findViewById(R.id.transmissionTypeSpinner);
        Button applyFilterButton = dialogView.findViewById(R.id.applyTransmissionTypeFilterButton);

        //array transmission types
        String[] transmissionTypes = {"Automatic", "Manual", "CVT"};

        // Set up the spinner with an adapter (you may need to create an ArrayAdapter with the transmission types)
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, transmissionTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transmissionTypeSpinner.setAdapter(adapter);

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Apply the transmission type filter here
                // Call a method to update the adapter with the filtered data

                String selectedTransmissionType = transmissionTypeSpinner.getSelectedItem().toString();

                if (!TextUtils.isEmpty(selectedTransmissionType)) {
                    // Call a method to filter the data based on the selected transmission type
                    applyTransmissionTypeFilter(selectedTransmissionType);
                } else {
                    // Handle the case where the transmission type is not selected
                    Toast.makeText(context, "Please select a transmission type", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void applyTransmissionTypeFilter(String selectedTransmissionType) {
        // Apply the filter logic based on the selected transmission type
        ArrayList<Car> filteredList = new ArrayList<>();

        for (Car car : copyCarDetails) {
            if (car.getTransmissionType().equalsIgnoreCase(selectedTransmissionType)) {
                filteredList.add(car);
            }
        }

        // Update the adapter with the filtered data
        updateAdapter(filteredList);
    }

    public void showFuelTypeFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.fuel_type_filter_dialog, null);
        builder.setView(dialogView);

        Spinner fuelTypeSpinner = dialogView.findViewById(R.id.fuelTypeSpinner);
        Button applyFilterButton = dialogView.findViewById(R.id.applyFuelTypeFilterButton);

        //array fuel types
        String[] fuelTypes = {"Hybrid", "Electric", "Diesel", "Gasoline"};

        // Set up the spinner with an adapter
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, fuelTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(adapter);

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Apply the fuel type filter here
                // Call a method to update the adapter with the filtered data

                String selectedFuelType = fuelTypeSpinner.getSelectedItem().toString();

                if (!TextUtils.isEmpty(selectedFuelType)) {
                    // Call a method to filter the data based on the selected fuel type
                    applyFuelTypeFilter(selectedFuelType);
                } else {
                    // Handle the case where the fuel type is not selected
                    Toast.makeText(context, "Please select a fuel type", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void applyFuelTypeFilter(String selectedFuelType) {
        // Apply the filter logic based on the selected fuel type
        ArrayList<Car> filteredList = new ArrayList<>();

        for (Car car : copyCarDetails) {
            if (car.getFuelType().equalsIgnoreCase(selectedFuelType)) {
                filteredList.add(car);
            }
        }

        // Update the adapter with the filtered data
        updateAdapter(filteredList);
    }




    private void updateAdapter(ArrayList<Car> filteredList) {
        copyCarDetails.clear();
        copyCarDetails.addAll(filteredList);
        notifyDataSetChanged();
    }

}
