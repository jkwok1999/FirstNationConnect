package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputText, postName;
    private Button submitPost;
    private FirebaseAuth mAuth;

    private String topic;

    private FirebaseFirestore firestoreDB;

    private String TAG = "NewPostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
        }

        inputText = findViewById(R.id.newPostContent);
        postName = findViewById(R.id.newPostName);
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

        if (inputText.getText().toString().trim().isEmpty() || postName.getText().toString().trim().isEmpty()) {
            Toast.makeText(NewPostActivity.this, "Please ensure post is not empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            String name = postName.getText().toString();
            String content = inputText.getText().toString();

            long milliseconds = System.currentTimeMillis();
            Date postDate = new java.util.Date(milliseconds);

            UUID newID = UUID.randomUUID();
            String stringID = newID.toString();

            //Potentially put this in a new method, have a List variable outside which stores the user?
            DocumentReference docRef = firestoreDB.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    User user = documentSnapshot.toObject(User.class);
                    String username = user.getUsername();
                    String profileImage = user.getProfilePic();

                    ForumPost newPost = new ForumPost(stringID, topic, name, content, username, postDate, profileImage, null);

                    firestoreDB.collection("Forum/" + topic + "/Subtopic").document(stringID).set(newPost);

                    DocumentReference topicRef = firestoreDB.collection("Forum").document(topic);
                    topicRef
                            .update("lastPost", stringID)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });

                    Toast.makeText(NewPostActivity.this, "Post was successfully added",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewPostActivity.this, SubforumActivity.class);
                    intent.putExtra("TopicName", topic);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}