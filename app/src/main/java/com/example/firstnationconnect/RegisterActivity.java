package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newEmail, newPassword, registerFirstName, registerLastName, registerAge;
    private Button createAccount;
    private CheckBox registerCheckBox;
    private ProgressBar registerProgressBar;


    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newEmail = findViewById(R.id.newEmail);
        newPassword = findViewById(R.id.newPassword);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        registerAge = findViewById(R.id.registerAge);
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(this);

        registerCheckBox = findViewById(R.id.registerCheckBox);
        registerProgressBar = findViewById(R.id.registerProgressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        createAccount();

        //createAccount(newEmail.getText().toString(),newPassword.getText().toString());
    }

//    public void createAccount(String email, String password) {
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            Toast.makeText(RegisterActivity.this, "Account Created Successfully",
//                                    Toast.LENGTH_SHORT).show();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(RegisterActivity.this, "Account Creation Failed. Please Try Again",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

    public void createAccount() {
        try {
            String email = newEmail.getText().toString().trim();
            String password = newPassword.getText().toString().trim();
            String firstName = registerFirstName.getText().toString().trim();
            String lastName = registerLastName.getText().toString().trim();
            boolean loginValid = true;
            if (registerAge.getText().toString().trim().isEmpty()){
                registerAge.setError("Age is required!");
                loginValid = false;
            }
            if (firstName.isEmpty()) {
                registerFirstName.setError("First name is required!");
                loginValid = false;
            }
            if (lastName.isEmpty()) {
                registerLastName.setError("Last name is required!");
                loginValid = false;
            }
            if (email.isEmpty()) {
                newEmail.setError("Email is required!");
                loginValid = false;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                newEmail.setError("Please provide a valid email address!");
                loginValid = false;
            }
            if (password.isEmpty()) {
                newPassword.setError("Password is required!");
                loginValid = false;
            }
            if (password.length() < 6) {
                newPassword.setError("Password must be at least 6 characters!");
                loginValid = false;
                return;
            }
            int age = Integer.parseInt(registerAge.getEditableText().toString());
            if (age < 16){
                registerAge.setError("User must be 16 years or older!");
                loginValid = false;
            }
            if (age > 110){
                registerAge.setError("Please enter valid age!");
                loginValid = false;
            }
            if (!registerCheckBox.isChecked()) {
                registerCheckBox.setError("Please agree to Terms and Conditions");
                loginValid = false;
            } else {
                registerCheckBox.setError(null);
            }
            if (loginValid == true) {

                registerProgressBar.setVisibility(View.VISIBLE);
                registerCheckBox.setError(null);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User user = new User(firstName, lastName, email, age);
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Account Created Successfully",
                                                        Toast.LENGTH_LONG).show();
                                                registerProgressBar.setVisibility(View.GONE);
                                                finish();
                                                mAuth.signOut();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(RegisterActivity.this, "Account Creation Failed. Please Try Again",
                                                        Toast.LENGTH_LONG).show();
                                                registerProgressBar.setVisibility(View.GONE);
                                                updateUI(null);
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Account Creation Failed. Please Try Again",
                                            Toast.LENGTH_SHORT).show();
                                    registerProgressBar.setVisibility(View.GONE);
                                }
                            }
                            private void updateUI(FirebaseUser currentUser) {

                            }
                        });
            }
        } catch (NumberFormatException e) {
            Toast.makeText(RegisterActivity.this, "Please enter all fields!", Toast.LENGTH_LONG).show();
        }

    }
}