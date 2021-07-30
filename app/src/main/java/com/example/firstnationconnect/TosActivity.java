package com.example.firstnationconnect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TosActivity extends AppCompatActivity implements View.OnClickListener {

    Button btAcceptTerms;
    TextView tvTermsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Tutorial: https://www.youtube.com/watch?v=fn5OlqQuOCk
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels*.8);
        int height = (int) (dm.heightPixels*.6);

        getWindow().setLayout(width, height);

        tvTermsContent = findViewById(R.id.tvTermsContent);
        btAcceptTerms = findViewById(R.id.btAcceptTerms);
        btAcceptTerms.setOnClickListener(this);

        tvTermsContent.setText(
                "This is a citizen science project aimed to collect data for Red Cross Australia to assist in research and further support programs aimed to First Nations People" +
                "\n\nBy using this application, you are permitting Red Cross Australia to use your data (including information from posts you submit, date/time, geolocational data)"
        );
    }


    @Override
    public void onClick(View view) {
        finish();
    }
}