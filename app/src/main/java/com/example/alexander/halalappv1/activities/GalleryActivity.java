package com.example.alexander.halalappv1.activities;

import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.gallery.GalleryPagerAdapter;
import com.example.alexander.halalappv1.services.RetrofitWebService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryActivity extends AppCompatActivity {

    private TextView mRestaurantNameTextView;
    private ImageView mArrowBackImageView;
    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private ConstraintLayout mLoadingIndicator;
    private LinearLayout mCircleIndicatorLayout;
    private ImageView mCircle1;
    private ImageView mCircle2;
    private ImageView mCircle3;

    private GalleryPagerAdapter mGalleryPagerAdapter;
    private ArrayList<String> mAllImagesList;
    private List<List<String>> mGridImagesList;

    private String mRestaurantName;
    private int mRestaurantId;

    private void findViewsById() {
        mRestaurantNameTextView = findViewById(R.id.tv_gallery_activity_restaurant_name);
        mArrowBackImageView = findViewById(R.id.iv_gallery_activity_arrow_back);
        mViewPager = findViewById(R.id.gallery_activity_view_pager);
        mCircleIndicator = findViewById(R.id.gallery_activity_circle_indicator);
        mLoadingIndicator = findViewById(R.id.pb_gallery_activity_loading_indicator);
        mCircleIndicatorLayout = findViewById(R.id.circle_indicator_layout);
        mCircle1 = findViewById(R.id.iv_circle_1);
        mCircle2 = findViewById(R.id.iv_circle_2);
        mCircle3 = findViewById(R.id.iv_circle_3);
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void getGalleryImages(int restaurantId) {
        if (restaurantId != -10) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<ArrayList<String>> call = webService.getGalleryImages(restaurantId);
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        mAllImagesList = response.body();
                        if (mAllImagesList != null && mAllImagesList.size() > 0) {
                            Set<String> hashSet = new HashSet<>();
                            hashSet.addAll(mAllImagesList);
                            mAllImagesList.clear();
                            mAllImagesList.addAll(hashSet);

                            mGridImagesList = splitAndReturn(mAllImagesList, 9);
                            mGalleryPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager(), mGridImagesList, mGridImagesList.size(), mRestaurantName);
                            mViewPager.setAdapter(mGalleryPagerAdapter);
                            mCircleIndicator.setViewPager(mViewPager);
                            mCircleIndicatorLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    hideLoadingIndicator();
                }
            });
        }
    }

    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        findViewsById();

        mRestaurantName = getIntent().getStringExtra(RestaurantProfileActivity.RESTAURANT_NAME_KEY);
        mRestaurantId = getIntent().getIntExtra(RestaurantProfileActivity.RESTAURANT_ID_KEY, -10);

        if (mRestaurantName != null) {
            mRestaurantNameTextView.setText(mRestaurantName);
        }

        showLoadingIndicator();

        getGalleryImages(mRestaurantId);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setSelectedCircle(position % 3);
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        arrowBackClick();
    }

    private void setSelectedCircle(int position) {
        if (position == 0) {
            mCircle1.setImageResource(R.drawable.shape_circle_indicator_selected);
            mCircle2.setImageResource(R.drawable.shape_circle_indicator_unselected);
            mCircle3.setImageResource(R.drawable.shape_circle_indicator_unselected);
        }

        if (position == 1) {
            mCircle1.setImageResource(R.drawable.shape_circle_indicator_unselected);
            mCircle2.setImageResource(R.drawable.shape_circle_indicator_selected);
            mCircle3.setImageResource(R.drawable.shape_circle_indicator_unselected);
        }

        if (position == 2) {
            mCircle1.setImageResource(R.drawable.shape_circle_indicator_unselected);
            mCircle2.setImageResource(R.drawable.shape_circle_indicator_unselected);
            mCircle3.setImageResource(R.drawable.shape_circle_indicator_selected);
        }
    }

    private List<List<String>> splitAndReturn(List<String> gridImagesList, int size) {
        List<List<String>> list = new ArrayList<List<String>>();
        int i = 0;
        while (i + size < gridImagesList.size()) {
            list.add(gridImagesList.subList(i, i + size));
            i = i + size;
        }
        list.add(gridImagesList.subList(i, gridImagesList.size()));
        return list;
    }
}
