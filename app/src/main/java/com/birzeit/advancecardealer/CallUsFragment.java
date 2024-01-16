package com.birzeit.advancecardealer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CallUsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_call_us, container, false);

        Button btnCallUs = view.findViewById(R.id.button_contact_phone);
        Button btnFindUs = view.findViewById(R.id.button_contact_maps);
        Button btnEmailUs = view.findViewById(R.id.button_contact_email);

        btnCallUs.setOnClickListener(v -> {
            // Open the dialer with the phone number
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:0599000000"));
            startActivity(dialIntent);
        });

        btnFindUs.setOnClickListener(v -> {
            // Open Google Maps to find the location
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Car+Dealer");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        btnEmailUs.setOnClickListener(v -> {
            Intent gmailIntent = new Intent();
            gmailIntent.setAction(Intent.ACTION_SENDTO);
            gmailIntent.setType("message/rfc822");
            gmailIntent.setData(Uri.parse("mailto:"));
            gmailIntent.putExtra(Intent.EXTRA_EMAIL, new String("salibamanolii@gmail.com"));
            startActivity(gmailIntent);
        });

        return view;
    }
}
