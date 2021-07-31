package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView profileFirstName, profileEmail, profileAge, profileUsername, profileDescent;
    private TextView tvFirstNameEdit, tvAgeEdit, tvEmailEdit, tvDescentEdit2;
    private Button btEditProfile;
    private ProgressBar progressBarEdit;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDB;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileFirstName = findViewById(R.id.profileFirstName);
        profileEmail = findViewById(R.id.profileEmail);
        profileAge = findViewById(R.id.profileAge);
        profileUsername = findViewById(R.id.profileUsername);
        profileImage = findViewById(R.id.profileImage);
        progressBarEdit = findViewById(R.id.progressBarEdit);
        profileDescent = findViewById(R.id.profileDescent);

        tvFirstNameEdit = findViewById(R.id.tvFirstNameEdit);
        tvAgeEdit = findViewById(R.id.tvAgeEdit);
        tvEmailEdit = findViewById(R.id.tvEmailEdit);
        tvDescentEdit2 = findViewById(R.id.tvDescentEdit2);

        btEditProfile = findViewById(R.id.btEditProfile);
        btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                startActivity(editProfileIntent);
            }
        });

        progressBarEdit.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.GONE);
        profileFirstName.setVisibility(View.GONE);
        profileEmail.setVisibility(View.GONE);
        profileAge.setVisibility(View.GONE);
        tvFirstNameEdit.setVisibility(View.GONE);
        tvAgeEdit.setVisibility(View.GONE);
        tvEmailEdit.setVisibility(View.GONE);
        btEditProfile.setVisibility(View.GONE);
        profileDescent.setVisibility(View.GONE);
        tvDescentEdit2.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = firestoreDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                progressBarEdit.setVisibility(View.GONE);
                profileImage.setVisibility(View.VISIBLE);
                profileFirstName.setVisibility(View.VISIBLE);
                profileEmail.setVisibility(View.VISIBLE);
                profileAge.setVisibility(View.VISIBLE);
                tvFirstNameEdit.setVisibility(View.VISIBLE);
                tvAgeEdit.setVisibility(View.VISIBLE);
                tvEmailEdit.setVisibility(View.VISIBLE);
                btEditProfile.setVisibility(View.VISIBLE);
                profileDescent.setVisibility(View.VISIBLE);
                tvDescentEdit2.setVisibility(View.VISIBLE);

                if (user.getFirstName() == null || user.getLastName() == null) {
                    profileFirstName.setText("");
                }

                profileFirstName.setText(user.getFirstName() + " " + user.getLastName());
                profileEmail.setText(user.getEmail());
                profileAge.setText(String.valueOf(user.getAge()));
                profileImage.setImageURI(currentUser.getPhotoUrl());
                profileUsername.setText(user.getUsername());
                profileDescent.setText(user.getFirstNationDescent());

            }
        });
    }
}