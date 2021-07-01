package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newEmail, newPassword;
    private Button createAccount;

    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newEmail = findViewById(R.id.newEmail);
        newPassword = findViewById(R.id.newPassword);
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        createAccount();

        //createAccount(newEmail.getText().toString(),newPassword.getText().toString());
    }

    public void createAccount (String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "Account Created Successfully",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Account Creation Failed. Please Try Again",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void createAccount () {

        String email = newEmail.getText().toString();
        String password = newPassword.getText().toString();

        boolean loginValid = true;


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            newEmail.setError("Please provide a valid email address!");
            loginValid = false;
        }
        if (email.isEmpty()) {
            newEmail.setError("Email is required!");
            loginValid = false;
        }
        if (password.length() < 6) {
            newPassword.setError("Password must be at least 6 characters!");
            loginValid = false;
        }
        if (password.isEmpty()) {
            newPassword.setError("Password is required!");
            loginValid = false;
        }


        if (loginValid == true) {

            //loginProgressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(RegisterActivity.this, "Account Created Successfully",
                                        Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                //FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Account Creation Failed. Please Try Again",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        else {
            Toast.makeText(RegisterActivity.this, "Account Creation Failed. Please Try Again",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser currentUser) {

    }
}