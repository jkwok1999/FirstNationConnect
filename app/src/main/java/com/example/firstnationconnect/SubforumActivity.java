package com.example.firstnationconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubforumActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private TextView topicTitle;
    private FloatingActionButton newPostButton;
    private RecyclerView.Adapter mAdapter;
    private FirebaseFirestore firestoreDB;
    private ProgressBar pbSubforum;

    private List<ForumPost> postList;

    private String TAG = "SubforumActivity";
    private String topic;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PostSelectActivity.class);
        intent.putExtra("TopicName", topic);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subforum);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
        }

        recyclerView = findViewById(R.id.forumRecyclerView);
        topicTitle = findViewById(R.id.tvSubforumTitle);
        pbSubforum = findViewById(R.id.pbSubforum);
        newPostButton = findViewById(R.id.newPostFAB);
        newPostButton.setOnClickListener(this);

        topicTitle.setText(topic);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestoreDB = FirebaseFirestore.getInstance();

        postList = new ArrayList<>();

        CollectionReference subtopicsRef = firestoreDB.collection("Forum/" + topic + "/Subtopic");
        subtopicsRef.orderBy("postDate", Query.Direction.DESCENDING)
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

                        mAdapter = new SubtopicAdapter(SubforumActivity.this, postList);
                        recyclerView.setAdapter(mAdapter);

                        pbSubforum.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onBackPressed(){
        //NavUtils.navigateUpFromSameTask(this);
        Intent intent = new Intent(SubforumActivity.this, ForumActivity.class);
        intent.putExtra("TopicName", topic);
        startActivity(intent);
    }
}