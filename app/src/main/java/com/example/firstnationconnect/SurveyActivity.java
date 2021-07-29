package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class SurveyActivity extends AppCompatActivity {

    private FirebaseFirestore firestoreDB;
    private Button btSubmitSurvey;
    private RadioGroup rgQuestionOne, rgQuestionTwo;
    //    private RadioButton rbQuestion1StronglyAgree, rbQuestion1Agree, rbQuestion1Neutral,
//            rbQuestion1Disagree, rbQuestion1StronglyDisagree;
//    private RadioButton rbQuestion2StronglyAgree, rbQuestion2Agree, rbQuestion2Neutral,
//            rbQuestion2Disagree, rbQuestion2StronglyDisagree;
    private RadioButton radioButton1;
    private TextInputEditText tietQuestionThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        firestoreDB = FirebaseFirestore.getInstance();

        rgQuestionOne = findViewById(R.id.rgQuestionOne);
        rgQuestionTwo = findViewById(R.id.rgQuestionTwo);

//        rbQuestion1StronglyAgree = findViewById(R.id.rbQuestion1StronglyAgree);
//        rbQuestion1Agree = findViewById(R.id.rbQuestion1Agree);
//        rbQuestion1Neutral = findViewById(R.id.rbQuestion1Neutral);
//        rbQuestion1Disagree = findViewById(R.id.rbQuestion1Disagree);
//        rbQuestion1StronglyDisagree = findViewById(R.id.rbQuestion1StronglyDisagree);
//
//        rbQuestion2StronglyAgree = findViewById(R.id.rbQuestion2StronglyAgree);
//        rbQuestion2Agree = findViewById(R.id.rbQuestion2Agree);
//        rbQuestion2Neutral = findViewById(R.id.rbQuestion2Neutral);
//        rbQuestion2Disagree = findViewById(R.id.rbQuestion2Disagree);
//        rbQuestion2StronglyDisagree = findViewById(R.id.rbQuestion2StronglyDisagree);


        tietQuestionThree = findViewById(R.id.tietQuestionThree);

        btSubmitSurvey = findViewById(R.id.btSubmitSurvey);
        btSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioId1 = rgQuestionOne.getCheckedRadioButtonId();

                radioButton1 = findViewById(radioId1);

                String question1Response = radioButton1.getText().toString();

                DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        String username = user.getUsername();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Survey surveyResults = new Survey(username, userId, question1Response);
                        //add more question attributes other than 1

                        firestoreDB.collection("Survey").document("Survey - " + FirebaseAuth.getInstance().getCurrentUser().getUid()).set(surveyResults);
                        Toast.makeText(SurveyActivity.this, "Survey Complete",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


            }
        });
    }

}