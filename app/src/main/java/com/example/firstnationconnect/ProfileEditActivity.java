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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText editFirstName, editLastName, editAge;
    private EditText editFNDTribe;
    private Button btEditChanges;
    private RadioGroup rgEditGender, rgFndEdit;
    private RadioButton radioButtonEditGender, radioButtonEditTribe, rbFndEditYes, rbFndEditNo, rbFndEditPNTS;
    private RadioButton rbEditMale, rbEditFemale, rbEditOther, rbEditPreferNotToSay;
    private CircleImageView profileImageEdit;
    private ProgressBar editProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDB;
    private StorageReference storageReference;

    private String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editAge = findViewById(R.id.editAge);
        btEditChanges = findViewById(R.id.btEditChanges);
        rgEditGender = findViewById(R.id.rgEditGender);
        rbEditMale = findViewById(R.id.rbEditMale);
        rbEditFemale = findViewById(R.id.rbEditFemale);
        rbEditOther = findViewById(R.id.rbEditOther);
        rbEditPreferNotToSay = findViewById(R.id.rbEditPreferNotToSay);
        editProgressBar = findViewById(R.id.editProgressBar);

        rgFndEdit = findViewById(R.id.rgFndEdit);
        rbFndEditYes = findViewById(R.id.rbFndEditYes);
        rbFndEditNo = findViewById(R.id.rbFndEditNo);
        rbFndEditPNTS = findViewById(R.id.rbFndEditPNTS);

        editFNDTribe = findViewById(R.id.editFNDTribe);


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

                editFirstName.setText(user.getFirstName());
                editLastName.setText(user.getLastName());
                editAge.setText(String.valueOf(user.getAge()));
                switch (user.getGender()) {
                    case "Male":
                        rbEditMale.setChecked(true);
                        break;
                    case "Female":
                        rbEditFemale.setChecked(true);
                        break;
                    case "Other":
                        rbEditOther.setChecked(true);
                        break;
                    case "Prefer not to say":
                        rbEditPreferNotToSay.setChecked(true);
                        break;
                }
                switch (user.getFirstNationDescent()) {
                    case "No":
                        rbFndEditNo.setChecked(true);
                        break;
                    case "Prefer not to say":
                        rbFndEditPNTS.setChecked(true);
                        break;
                    default:
                        rbFndEditYes.setChecked(true);
                        editFNDTribe.setText(user.getFirstNationDescent());
                        editFNDTribe.setVisibility(View.VISIBLE);
                }

                if (profileImageEdit != null) {
                    if (currentUser.getPhotoUrl() != null) {
                        StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                                .child("profile_images")
                                .child(user.getProfilePic());
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(profileImageEdit);
                            }
                        });
                    }
                }

                if (user.getProfilePic() != null) {
                    imageFileName = user.getProfilePic();
                }

            }
        });

        btEditChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String firstName = editFirstName.getText().toString().trim();
                    String lastName = editLastName.getText().toString().trim();
                    String etTribe = editFNDTribe.getText().toString().trim();
                    boolean editProfile = true;

                    if (firstName.isEmpty()) {
                        firstName = null;
                    }
                    if (lastName.isEmpty()) {
                        lastName = null;
                    }

                    if (editAge.getText().toString().trim().isEmpty()) {
                        editAge.setError("Age is required!");
                        editProfile = false;
                    }
                    if (rgFndEdit.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(ProfileEditActivity.this, "Please enter required fields",
                                Toast.LENGTH_SHORT).show();
                        editProfile = false;
                    }
                    if (etTribe.isEmpty() && editFNDTribe.getVisibility() == View.VISIBLE) {
                        editFNDTribe.setError("Please enter tribe name");
                        editProfile = false;
                    }

                    int age = Integer.parseInt(editAge.getEditableText().toString());
                    if (age < 16) {
                        editAge.setError("User must be 16 years or older!");
                        editProfile = false;
                    }
                    if (age > 110) {
                        editAge.setError("Please enter valid age!");
                        editProfile = false;
                    }
                    if(editProfile == true) {
                        editProgressBar.setVisibility(View.VISIBLE);
                        String email = mAuth.getCurrentUser().getEmail();
                        String username = mAuth.getCurrentUser().getDisplayName();
                        String profileImage = null;
                        String tribe = null;
                        /*if(mAuth.getCurrentUser().getPhotoUrl() != null) {
                            profileImage = mAuth.getCurrentUser().getPhotoUrl().toString();
                            System.out.println(mAuth.getCurrentUser().getPhotoUrl().toString());
                            System.out.println(currentUser.getPhotoUrl());
                            System.out.println(downloadUrl);
                        }*/

                        if (imageFileName != null) {
                            profileImage = imageFileName;
                        }

                        if (rbFndEditNo.isChecked() || rbFndEditPNTS.isChecked()) {
                            int radioIdTribe = rgFndEdit.getCheckedRadioButtonId();
                            radioButtonEditTribe = findViewById(radioIdTribe);
                            tribe = radioButtonEditTribe.getText().toString();
                        } else if (rbFndEditYes.isChecked()){
                            tribe = etTribe;
                        }

                        int radioIdGender = rgEditGender.getCheckedRadioButtonId();
                        radioButtonEditGender = findViewById(radioIdGender);
                        String gender = radioButtonEditGender.getText().toString();

                        User editUser = new User(firstName, lastName, email, username, age, gender, profileImage, tribe);
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
                } catch(NumberFormatException e) {
                    Toast.makeText(ProfileEditActivity.this, "Please enter required fields",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

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


    public void FndEditYes(View view) {
        if (rbFndEditNo.isChecked() || rbFndEditPNTS.isChecked()) {
            editFNDTribe.setVisibility(View.GONE);
        }
        if (rbFndEditYes.isChecked()) {
            editFNDTribe.setVisibility(View.VISIBLE);
        }

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

                imageFileName = user_id + ".jpg";
                StorageReference image_path = storageReference.child("profile_images").child(imageFileName);
                image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        /*image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();

                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileImageUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();
                                user.updateProfile(profileImageUpdates);

                                Toast.makeText(ProfileEditActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                            }
                        });*/

                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileImageUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(mainImageURI)
                                .build();
                        user.updateProfile(profileImageUpdates);

                        Toast.makeText(ProfileEditActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}