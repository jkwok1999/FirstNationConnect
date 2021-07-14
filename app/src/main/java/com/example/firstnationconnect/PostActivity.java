package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button replyButton;
    private TextView postTitle;
    private EditText replyText;

    private RecyclerView.Adapter mAdapter;
    //private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDB;

    private List<ForumPost> postList;

    private String TAG = "PostActivity";
    private String topic;
    private String mainPostName;
    private String mainPostID;

    @Override
    public void onClick(View v) {

        if (replyText.getText().toString().trim().isEmpty()) {
            Toast.makeText(PostActivity.this, "Please ensure reply is not empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            String replyContent = replyText.getText().toString();

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

                    ForumPost newPost = new ForumPost(stringID, topic, mainPostName, replyContent, username, postDate);

                    firestoreDB.collection("Forum/" + topic + "/Subtopic/" + mainPostID + "/Replies").document(stringID).set(newPost);

                    Toast.makeText(PostActivity.this, "Post was successfully added",
                            Toast.LENGTH_SHORT).show();

                    replyText.setText(null);

                    updateUi();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
            mainPostName = intent.getStringExtra("PostName");
            mainPostID = intent.getStringExtra("PostID");
        }

        recyclerView = findViewById(R.id.postRecyclerView);
        postTitle = findViewById(R.id.postTitle);
        replyText = findViewById(R.id.replyEditText);
        replyButton = findViewById(R.id.postReplyButton);
        replyButton.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestoreDB = FirebaseFirestore.getInstance();

        postList = new ArrayList<>();

        /*firestoreDB.collection("Forum/" + topic + "/Subtopic/" + mainPostID + "/Replies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(document.toObject(ForumPost.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        mAdapter = new PostAdapter(PostActivity.this, postList);
                        recyclerView.setAdapter(mAdapter);
                    }
                });*/

        updateUi();

    }

    public void updateUi() {

        if (!postList.isEmpty()) {
            postList.clear();
        }

        DocumentReference docRef = firestoreDB.collection("Forum/" + topic + "/Subtopic").document(mainPostID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ForumPost mainPost = documentSnapshot.toObject(ForumPost.class);
                postList.add(mainPost);
                postTitle.setText("'" + mainPost.getPostName() + "'");

                getReplies();

                //mAdapter = new PostAdapter(PostActivity.this, postList);
                //recyclerView.setAdapter(mAdapter);
            }
        });

        /*firestoreDB.collection("Forum/" + topic + "/Subtopic/" + mainPostID + "/Replies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(document.toObject(ForumPost.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        mAdapter = new PostAdapter(PostActivity.this, postList);
                        recyclerView.setAdapter(mAdapter);
                    }
                });*/
    }

    public void getReplies() {
        firestoreDB.collection("Forum/" + topic + "/Subtopic/" + mainPostID + "/Replies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                postList.add(document.toObject(ForumPost.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        mAdapter = new PostAdapter(PostActivity.this, postList);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    /*@Override
    public void onBackPressed(){
        //NavUtils.navigateUpFromSameTask(this);
        Intent intent = new Intent(PostActivity.this, SubforumActivity.class);
        intent.putExtra("TopicName", topic);
        startActivity(intent);
    }*/

    //Makes up button pass an intent
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                //System.out.println("Button clicked");
                Intent intent = new Intent(PostActivity.this, SubforumActivity.class);
                intent.putExtra("TopicName", topic);
                startActivity(intent);
                break;
        }
        return true;

    }
}