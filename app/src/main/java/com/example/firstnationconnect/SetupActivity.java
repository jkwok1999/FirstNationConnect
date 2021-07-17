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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText registerFirstName, registerLastName, registerUsername, registerAge;
    private Button createSetup;
    private ProgressBar registerProgressBar2;
    //    private CheckBox registerCheckBox;
    private CircleImageView setupImage;
    private ImageView plusImage;
    private Uri mainImageURI = null;
    //TODO: is null currently ^ - but we need to set up a "default" image for users without pfp's

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        plusImage = findViewById(R.id.plusImage);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        registerUsername = findViewById(R.id.registerUsername);
        registerAge = findViewById(R.id.registerAge);
//        registerCheckBox = findViewById(R.id.registerCheckBox);
        registerProgressBar2 = findViewById(R.id.registerProgressBar2);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestoreDB = FirebaseFirestore.getInstance();

        createSetup = findViewById(R.id.createSetup);
        createSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = registerFirstName.getText().toString().trim();
                String lastName = registerLastName.getText().toString().trim();
                String username = registerUsername.getText().toString().trim();
                boolean loginValid = true;

                if (registerAge.getText().toString().trim().isEmpty()) {
                    registerAge.setError("Age is required!");
                    loginValid = false;
                }
                if (registerUsername.getText().toString().trim().isEmpty()) {
                    registerUsername.setError("Username is required!");
                    loginValid = false;
                }
                if (firstName.isEmpty()) {
                    registerFirstName.setError("First name is required!");
                    loginValid = false;
                }
                if (lastName.isEmpty()) {
                    registerLastName.setError("Last name is required!");
                    loginValid = false;
                    return;
                }
                int age = Integer.parseInt(registerAge.getEditableText().toString());
                if (age < 16) {
                    registerAge.setError("User must be 16 years or older!");
                    loginValid = false;
                }
                if (age > 110) {
                    registerAge.setError("Please enter valid age!");
                    loginValid = false;
                }
//                if (!registerCheckBox.isChecked()) {
//                    registerCheckBox.setError("Please agree to Terms and Conditions");
//                    loginValid = false;
//                } else {
//                    registerCheckBox.setError(null);
//                }
                if (loginValid == true) {
                    registerProgressBar2.setVisibility(View.VISIBLE);
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    String profilePic = mainImageURI.toString();
//                    StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");
//                    image_path.putFile(mainImageURI)
//                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                    if (task.isSuccessful()) {
                    User user = new User(firstName, lastName, email, username, profilePic, age);
                    // Sign in success, update UI with the signed-in user's information

                    firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username)
//                                               .setPhotoUri(mainImageURI)
                                                .build();
                                        user.updateProfile(profileUpdates);
                                        Toast.makeText(SetupActivity.this, "Account Created Successfully",
                                                Toast.LENGTH_LONG).show();
                                        registerProgressBar2.setVisibility(View.GONE);
                                        finish();
                                        firebaseAuth.signOut();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SetupActivity.this, "Account Creation Failed. Please Try Again",
                                                Toast.LENGTH_LONG).show();
                                        registerProgressBar2.setVisibility(View.GONE);
                                    }
                                }
                            });
//                                    }
//                            if (task.isSuccessful()) {
//                                Uri download_uri = task.getResult().getStorage().getDownloadUrl().getResult();
//                                Map<String, String> userMap = new HashMap<>();
//                                userMap.put("firstName", firstName);
//                                userMap.put("lastName", lastName);
//                                userMap.put("username", username);
//                                userMap.put("image", download_uri.toString());
//                                userMap.put("age", String.valueOf(age));
//
//                                firestoreDB.collection("User").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(SetupActivity.this, "User settings are updated.", Toast.LENGTH_LONG).show();
//                                            Intent mainIntent = new Intent(SetupActivity.this, HomeActivity.class);
//                                            startActivity(mainIntent);
//                                            finish();
//                                        } else {
//                                            String error = task.getException().getMessage();
//                                            Toast.makeText(SetupActivity.this, "Firestore Error: " + error, Toast.LENGTH_LONG).show();
//                                        }
//                                        registerProgressBar2.setVisibility(View.INVISIBLE);
//                                    }
//                                });
//
//                            } else {
//                                String error = task.getException().getMessage();
//                                Toast.makeText(SetupActivity.this, "Image Error: " + error, Toast.LENGTH_LONG).show();
//                                registerProgressBar2.setVisibility(View.INVISIBLE);
//                            }

//                                }
//                            });
                }
            }
        });

        setupImage = findViewById(R.id.setupImage);
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission
                            (SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 1);
                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(SetupActivity.this);
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(SetupActivity.this);
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
                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);
                String user_id = firebaseAuth.getCurrentUser().getUid();

                StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");
                image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        UserProfileChangeRequest profileImageUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(mainImageURI)
                                .build();
                        user.updateProfile(profileImageUpdates);
                        Toast.makeText(SetupActivity.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}