package com.example.alexander.halalappv1.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.gallery.GalleryFragment;
import com.squareup.picasso.Picasso;

public class DisplayImageActivity extends AppCompatActivity {

    private TextView mRestaurantNameTextView;
    private ImageView mArrowBackImageView;
    private ImageView mGalleryImageView;

    private String mRestaurantName;
    private String mImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);


        mRestaurantNameTextView = findViewById(R.id.tv_display_image_activity_restaurant_name);
        mArrowBackImageView = findViewById(R.id.iv_display_image_activity_arrow_back);
        mGalleryImageView = findViewById(R.id.iv_display_image_activity_gallery_image);

        mRestaurantName = getIntent().getStringExtra(GalleryFragment.RESTAURANT_NAME_KEY);
        mImageUrl = getIntent().getStringExtra(GalleryFragment.IMAGE_URL_KEY);

        if (mRestaurantName != null) {
            mRestaurantNameTextView.setText(mRestaurantName);
        }

        if (mImageUrl != null) {
            Picasso.with(this).load(mImageUrl).into(mGalleryImageView);
        }

        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
