package com.example.smtaani;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class profilePage extends AppCompatActivity {

    ImageButton back_arrow, updateProfileBtn, deleteProfileBtn;
    TextView userName, phoneNumber, userEmail;
    Button logoutBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        back_arrow = findViewById(R.id.back_arrow);
        updateProfileBtn = findViewById(R.id.updateProfileBtn);
        deleteProfileBtn = findViewById(R.id.deleteProfileBtn);
        userName = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
        userEmail = findViewById(R.id.user_email);
        logoutBtn = findViewById(R.id.logoutBtn);
        imageView = findViewById(R.id.imageView);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement delete profile functionality
                deleteProfileImage();
            }
        });

        // Logic For Logout Button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), loginRegister.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Get the current user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            // Fetch user details from FireStore "Users" collection
            fStore.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Retrieve user details
                                String name = document.getString("name");
                                String number = document.getString("phone");
                                String email = document.getString("email");
                                // Retrieve profile photo URL from FireStore
                                String profileImageUrl = document.getString("profilePicture");
                                // Set the user details in the respective TextViews
                                userName.setText(name);
                                phoneNumber.setText(number);
                                userEmail.setText(email);

                                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                                    // Load profile photo into imageView using Glide or any other image loading library
                                    Glide.with(this).load(profileImageUrl).into(imageView);
                                } else {
                                    // Handle the case when no profile photo is available
                                    //Toast.makeText(studentProfile.this, "No profile photo found", Toast.LENGTH_SHORT).show();
                                    Log.d("Profile", "No profile photo found");
                                }
                            } else {
                                Toast.makeText(profilePage.this, "User document not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(profilePage.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    private void deleteProfileImage() {
        // Get a reference to the Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to 'profile_pictures/<FILENAME>.jpg'
        final StorageReference profileRef = storageRef.child("profile_pictures/" + FirebaseAuth.getInstance().getUid() + ".jpg");
        // Delete the file from Firebase Storage
        profileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Remove the profile picture URL from FireStore
                fStore.collection("Users")
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .update("profilePicture", null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Set default image in ImageView
                                imageView.setImageResource(R.drawable.profile_image); // Replace with your default image resource
                                Toast.makeText(profilePage.this, "Profile photo deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                            // Handle unsuccessful uploads
                            Toast.makeText(profilePage.this, "Failed to remove profile picture URL", Toast.LENGTH_SHORT).show();

                        });

            }

        }).addOnFailureListener(e -> {
            // Handle unsuccessful uploads
            Toast.makeText(profilePage.this, "Failed to delete profile photo", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            // Get a reference to the Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            // Create a reference to 'profile_pictures/<FILENAME>.jpg'
            final StorageReference profileRef = storageRef.child("profile_pictures/" + FirebaseAuth.getInstance().getUid() + ".jpg");
            // Upload file to Firebase Storage
            assert uri != null;
            profileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Get the download URL of the uploaded file
                                    String imageUrl = uri.toString();
                                    // Update the user's profile picture in Firestore
                                    fStore.collection("Users")
                                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                            .update("profilePicture", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // Profile picture uploaded successfully
                                                    Log.d("selectProfile", "Profile picture uploaded successfully");
                                                }
                                            }).addOnFailureListener(e -> {
                                                // Handle unsuccessful uploads
                                                Toast.makeText(profilePage.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                                            });

                                }
                            });
                        }
                    });
        } else {
            // Handle the case when no image is selected or user canceled the operation
            //Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            Log.d("Profile", "No image selected");
        }

    }



}