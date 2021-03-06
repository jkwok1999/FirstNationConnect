package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ReplySelectActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btBackToPost, btImageReply, btVideoReply;
    private String topic, reply;

    private String mainPostName;
    private String mainPostID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_select);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("TopicName");
            mainPostName = intent.getStringExtra("PostName");
            mainPostID = intent.getStringExtra("PostID");
            reply = intent.getStringExtra("Reply");
        }

        btBackToPost = findViewById(R.id.btBackToPost);
        btBackToPost.setOnClickListener(this);
        btImageReply = findViewById(R.id.btImageReply);
        btImageReply.setOnClickListener(this);
        btVideoReply = findViewById(R.id.btVideoReply);
        btVideoReply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBackToPost:
                finish();
                break;
            case R.id.btImageReply:
                Intent imageIntent = new Intent(ReplySelectActivity.this, NewImageReplyActivity.class);
                imageIntent.putExtra("TopicName",topic);
                imageIntent.putExtra("PostName",mainPostName);
                imageIntent.putExtra("PostID",mainPostID);

                if (reply != null) {
                    imageIntent.putExtra("Reply",reply);
                }
                startActivity(imageIntent);
                break;
            case R.id.btVideoReply:
                /*Intent videoIntent = new Intent(ReplySelectActivity.this, NewVideoReplyActivity.class);
                videoIntent.putExtra("TopicName",topic);
                videoIntent.putExtra("PostName",mainPostName);
                videoIntent.putExtra("PostID",mainPostID);

                if (reply != null) {
                    videoIntent.putExtra("Reply",reply);
                }
                startActivity(videoIntent);*/
                Toast.makeText(ReplySelectActivity.this, "Functionality will be added in the future",
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