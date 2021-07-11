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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newEmail, newPassword, registerFirstName, registerLastName, registerUsername, registerAge;
    private Button createAccount;
    private CheckBox registerCheckBox;
    private ProgressBar registerProgressBar;


    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newEmail = findViewById(R.id.newEmail);
        newPassword = findViewById(R.id.newPassword);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        registerUsername = findViewById(R.id.registerUsername);
        registerAge = findViewById(R.id.registerAge);
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(this);

        registerCheckBox = findViewById(R.id.registerCheckBox);
        registerProgressBar = findViewById(R.id.registerProgressBar);

        mAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {

        createAccount();

    }

    public void createAccount() {
        try {
            FirebaseUser user = mAuth.getCurrentUser();
            String email = newEmail.getText().toString().trim();
            String password = newPassword.getText().toString().trim();
            String firstName = registerFirstName.getText().toString().trim();
            String lastName = registerLastName.getText().toString().trim();
            String username = registerUsername.getText().toString().trim();
            boolean loginValid = true;
            if (registerAge.getText().toString().trim().isEmpty()){
                registerAge.setError("Age is required!");
                loginValid = false;
            }
            if (registerUsername.getText().toString().trim().isEmpty()){
                registerUsername.setError("Username is required!");
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
                                    User user = new User(firstName, lastName, email, username, age);
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");



                                    /*FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {*/
                                    firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(username).build();
                                                user.updateProfile(profileUpdates);
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