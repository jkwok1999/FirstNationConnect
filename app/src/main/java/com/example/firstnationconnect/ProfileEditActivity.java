package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText editUsername, editFirstName, editLastName, editAge;
    private Button btEditChanges;
    private CircleImageView profileImageEdit;
    private ProgressBar editProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDB;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        editUsername = findViewById(R.id.editUsername);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editAge = findViewById(R.id.editAge);
        btEditChanges = findViewById(R.id.btEditChanges);
        editProgressBar = findViewById(R.id.editProgressBar);

        profileImageEdit = findViewById(R.id.profileImageEdit);

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

                editUsername.setText(user.getUsername());
                editFirstName.setText(user.getFirstName());
                editLastName.setText(user.getLastName());
                editAge.setText(String.valueOf(user.getAge()));
            }
        });

        btEditChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editFirstName.getText().toString().trim();
                String lastName = editLastName.getText().toString().trim();
                String username = editUsername.getText().toString().trim();

                if (firstName.isEmpty()) {
                    firstName = null;
                }
                if (lastName.isEmpty()) {
                    lastName = null;
                }

                if (editAge.getText().toString().trim().isEmpty()) {
                    editAge.setError("Age is required!");
                }
                if (username.isEmpty()) {
                    editUsername.setError("Username is required!");
                    return;
                }

                int age = Integer.parseInt(editAge.getEditableText().toString());
                if (age < 16) {
                    editAge.setError("User must be 16 years or older!");
                }
                if (age > 110) {
                    editAge.setError("Please enter valid age!");
                }
                editProgressBar.setVisibility(View.VISIBLE);
                String email = mAuth.getCurrentUser().getEmail();
                User editUser = new User(firstName, lastName, email, username, age);
                DocumentReference docRef = firestoreDB.collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                docRef.set(editUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates);
                            Toast.makeText(ProfileEditActivity.this, "Profile Settings Saved",
                                    Toast.LENGTH_LONG).show();
                            editProgressBar.setVisibility(View.INVISIBLE);
                            finish();
                            Intent editFinishProfileIntent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
                            startActivity(editFinishProfileIntent);
                    }
                }
            });
            }
        });

        if(profileImageEdit != null) {
            profileImageEdit.setImageURI(currentUser.getPhotoUrl());
        }

        profileImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission
                            (ProfileEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ProfileEditActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(ProfileEditActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 1);
                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(ProfileEditActivity.this);
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(ProfileEditActivity.this);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri mainImageURI;
                mainImageURI = result.getUri();
                profileImageEdit.setImageURI(mainImageURI);
                String user_id = mAuth.getCurrentUser().getUid();

                String imageFileName = user_id + ".jpg";
                StorageReference image_path = storageReference.child("profile_images").child(imageFileName);
                image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileImageUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(mainImageURI)
                                .build();
                        user.updateProfile(profileImageUpdates);
                        Toast.makeText(ProfileEditActivity.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}