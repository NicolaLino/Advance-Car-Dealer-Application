package com.birzeit.advancecardealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button connectButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = findViewById(R.id.button_connect);

        connectButton.setOnClickListener(view -> new ConnectTask().execute());

    }

    public class ConnectTask extends AsyncTask<Void, Void, ArrayList<Car>> {

        @Override
        protected ArrayList<Car> doInBackground(Void... voids) {
            String apiUrl = "https://mpfa9da8be12d248968a.free.beeceptor.com/car-data";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);


                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return parseJsonResponse(response.toString());
                }
                else{
                    return new ArrayList<>();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }


        @Override
        protected void onPostExecute(ArrayList<Car> carTypes) {
            if (!carTypes.isEmpty()) {
                for (Car carType : carTypes) {
                    String carTypeString = carType.getFactoryName() + " " + carType.getModel() + " " + carType.getPrice() + " " + carType.getFuelType() + " " + carType.getTransmissionType() + " " + carType.getMileage();
                    Log.d("CarTypeLog", carTypeString); // Log the car type for debugging purposes
                }
                proceedToLoginAndRegistration();
            } else {
                displayErrorMessage();
            }
        }
    }

    private void proceedToLoginAndRegistration() {
        Intent loginIntent = new Intent(MainActivity.this, LoginPage.class);
        startActivity(loginIntent);
        Toast.makeText(MainActivity.this, "Connection successful!", Toast.LENGTH_SHORT).show();
    }
    private void displayErrorMessage() {
        Toast.makeText(MainActivity.this, "Connection unsuccessful. Please try again.", Toast.LENGTH_SHORT).show();
    }
    private ArrayList<Car> parseJsonResponse(String jsonResponse) {
        ArrayList<Car> carDetails;

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            carDetails = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Car car = new Car();
                int id = jsonObject.getInt("id");
                car.setId(id);
                String factoryName = jsonObject.getString("factoryName");
                car.setFactoryName(factoryName);
                String model = jsonObject.getString("model");
                car.setModel(model);
                int price = jsonObject.getInt("price");
                car.setPrice(price);
                String fuelType = jsonObject.getString("fuelType");
                car.setFuelType(fuelType);
                String transmissionType = jsonObject.getString("transmissionType");
                car.setTransmissionType(transmissionType);
                String mileage = jsonObject.getString("mileage");
                car.setMileage(mileage);

                carDetails.add(car);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return carDetails;
    }

}