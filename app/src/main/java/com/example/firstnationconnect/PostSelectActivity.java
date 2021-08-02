package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PostSelectActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btRegularPost, btImagePost, btVideoPost;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_select);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
        }

        btRegularPost = findViewById(R.id.btRegularPost);
        btRegularPost.setOnClickListener(this);
        btImagePost = findViewById(R.id.btImagePost);
        btImagePost.setOnClickListener(this);
        btVideoPost = findViewById(R.id.btVideoPost);
        btVideoPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()) {
            case R.id.btRegularPost:
                Intent regularIntent = new Intent(PostSelectActivity.this, NewPostActivity.class);
                regularIntent.putExtra("TopicName", topic);
                startActivity(regularIntent);
                break;
            case R.id.btImagePost:
                Intent imageIntent = new Intent(PostSelectActivity.this, NewImagePostActivity.class);
                imageIntent.putExtra("TopicName", topic);
                startActivity(imageIntent);
                break;
            case R.id.btVideoPost:
                /*Intent videoIntent = new Intent(PostSelectActivity.this, NewVideoPostActivity.class);
                videoIntent.putExtra("TopicName", topic);
                startActivity(videoIntent);*/
                Toast.makeText(PostSelectActivity.this, "Functionality will be added in the future",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}