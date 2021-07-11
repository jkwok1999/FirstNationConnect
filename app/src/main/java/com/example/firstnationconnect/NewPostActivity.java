package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputText, postName;
    private Button submitPost;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        inputText = findViewById(R.id.inputText);
        postName = findViewById(R.id.postName);
        submitPost = findViewById(R.id.submitPost);
        submitPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        database = FirebaseDatabase.getInstance();

        if (inputText.getText().toString().trim().isEmpty()) {
            Toast.makeText(NewPostActivity.this, "Please ensure post is not empty",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            String name = postName.getText().toString();
            String content = inputText.getText().toString();
            //Date postDate = FieldValue.serverTimestamp().

            //ForumPost newPost = new ForumPost(name, content, );

            Toast.makeText(NewPostActivity.this, "Post was successfully added",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}