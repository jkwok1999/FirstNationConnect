package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
    private Button btSubmitSurvey, btViewResults;
    private RadioGroup rgQuestionOne, rgQuestionTwo;
    private TextView tvQuestionOne, tvQuestionTwo;
    private RadioButton radioButton1, radioButton2;
    private TextInputEditText tietQuestionThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        firestoreDB = FirebaseFirestore.getInstance();

        tvQuestionOne = findViewById(R.id.tvQuestionOne);
        tvQuestionTwo = findViewById(R.id.tvQuestionTwo);
        rgQuestionOne = findViewById(R.id.rgQuestionOne);
        rgQuestionTwo = findViewById(R.id.rgQuestionTwo);

        tietQuestionThree = findViewById(R.id.tietQuestionThree);

        btViewResults = findViewById(R.id.btViewResults);

        btSubmitSurvey = findViewById(R.id.btSubmitSurvey);
        btSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQuestionOne.setTextColor(getResources().getColor(R.color.black));
                tvQuestionTwo.setTextColor(getResources().getColor(R.color.black));
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
                if (surveyValid) {

                    int radioId1 = rgQuestionOne.getCheckedRadioButtonId();
                    int radioId2 = rgQuestionTwo.getCheckedRadioButtonId();

                    radioButton1 = findViewById(radioId1);
                    radioButton2 = findViewById(radioId2);

                    String question1Response = radioButton1.getText().toString();
                    String question2Response = radioButton2.getText().toString();

                    DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            String username = user.getUsername();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Survey surveyResults = new Survey(username, userId, question1Response, question2Response);
                            //add more question attributes other than 1

                            firestoreDB.collection("Survey").document("Survey " + FirebaseAuth.getInstance().getCurrentUser().getUid()).set(surveyResults);
                            Toast.makeText(SurveyActivity.this, "Survey Complete",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
}