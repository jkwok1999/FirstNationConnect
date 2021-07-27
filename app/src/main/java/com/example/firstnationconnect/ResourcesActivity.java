package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.common.io.Resources;

public class ResourcesActivity extends AppCompatActivity implements View.OnClickListener {

    CardView suicideResource, depressionResource, anxietyResource, belongingResource, perinatalResource, bullyingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        suicideResource = findViewById(R.id.cvSuicidePrev);
        depressionResource = findViewById(R.id.cvDepression);
        anxietyResource = findViewById(R.id.cvAnxiety);
        belongingResource = findViewById(R.id.cvBelonging);
        perinatalResource = findViewById(R.id.cvPerinatal);
        bullyingResource = findViewById(R.id.cvBully);

        suicideResource.setOnClickListener(this);
        depressionResource.setOnClickListener(this);
        anxietyResource.setOnClickListener(this);
        belongingResource.setOnClickListener(this);
        perinatalResource.setOnClickListener(this);
        bullyingResource.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String topic = "";
        Intent intent = new Intent(ResourcesActivity.this, ResourceTopicActivity.class);

        //using switch to better organise onClick buttons
        switch (v.getId()) {
            case R.id.cvSuicidePrev:
                topic = "Suicide Prevention";
                break;
            case R.id.cvDepression:
                topic = "Depression";
                break;
            case R.id.cvAnxiety:
                topic = "Anxiety";
                break;
            case R.id.cvBelonging:
                topic = "Belonging";
                break;
            case R.id.cvPerinatal:
                topic = "Perinatal Health";
                break;
            case R.id.cvBully:
                topic = "Bullying";
                break;
        }

        intent.putExtra("Topic", topic);
        startActivity(intent);
    }
}