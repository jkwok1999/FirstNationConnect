package com.example.firstnationconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.UUID;

public class SurveyActivity extends AppCompatActivity {

    private FirebaseFirestore firestoreDB;
    private Button btSubmitSurvey, btViewResults;
    private RadioGroup rgQuestionOne, rgQuestionTwo, rgQuestionThree;
    private TextView tvQuestionOne, tvQuestionTwo, tvQuestionThree2;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private TextInputEditText tietQuestionThree;
    private String thoughts = null;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        firestoreDB = FirebaseFirestore.getInstance();

        tvQuestionOne = findViewById(R.id.tvQuestionOne);
        tvQuestionTwo = findViewById(R.id.tvQuestionTwo);
        tvQuestionThree2 = findViewById(R.id.tvQuestionThree2);
        rgQuestionOne = findViewById(R.id.rgQuestionOne);
        rgQuestionTwo = findViewById(R.id.rgQuestionTwo);
        rgQuestionThree = findViewById(R.id.rgQuestionThree);

        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        tietQuestionThree = findViewById(R.id.tietQuestionThree);

        btViewResults = findViewById(R.id.btViewResults);
        btViewResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRef = firestoreDB.collection("Survey").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        mAuth = FirebaseAuth.getInstance();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Survey survey = documentSnapshot.toObject(Survey.class);
                                        if (user.getUid().equals(survey.getUserID())) {
                                            startActivity(new Intent(SurveyActivity.this, SurveyResultActivity.class));
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "Document does not exist!");
                                    Toast.makeText(SurveyActivity.this, "Please complete Survey",
                                            Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }

                });
//                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        mAuth = FirebaseAuth.getInstance();
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Survey survey = documentSnapshot.toObject(Survey.class);
//                        if (user.getUid().equals(survey.getUserID())) {
//                            startActivity(new Intent(SurveyActivity.this, SurveyResultActivity.class));
//                        } else if (survey.getUserID() == null){
//                            Toast.makeText(SurveyActivity.this, "Please complete Survey",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        });

        btSubmitSurvey = findViewById(R.id.btSubmitSurvey);
        btSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQuestionOne.setTextColor(getResources().getColor(R.color.black));
                tvQuestionTwo.setTextColor(getResources().getColor(R.color.black));
                tvQuestionThree2.setTextColor(getResources().getColor(R.color.black));
                boolean surveyValid = true;


                if (rgQuestionOne.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SurveyActivity.this, "Please enter all required survey fields",
                            Toast.LENGTH_SHORT).show();
                    tvQuestionOne.setTextColor(getResources().getColor(R.color.colorRed));
                    surveyValid = false;
                }
                if (rgQuestionTwo.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SurveyActivity.this, "Please enter all required survey fields",
                            Toast.LENGTH_SHORT).show();
                    tvQuestionTwo.setTextColor(getResources().getColor(R.color.colorRed));
                    surveyValid = false;
                }
                if(rgQuestionThree.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SurveyActivity.this, "Please enter all required survey fields",
                            Toast.LENGTH_SHORT).show();
                    tvQuestionThree2.setTextColor(getResources().getColor(R.color.colorRed));
                    surveyValid = false;
                }
                if (surveyValid) {

                    int radioId1 = rgQuestionOne.getCheckedRadioButtonId();
                    int radioId2 = rgQuestionTwo.getCheckedRadioButtonId();
                    int radioId3 = rgQuestionThree.getCheckedRadioButtonId();

                    radioButton1 = findViewById(radioId1);
                    radioButton2 = findViewById(radioId2);
                    radioButton3 = findViewById(radioId3);

                    thoughts = tietQuestionThree.getText().toString();

                    String question1Response = radioButton1.getText().toString();
                    String question2Response = radioButton2.getText().toString();
                    String question3Response = radioButton3.getText().toString();

                    long milliseconds = System.currentTimeMillis();
                    Date postDate = new java.util.Date(milliseconds);

                    DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            String username = user.getUsername();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            if (ActivityCompat.checkSelfPermission(SurveyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                fusedLocationClient.getLastLocation().addOnSuccessListener(SurveyActivity.this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            // Logic to handle location object

                                            GeoPoint postLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                                            Survey surveyResults = new Survey(username, userId, question1Response, question2Response, thoughts, postDate, postLocation, question3Response);
                                            //add more question attributes other than 1

                                            firestoreDB.collection("Survey").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(surveyResults);


                                        } else {

                                            Survey surveyResults = new Survey(username, userId, question1Response, question2Response, thoughts, postDate, null, question3Response);
                                            //add more question attributes other than 1

                                            firestoreDB.collection("Survey").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(surveyResults);

                                        }

                                        Toast.makeText(SurveyActivity.this, "Survey Complete",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(SurveyActivity.this, SurveyResultActivity.class));
                                    }
                                });
                            } else {
                            ActivityCompat.requestPermissions(SurveyActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},44);
                            Toast.makeText(SurveyActivity.this, "Please allow location access and try again",
                                    Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}