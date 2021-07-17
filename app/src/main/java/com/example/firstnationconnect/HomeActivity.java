package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cvSignOut, cvDiscussionForum;
    TextView welcomeName;

    private static final String TAG = "HomeActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cvSignOut = findViewById(R.id.cvSignOut);
        cvSignOut.setOnClickListener(this);
        cvDiscussionForum = findViewById(R.id.cvDiscussionForum);
        cvDiscussionForum.setOnClickListener(this);
        welcomeName = findViewById(R.id.welcomeName);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        welcomeName.setText("Welcome Back, " + user.getDisplayName() + "!");
//        loggedInEmail.setText(user.getEmail());
//        loggedInUsername.setText(user.getDisplayName());

    }

    @Override
    public void onClick(View v) {
        //using switch to better organise onClick buttons
        switch (v.getId()) {
            case R.id.cvSignOut:
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                //finish() function will ensure user is fully logged out and will not encounter
                //bugs that make them remain logged in
                finish();
                break;
            case R.id.cvDiscussionForum:
                startActivity(new Intent(HomeActivity.this, ForumActivity.class));
                break;
        }
    }

    public void getCurrentUser () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }
}