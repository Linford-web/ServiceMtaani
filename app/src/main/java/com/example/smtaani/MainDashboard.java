package com.example.smtaani;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smtaani.adapters.dashAdapter;
import com.example.smtaani.barberShopActivities.barberShop;
import com.example.smtaani.models.dashModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainDashboard extends AppCompatActivity {

    TextView get_name;
    CircleImageView userProfile;
    ImageButton allBarbersBtn, allCarWashBtn, allMovieBtn, allThriftBtn;
    RecyclerView dashRv, barberRv, carWashRv, moviesRv, clothesRv;
    ImageView messageIcon, bookedIcon, profileIcon, feedBackIcon;
    FirebaseAuth auth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dashboard);

        get_name = findViewById(R.id.get_name);
        allBarbersBtn = findViewById(R.id.allBarbersBtn);
        allCarWashBtn = findViewById(R.id.allCarWashBtn);
        allMovieBtn = findViewById(R.id.allMovieBtn);
        allThriftBtn = findViewById(R.id.allThriftBtn);
        dashRv = findViewById(R.id.dashRv);
        barberRv = findViewById(R.id.barberRv);
        carWashRv = findViewById(R.id.carWashRv);
        moviesRv = findViewById(R.id.moviesRv);
        clothesRv = findViewById(R.id.clothesRv);
        userProfile = findViewById(R.id.userProfileTv);
        messageIcon = findViewById(R.id.messageIcon);
        bookedIcon = findViewById(R.id.bookedIcon);
        profileIcon = findViewById(R.id.profileIcon);
        feedBackIcon = findViewById(R.id.feedBackIcon);
        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        // Get the current user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getUid();
        // logic to get the users image to be displayed on the Dashboard
        if (userId != null) {
            // Fetch user details from FireStore "Users" collection
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Retrieve profile photo URL from FireStore
                                String profileImageUrl = document.getString("profilePicture");
                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into otherImageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(userProfile);
                                } else {
                                    // Handle the case when no profile photo is available
                                    Log.d("Profile", "No profile photo found");
                                }
                            } else {
                                Toast.makeText(this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Logic for allBarbersBtn
        messageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainDashboard.class));
            }
        });
        bookedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainDashboard.class));
            }
        });
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), profilePage.class));
            }
        });
        feedBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainDashboard.class));
            }
        });

        fetchUserName();
        // set up a recyclerview with necessary onclick item to direct users to appropriate pages
        setupRecyclerView();

    }

    private void setupRecyclerView() {

        List<dashModel> itemList = new ArrayList<>();

        itemList.add(new dashModel(R.drawable.barbershop, "Barber Shop", barberShop.class));
        itemList.add(new dashModel(R.drawable.carwash, "Car Wash", carWash.class));
        itemList.add(new dashModel(R.drawable.movie, "Movie Shop", movieShop.class));
        itemList.add(new dashModel(R.drawable.boutique, "Thrift", thriftShop.class));
        itemList.add(new dashModel(R.drawable.restaurant, "Restaurant", restaurant.class));
        itemList.add(new dashModel(R.drawable.cyber_printer, "Documentation", cyberCafe.class));

        dashAdapter adapter = new dashAdapter(MainDashboard.this, itemList);
        dashRv.setLayoutManager(new GridLayoutManager(this, 2));
        dashRv.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        finishAffinity();
    }
    private void fetchUserName() {

        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String userName = document.getString("name");
                                    // Set the user name in the TextView
                                    get_name.setText(userName);
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    });
        }
    }

}