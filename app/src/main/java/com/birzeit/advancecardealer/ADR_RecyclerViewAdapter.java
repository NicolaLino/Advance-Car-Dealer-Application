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

public class ADR_RecyclerViewAdapter extends RecyclerView.Adapter<ADR_RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<Car> carDetails;
    ArrayList<Car> copyCarDetails;
    private AlertDialog alertDialog;
    CarDBHelper carDB;

    LoginDBHelper loginDBHelper;


    public ADR_RecyclerViewAdapter(Context context, ArrayList<Car> carDetails){
        this.context = context;
        this.carDetails = carDetails;
        this.copyCarDetails = new ArrayList<>(carDetails);
        this.carDB = new CarDBHelper(context);
        this.loginDBHelper=new LoginDBHelper(context);
    }



    @NotNull
    @Override
    public ADR_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_admin, parent, false);
        return new ADR_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ADR_RecyclerViewAdapter.MyViewHolder holder, int position){
        holder.carName.setText(carDetails.get(position).getModel());
        holder.factoryName.setText(carDetails.get(position).getFactoryName());
        holder.price.setText("$" + carDetails.get(position).getPrice());
        holder.customerName.setText("Reserved by: "+carDB.getCustomerName(carDetails.get(position).getId(),loginDBHelper));
        holder.reservationDate.setText("Date: "+ carDB.getReservationDate(carDetails.get(position).getId()));
        String imageUrl = carDetails.get(position).getUrl();
        Picasso.get().load(imageUrl).resize(0, 100).into(holder.listImage);

        final boolean[] isPlay = {false};

        String userEmail = LoginPage.emailStr;
        int adapterPosition = holder.getAdapterPosition();
        if (adapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        int carId = carDetails.get(adapterPosition).getId();

    }


    @Override
    public int getItemCount(){
        return carDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView listImage;
        TextView carName;
        TextView factoryName;
        TextView price;
        TextView customerName;
        TextView reservationDate;
        public MyViewHolder(@NotNull View itemView){
            super(itemView);
            listImage = itemView.findViewById(R.id.listImage);
            carName = itemView.findViewById(R.id.carNameTextView);
            factoryName = itemView.findViewById(R.id.factoryNameTextView);
            price = itemView.findViewById(R.id.priceTextView);
            customerName=itemView.findViewById(R.id.view_customerName);
            reservationDate=itemView.findViewById(R.id.view_date);
        }
    }

}
