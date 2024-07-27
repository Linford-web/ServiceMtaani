package com.example.smtaani.barberShopActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smtaani.R;

public class barberShop extends AppCompatActivity {

    ImageButton back, searchBtn, search_icon, cancelSearchBtn;
    EditText searchInput;

    RecyclerView barberShopsRv, searchEventsRv;
    LinearLayout search_events_layout, search_layout, displayBarberShops, cancel_search_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_barber_shop);

        back = findViewById(R.id.back_arrow);
        searchBtn = findViewById(R.id.searchBtn);
        search_icon = findViewById(R.id.search_icon);
        searchInput = findViewById(R.id.searchInput);
        barberShopsRv = findViewById(R.id.barberShopsRv);
        searchEventsRv = findViewById(R.id.searchEventsRv);
        search_events_layout = findViewById(R.id.search_events_layout);
        search_layout = findViewById(R.id.search_layout);
        displayBarberShops = findViewById(R.id.displayBarberShops);
        cancel_search_layout = findViewById(R.id.cancel_search_layout);
        cancelSearchBtn = findViewById(R.id.cancelBtn);


        back.setOnClickListener(v -> {
            finish();
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_events_layout.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                cancel_search_layout.setVisibility(View.VISIBLE);
                displayBarberShops.setVisibility(View.GONE);
            }
        });
        cancelSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_events_layout.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
                cancel_search_layout.setVisibility(View.GONE);
                displayBarberShops.setVisibility(View.VISIBLE);
            }
        });




    }
}