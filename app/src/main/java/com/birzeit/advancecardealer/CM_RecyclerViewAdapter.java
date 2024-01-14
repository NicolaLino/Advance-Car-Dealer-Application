package com.birzeit.advancecardealer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class CM_RecyclerViewAdapter extends RecyclerView.Adapter<CM_RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<Car> carDetails;

    public CM_RecyclerViewAdapter(Context context, ArrayList<Car> carDetails){
        this.context = context;
        this.carDetails = carDetails;
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
        holder.carName.setText(carDetails.get(position).getModel());
        holder.factoryName.setText(carDetails.get(position).getFactoryName());
        holder.price.setText("$" + carDetails.get(position).getPrice());
        String imageUrl = "https://th.bing.com/th/id/R.3442a695e8b0034166c44738e620144e?rik=4PsNnbuNvEIElA&riu=http%3a%2f%2fstatic4.businessinsider.com%2fimage%2f5a4f952c3225de2b1a8b47cd-1227%2f2120x920mx-whitesunset.png&ehk=tK7vHbOXEaSxssvXQbCSNo4wd%2b86psdzI6KeZBdV85k%3d&risl=&pid=ImgRaw&r=0";
        Picasso.get().load(imageUrl).into(holder.listImage);
    }

    @Override
    public int getItemCount(){
        return carDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView listImage;
        Button favoriteButton;
        Button reservationButton;
        TextView carName;
        TextView factoryName;
        TextView price;
        public MyViewHolder(@NotNull View itemView){
            super(itemView);
            listImage = itemView.findViewById(R.id.listImage);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            reservationButton = itemView.findViewById(R.id.reservationButton);
            carName = itemView.findViewById(R.id.carNameTextView);
            factoryName = itemView.findViewById(R.id.factoryNameTextView);
            price = itemView.findViewById(R.id.priceTextView);
        }
    }
}
