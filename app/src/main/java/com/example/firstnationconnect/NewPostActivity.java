package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputText, postName;
    private Button submitPost;
    private FirebaseAuth mAuth;

    private FirebaseFirestore firestoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        inputText = findViewById(R.id.inputText);
        postName = findViewById(R.id.postName);
        submitPost = findViewById(R.id.submitPost);
        submitPost.setOnClickListener(this);

        firestoreDB = FirebaseFirestore.getInstance();
    }

/*    @Override
    public void onClick(View view) {

        if (inputText.getText().toString().trim().isEmpty()) {
            Toast.makeText(NewPostActivity.this, "Please ensure post is not empty",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            String name = postName.getText().toString();
            String content = inputText.getText().toString();
            Date postDate = null;

            User currentUser = getCurrentUser();
            String username = currentUser.getUsername();

            UUID newID = UUID.randomUUID();
            String stringID = newID.toString();

            ForumPost newPost = new ForumPost(stringID, name, content, username, postDate);

            firestoreDB.collection("Forum").document(stringID).set(newPost);

            Toast.makeText(NewPostActivity.this, "Post was successfully added",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public User getCurrentUser() {

        final User[] user = new User[1];

        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());

        DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        System.out.println(docRef);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                System.out.println("Hi");
                user[0] = documentSnapshot.toObject(User.class);
                System.out.println(user[0]);

            }
        });

        //Code has issues: Need some way to return User AFTER it has been retrieved.
        System.out.println("Something");
        System.out.println(user[0].getEmail());

        return user[0];
    }*/

    @Override
    public void onClick(View view) {

        if (inputText.getText().toString().trim().isEmpty()) {
            Toast.makeText(NewPostActivity.this, "Please ensure post is not empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            String name = postName.getText().toString();
            String content = inputText.getText().toString();
            Date postDate = null;

            UUID newID = UUID.randomUUID();
            String stringID = newID.toString();

            DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    User user = documentSnapshot.toObject(User.class);
                    String username = user.getUsername();

                    ForumPost newPost = new ForumPost(stringID, name, content, username, postDate);

                    firestoreDB.collection("Forum").document(stringID).set(newPost);

                    Toast.makeText(NewPostActivity.this, "Post was successfully added",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewPostActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}