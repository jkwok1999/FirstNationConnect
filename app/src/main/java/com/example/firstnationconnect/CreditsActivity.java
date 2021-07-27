package com.example.firstnationconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {

    TextView tvCreditsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        tvCreditsContent = findViewById(R.id.tvCreditsContent);
        tvCreditsContent.setText("Add credits here");
    }
}