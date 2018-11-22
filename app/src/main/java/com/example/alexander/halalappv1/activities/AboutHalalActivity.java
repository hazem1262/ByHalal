package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.alexander.halalappv1.R;

public class AboutHalalActivity extends AppCompatActivity {

    private ImageView mArrowBakcImageView;
    private ConstraintLayout mPoliciesLayout;
    private ConstraintLayout mConditionsLayout;
    private ConstraintLayout mHelpLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_halal);


        mArrowBakcImageView = findViewById(R.id.about_halal_arrow_back);
        mPoliciesLayout = findViewById(R.id.about_halal_confidentiality_policies_layout);
        mConditionsLayout = findViewById(R.id.about_halal_conditions_of_utilization_layout);
        mHelpLayout = findViewById(R.id.about_halal_help_and_assistance_layout);

        mArrowBakcImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPoliciesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutHalalActivity.this, PoliciesActivity.class);
                startActivity(intent);
            }
        });

        mConditionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutHalalActivity.this, ConditionsActivity.class);
                startActivity(intent);
            }
        });

        mHelpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutHalalActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
    }
}
