package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText loginEmail, loginPassword;
    private Button loginSignin;
    private TextView loginForgotPassword, loginRegister, invalidLogin;
    private ProgressBar loginProgressBar, pbLogin;

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginSignin = findViewById(R.id.loginSignin);
        loginSignin.setOnClickListener(this);
        loginRegister = findViewById(R.id.loginRegister);
        loginRegister.setOnClickListener(this);
        loginForgotPassword = findViewById(R.id.loginForgotPassword);
        loginForgotPassword.setOnClickListener(this);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        pbLogin = findViewById(R.id.pbLogin);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.loginSignin:
                //signIn(loginEmail.getText().toString(), loginPassword.getText().toString());
                signIn();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            pbLogin.setVisibility(View.VISIBLE);

            FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

            DocumentReference docRef = firestoreDB.collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user == null) {
                        Toast.makeText(MainActivity.this, "Please set up your account",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent (MainActivity.this, SetupActivity.class));
                    } else {
                        startActivity(new Intent (MainActivity.this, HomeActivity.class));
                        startActivity(new Intent (MainActivity.this, TosActivity.class));
                        finish();
                    }

                    pbLogin.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
    //Potential Issue: If the user closes the app and then relaunches, they can skip the TOS step.

    public void signIn () {

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        boolean loginValid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please provide a valid email address!");
            loginValid = false;
        }
        if (email.isEmpty()) {
            loginEmail.setError("Email is required!");
            loginValid = false;
        }
        if (password.isEmpty()) {
            loginPassword.setError("Password is required!");
            loginValid = false;
        }

        if (loginValid == true) {
            loginProgressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                pbLogin.setVisibility(View.VISIBLE);

                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Toast.makeText(MainActivity.this, "Login Successful",
                                        Toast.LENGTH_SHORT).show();

                                FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

                                DocumentReference docRef = firestoreDB.collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class);
                                        if (user == null) {
/*                                            Toast.makeText(MainActivity.this, "Please set up your account",
                                                    Toast.LENGTH_SHORT).show();*/
                                            startActivity(new Intent (MainActivity.this, SetupActivity.class));
                                        } else {
                                            startActivity(new Intent (MainActivity.this, HomeActivity.class));
                                            startActivity(new Intent (MainActivity.this, TosActivity.class));
                                            finish();
                                        }

                                        pbLogin.setVisibility(View.VISIBLE);
                                    }
                                });

                                /*startActivity(new Intent (MainActivity.this, HomeActivity.class));
                                startActivity(new Intent (MainActivity.this, TosActivity.class));
                                finish();*/

                                loginProgressBar.setVisibility(View.INVISIBLE);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Login Failed. Please Try Again",
                                        Toast.LENGTH_SHORT).show();
                                loginProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
        else {
            Toast.makeText(MainActivity.this, "Login Failed. Please Try Again",
                    Toast.LENGTH_SHORT).show();
            loginProgressBar.setVisibility(View.INVISIBLE);
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