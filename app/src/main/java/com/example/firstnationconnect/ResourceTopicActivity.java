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
    TextView topicName, mentaldesc;
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

        mentaldesc = findViewById(R.id.mentaldesc);
        rvResourceList = findViewById(R.id.rvResourceList);
        topicName = findViewById(R.id.resourceTopicTV);
        topicName.setText(topic);

        rvResourceList.setHasFixedSize(true);
        rvResourceList.setLayoutManager(new LinearLayoutManager(this));

        switch (topic) {
            case "Suicide Prevention":
                resourceList = Resource.getSuicidePreventionResources();
                mentaldesc.setText("Suicidal ideation (or suicidal thoughts) means having the ideas, thoughts or ruminations about" +
                        " the possibility of ending one’s life. This is not a diagnosis but is a symptom of some mental disorders" +
                        " and which can occur in response to adverse events such as bullying, anxiety," +
                        " feelings of sadness or distraught, mental health issues, and many more risk factors." +
                        " Help and appropriate support is offered at all times for those in need. Please seek more information" +
                        " and support below.");
                break;
            case "Depression":
                resourceList = Resource.getDepressionResources();
                mentaldesc.setText("Depression is classified as a mood disorder, described as feelings of sadness, unhappiness, loss," +
                        " or anger which can interfere with a person’s everyday activities, possibly resulting in lost time and" +
                        " lower productivity. Depression can also be associated with the thoughts of suicide. If you feel depressed," +
                        " see your doctor. Don’t delay. Seeking support early can help stop symptoms becoming worse. See for" +
                        " more information and support below.");
                break;
            case "Anxiety":
                resourceList = Resource.getAnxietyResources();
                mentaldesc.setText("Anxiety Is the feeling of fear or apprehension of about what’s to come. This is a natural body" +
                        " response to stress. The first day of school, going for your job interview, or giving a speech in front of an" +
                        " audience may cause most people to feel nervous and fearful. See for more information and help below.");
                break;
            case "Belonging":
                resourceList = Resource.getBelongingResources();
                mentaldesc.setText("For many Indigenous Australians, land relates to all aspects of existence such as identity," +
                        " language, culture, spirituality and family. Each person belongs to a piece of land which they’re" +
                        " related to through the kinship system. Significant risk factors impacting upon the social emotional" +
                        " wellbeing of Aboriginal and Torres Strait Islander communities can include a sense of disconnection" +
                        " and belonging caused by economic and social disadvantages, separation from culture, identity issues and" +
                        " more. See for more information and help below.");
                break;
            case "Perinatal Health":
                resourceList = Resource.getPerinatalHealthResources();
                mentaldesc.setText("Adjusting to a new baby is rewarding but can also bring challenges and major changes to parents." +
                        " This may include experiencing physical health problems during pregnancy, birth or early parenthood." +
                        " Mental health conditions can happen to anyone, thus is it important to recognise these signs and seek" +
                        " support early. The most common types of mental health conditions are anxiety and depression in the" +
                        " perinatal period. Please talk to a general practitioner (GP) or another health professional as your" +
                        " first step in getting support. See for more info and support below.");
                break;
            case "Bullying":
                resourceList = Resource.getBullyingResources();
                mentaldesc.setText("Bullying can occur at any point in our lives, and it is never easy to deal with to anyone of " +
                        "all ages. Bullying is when people intentionally use harmful words or actions against someone or a group of " +
                        "people to cause distress and damage to their wellbeing. Bullying can happen anywhere, whether at school, " +
                        "at home, at work, or even in online social spaces via text messages or email. This can be physical, verbal," +
                        " emotional, and also include messages, public statements and behaviour online intended to cause harm." +
                        " See for more info and support below.");
                break;
        }

        mAdapter = new ResourceTopicAdapter(ResourceTopicActivity.this, resourceList);
        rvResourceList.setAdapter(mAdapter);

    }
}