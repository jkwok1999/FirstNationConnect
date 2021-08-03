package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputText, postName;
    private Button submitPost;
    private FirebaseAuth mAuth;

    private String topic;

    private FirebaseFirestore firestoreDB;

    private String TAG = "NewPostActivity";

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
        }

        inputText = findViewById(R.id.newPostContent);
        postName = findViewById(R.id.newPostName);
        submitPost = findViewById(R.id.submitPost);
        submitPost.setOnClickListener(this);

        firestoreDB = FirebaseFirestore.getInstance();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onClick(View view) {

        if (inputText.getText().toString().trim().isEmpty() || postName.getText().toString().trim().isEmpty()) {
            Toast.makeText(NewPostActivity.this, "Please ensure post is not empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            String name = postName.getText().toString();
            String content = inputText.getText().toString();

            long milliseconds = System.currentTimeMillis();
            Date postDate = new java.util.Date(milliseconds);

            UUID newID = UUID.randomUUID();
            String stringID = newID.toString();

            //Potentially put this in a new method, have a List variable outside which stores the user?
            DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    User user = documentSnapshot.toObject(User.class);
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String username = user.getUsername();

                    if (ActivityCompat.checkSelfPermission(NewPostActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        fusedLocationClient.getLastLocation().addOnSuccessListener(NewPostActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object

                                    GeoPoint postLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                                    ForumPost newPost = new ForumPost(stringID, topic, name, content, userID, postDate, username, null, postLocation, "Regular", null, null);

                                    firestoreDB.collection("Forum/" + topic + "/Subtopic").document(stringID).set(newPost);

                                }
                                else {
                                    ForumPost newPost = new ForumPost(stringID, topic, name, content, userID, postDate, username, null, null, "Regular", null, null);

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

                                Toast.makeText(NewPostActivity.this, "Post was successfully added",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewPostActivity.this, SubforumActivity.class);
                                intent.putExtra("TopicName", topic);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(NewPostActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},44);
                        Toast.makeText(NewPostActivity.this, "Please allow location access and try again",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //System.out.println("Button clicked");
                Intent intent = new Intent(NewPostActivity.this, SubforumActivity.class);
                intent.putExtra("TopicName", topic);
                startActivity(intent);
                break;
        }
        return true;
    }
}