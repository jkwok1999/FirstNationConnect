package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SurveyResultActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDB;
    private TextView tvQuestionOneResponseText, tvQuestionTwoResponseText, tvQuestionThreeResponseText;
    private Button btCompleteSurveyAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        tvQuestionOneResponseText = findViewById(R.id.tvQuestionOneResponseText);
        tvQuestionTwoResponseText = findViewById(R.id.tvQuestionTwoResponseText);
        tvQuestionThreeResponseText = findViewById(R.id.tvQuestionThreeResponseText);

        btCompleteSurveyAgain = findViewById(R.id.btCompleteSurveyAgain);
        btCompleteSurveyAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent completeSurveyAgain = new Intent(SurveyResultActivity.this, SurveyActivity.class);
                startActivity(completeSurveyAgain);
            }
        });

        firestoreDB = FirebaseFirestore.getInstance();

        DocumentReference docRef = firestoreDB.collection("Survey").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Survey surveyResponse = documentSnapshot.toObject(Survey.class);

                tvQuestionOneResponseText.setText(surveyResponse.getSurveyResponseOne());
                tvQuestionTwoResponseText.setText(surveyResponse.getSurveyResponseOne());
                if(surveyResponse.getThoughts() != "") {
                    tvQuestionThreeResponseText.setText(surveyResponse.getThoughts());
                } else {
                    tvQuestionThreeResponseText.setText("Didn't provide a response");
                }
            }
        });
    }
}