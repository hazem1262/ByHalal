package com.example.alexander.halalappv1.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.WorkDay;
import com.example.alexander.halalappv1.adapters.WorkingHoursAdapter;

import java.util.ArrayList;

public class WorkingHoursActivity extends AppCompatActivity {

    private TextView mRestaurantNameTextView;
    private ImageView mArrowBackImageView;
    private RecyclerView mWorkingHoursRecyclerView;
    private WorkingHoursAdapter mWorkingHoursAdapter;
    private ArrayList<WorkDay> mWorkDaysList;
    private String mRestaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_hours);


        mRestaurantName = getIntent().getStringExtra("RestaurantName");
        mWorkDaysList = getIntent().getParcelableArrayListExtra("workDaysList");

        mRestaurantNameTextView = findViewById(R.id.tv_working_hours_restaurant_name);
        mRestaurantNameTextView.setText(mRestaurantName);

        mArrowBackImageView = findViewById(R.id.working_hours_arrow_back);
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWorkingHoursRecyclerView = findViewById(R.id.rv_working_hours);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mWorkingHoursRecyclerView.setLayoutManager(layoutManager);
        mWorkingHoursAdapter = new WorkingHoursAdapter(this);
        mWorkingHoursAdapter.setWorkDaysList(mWorkDaysList);
        mWorkingHoursRecyclerView.setAdapter(mWorkingHoursAdapter);
    }
}
