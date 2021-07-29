package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ResourceTopicActivity extends AppCompatActivity {

    String topic;
    TextView topicName;
    RecyclerView rvResourceList;

    private List<Resource> resourceList;

    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_topic);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            topic = intent.getStringExtra("Topic");
        }

        rvResourceList = findViewById(R.id.rvResourceList);
        topicName = findViewById(R.id.resourceTopicTV);
        topicName.setText(topic);

        rvResourceList.setHasFixedSize(true);
        rvResourceList.setLayoutManager(new LinearLayoutManager(this));

        switch (topic) {
            case "Suicide Prevention":
                resourceList = Resource.getSuicidePreventionResources();
                break;
            case "Depression":
                resourceList = Resource.getDepressionResources();
                break;
            case "Anxiety":
                resourceList = Resource.getAnxietyResources();
                break;
            case "Belonging":
                resourceList = Resource.getBelongingResources();
                break;
            case "Perinatal Health":
                resourceList = Resource.getPerinatalHealthResources();
                break;
            case "Bullying":
                resourceList = Resource.getBullyingResources();
                break;
        }

        mAdapter = new ResourceTopicAdapter(ResourceTopicActivity.this, resourceList);
        rvResourceList.setAdapter(mAdapter);

    }
}