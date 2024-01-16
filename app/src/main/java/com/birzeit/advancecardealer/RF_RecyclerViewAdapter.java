package com.birzeit.advancecardealer;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class RF_RecyclerViewAdapter extends RecyclerView.Adapter<RF_RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Car> carDetails;
    private CarDBHelper carDB;
    private AlertDialog alertDialog;

    public RF_RecyclerViewAdapter(Context context, ArrayList<Car> carDetails) {
        this.context = context;
        this.carDetails = carDetails;
        this.carDB = new CarDBHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Car car = carDetails.get(position);

        String userEmail = LoginPage.emailStr;
        // Check if the car is reserved in the database
        if (carDB.isReserved(userEmail, car.getId())) {
            holder.carName.setText(car.getModel());
            holder.factoryName.setText(car.getFactoryName());
            holder.price.setText("$" + car.getPrice());
            String imageUrl = car.getUrl();
            Picasso.get().load(imageUrl).resize(0, 100).into(holder.listImage);

            holder.carName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInformationDialog(car);
                }
            });

            // Implementing click listener for favoriteButton
            holder.favoriteButton.setVisibility(View.GONE);

            final boolean[] isPlay = {false};
            int carId = car.getId();

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
                        if (showReservationDialog(car)) {
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
        } else {

        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < carDetails.size(); i++) {
            Car car = carDetails.get(i);
            String userEmail = LoginPage.emailStr;
            // Check if the car is reserved in the database
            if (carDB.isReserved(userEmail, car.getId())) {
                count++;
            }
        }
        return count;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView listImage;
        Button favoriteButton;
        MaterialButton reservationButton;
        TextView carName;
        TextView factoryName;
        TextView price;
        public MyViewHolder(@NotNull View itemView){
            super(itemView);
            listImage = itemView.findViewById(R.id.listImage);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            reservationButton = (MaterialButton) itemView.findViewById(R.id.reservationButton);
            carName = itemView.findViewById(R.id.carNameTextView);
            factoryName = itemView.findViewById(R.id.factoryNameTextView);
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
        String strDate = android.text.format.DateFormat.format("dd-MM-yyyy", date).toString();
        textReservationDate.setText("Reservation Date: " + strDate);

        buttonReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = LoginPage.emailStr;
                int carId = car.getId();
                if (carDB.addReservation(userEmail, carId, strDate)) {
                    // Show success message
                    isPlay[0] = true;
                    Toast.makeText(context, "Reservation added!", Toast.LENGTH_SHORT).show();
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
}
