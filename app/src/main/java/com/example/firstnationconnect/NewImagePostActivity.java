package com.example.firstnationconnect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;
import java.util.UUID;

public class NewImagePostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputText, postName;
    private ImageView ivNewPostImage;
    private Button submitPost;
    private Uri mainImageURI = null;

    private String stringID;
    private String topic;
    private String imageFileName;

    private FirebaseFirestore firestoreDB;
    private StorageReference storageReference;

    private String TAG = "NewPostActivity";

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image_post);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
        }

        inputText = findViewById(R.id.newPostContent);
        postName = findViewById(R.id.newPostName);
        ivNewPostImage = findViewById(R.id.ivNewPostImage);
        ivNewPostImage.setOnClickListener(this);
        submitPost = findViewById(R.id.submitPost);
        submitPost.setOnClickListener(this);

        firestoreDB = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        UUID newID = UUID.randomUUID();
        stringID = newID.toString();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case (R.id.ivNewPostImage):

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission
                            (NewImagePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(NewImagePostActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(NewImagePostActivity.this, new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 1);
                    } else {
                        /*CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(2, 1)
                                .start(NewImagePostActivity.this);*/
                        mGetContent.launch("image/*");
                    }
                } else {
                    /*CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(2, 1)
                            .start(NewImagePostActivity.this);*/
                    mGetContent.launch("image/*");
                }

                break;
            case (R.id.submitPost):

                if (inputText.getText().toString().trim().isEmpty() || postName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(NewImagePostActivity.this, "Please ensure post is not empty",
                            Toast.LENGTH_SHORT).show();
                } else if (mainImageURI == null) {
                    Toast.makeText(NewImagePostActivity.this, "Please ensure you have uploaded a picture",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String name = postName.getText().toString();
                    String content = inputText.getText().toString();

                    long milliseconds = System.currentTimeMillis();
                    Date postDate = new Date(milliseconds);

                    //Potentially put this in a new method, have a List variable outside which stores the user?
                    DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            User user = documentSnapshot.toObject(User.class);
                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String username = user.getUsername();
                            //String postImageString = mainImageURI.toString();

                            if (ActivityCompat.checkSelfPermission(NewImagePostActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(NewImagePostActivity.this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                // Got last known location. In some rare situations this can be null.
                                                if (location != null) {
                                                    // Logic to handle location object

                                                    GeoPoint postLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                                                    ForumPost newPost = new ForumPost(stringID, topic, name, content, userID, postDate, username, null, postLocation, "Image", imageFileName, null);

                                                    firestoreDB.collection("Forum/" + topic + "/Subtopic").document(stringID).set(newPost);

                                                } else {
                                                    ForumPost newPost = new ForumPost(stringID, topic, name, content, userID, postDate, username, null, null, "Image", imageFileName, null);

                                                    firestoreDB.collection("Forum/" + topic + "/Subtopic").document(stringID).set(newPost);

                                                }

                                                DocumentReference topicRef = firestoreDB.collection("Forum").document(topic);
                                                topicRef
                                                        .update("lastPost", stringID)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error updating document", e);
                                                            }
                                                        });

                                                Toast.makeText(NewImagePostActivity.this, "Post was successfully added",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(NewImagePostActivity.this, SubforumActivity.class);
                                                intent.putExtra("TopicName", topic);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            } else {
                                ActivityCompat.requestPermissions(NewImagePostActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
                                Toast.makeText(NewImagePostActivity.this, "Please allow location access and try again",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                break;
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    if (uri != null) {
                        System.out.println("Checkpoint");
                        mainImageURI = uri;
                        ivNewPostImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        ivNewPostImage.setImageURI(mainImageURI);

                        imageFileName = stringID + ".jpg";
                        StorageReference image_path = storageReference.child("forum_post_images").child(imageFileName);
                        image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {

                                Toast.makeText(NewImagePostActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();

                ivNewPostImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ivNewPostImage.setImageURI(mainImageURI);

                imageFileName = stringID + ".jpg";

                //Potentially move this to the submitPost onClick
                StorageReference image_path = storageReference.child("forum_post_images").child(imageFileName);
                image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(NewImagePostActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();

                        *//*image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                Toast.makeText(NewImagePostActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                            }
                        });*//*

                        Toast.makeText(NewImagePostActivity.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //System.out.println("Button clicked");
                Intent intent = new Intent(NewImagePostActivity.this, SubforumActivity.class);
                intent.putExtra("TopicName", topic);
                startActivity(intent);
                break;
        }
        return true;
    }
}