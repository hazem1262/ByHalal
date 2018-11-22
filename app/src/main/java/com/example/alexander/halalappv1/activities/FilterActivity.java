package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.CuisineSpinnerAdapter;
import com.example.alexander.halalappv1.fragments.HomeFragment;
import com.example.alexander.halalappv1.model.Cuisine;
import com.example.alexander.halalappv1.model.RestaurantsList;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {


    public static final String CUISINE_ID_KEY = "CuisineId";
    public static final String PRICE_KEY = "Price";
    public static final String SORT_BY_KEY = "SortBy";

    public static final String ACTION_FILTER_KEY = "ActionFilterKey";
    public static final String ACTION_FILTER_VALUE = "ActionFilterValue";

    private ImageView mArrowBackImageView;
    private Spinner mCuisinesSpinner;
    private ConstraintLayout mPriceOneEuroLayout;
    private ConstraintLayout mPriceTwoEuroLayout;
    private ConstraintLayout mPriceThreeEuroLayout;
    private ConstraintLayout mPriceFourEuroLayout;
    private ConstraintLayout mSortByLocationLayout;
    private ConstraintLayout mSortByPriceLayout;
    private ConstraintLayout mSortByFromAToZLayout;

    private TextView mResetTextView;
    private Button mShowOptionsButton;
    private ConstraintLayout mLoadingIndicator;

    private ImageView mOneEuroImageView;
    private ImageView mTwoEuroImageView;
    private ImageView mThreeEuroImageView;
    private ImageView mFourEuroImageView;

    private ImageView mSortLocationImageView;
    private ImageView mSortPriceImageView;
    private TextView mAZTextView;

    private View mLineFirstView;
    private View mLineSecondView;

    private void findViewsById() {
        mArrowBackImageView = findViewById(R.id.layout_filter_arrow_back);
        mCuisinesSpinner = findViewById(R.id.spinner_cuisines);
        mPriceOneEuroLayout = findViewById(R.id.price_layout_one_euro);
        mPriceTwoEuroLayout = findViewById(R.id.price_layout_two_euro);
        mPriceThreeEuroLayout = findViewById(R.id.price_layout_three_euro);
        mPriceFourEuroLayout = findViewById(R.id.price_layout_four_euro);
        mSortByLocationLayout = findViewById(R.id.sort_by_location_layout);
        mSortByPriceLayout = findViewById(R.id.sort_by_euro_layout);
        mSortByFromAToZLayout = findViewById(R.id.sort_by_a_to_z_layout);

        mResetTextView = findViewById(R.id.tv_layout_filter_reset);
        mShowOptionsButton = findViewById(R.id.btn_layout_filter_show_options);
        mLoadingIndicator = findViewById(R.id.pb_filter_activity_loading_indicator);


        mOneEuroImageView = findViewById(R.id.iv_price_one_euro);
        mTwoEuroImageView = findViewById(R.id.iv_price_two_euro);
        mThreeEuroImageView = findViewById(R.id.iv_price_three_euro);
        mFourEuroImageView = findViewById(R.id.iv_price_four_euro);

        mSortLocationImageView = findViewById(R.id.iv_sort_location);
        mSortPriceImageView = findViewById(R.id.iv_sort_price);
        mAZTextView = findViewById(R.id.tv_sort_az);

        mLineFirstView = findViewById(R.id.view_filter_sort_by_line_first);
        mLineSecondView = findViewById(R.id.view_filter_sort_by_line_second);
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }
    //==============================================================================================
    private CuisineSpinnerAdapter mCuisineSpinnerAdapter;
    private ArrayList<Cuisine> mCuisinesList;

    private void setUpCuisinesSpinner() {
        mCuisinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View spinnerDivider = view.findViewById(R.id.spinner_list_item_divider);
                spinnerDivider.setVisibility(View.GONE);
                Cuisine cuisine = mCuisinesList.get(position);
                mCuisineId = cuisine.getCuisineId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void getCuisines() {
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<ArrayList<Cuisine>> cuisinesListCall = webService.getCuisines();
        cuisinesListCall.enqueue(new Callback<ArrayList<Cuisine>>() {
            @Override
            public void onResponse(Call<ArrayList<Cuisine>> call, Response<ArrayList<Cuisine>> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    mCuisinesList = response.body();
                    Cuisine cuisine = new Cuisine();
                    mCuisinesList.add(0,cuisine);
                    if (mCuisinesList != null && mCuisinesList.size() > 0) {
                        mCuisineSpinnerAdapter = new CuisineSpinnerAdapter(FilterActivity.this, R.layout.spinner_list_item, mCuisinesList);
                        mCuisinesSpinner.setAdapter(mCuisineSpinnerAdapter);

                        for (int i = 0; i < mCuisinesList.size(); i ++) {
                            if (mCuisinesList.get(i).getCuisineId() == mCuisineId) {
                                mCuisinesSpinner.setSelection(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cuisine>> call, Throwable t) {}
        });
    }
    //==============================================================================================
    private String mAction;
    private RestaurantsList mTable;
    private int mCuisineId;
    private int mPrice;
    private String mSortBy;

    private double mCityLatitude;
    private double mCityLongitude;
    private String mSearchKeywords;

    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //==============================================================================================
    private void priceOneEuroLayoutClick() {
        mPriceOneEuroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrice = 1;

                mPriceOneEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
                mOneEuroImageView.setImageResource(R.drawable.ic_euro_1);

                mPriceTwoEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mTwoEuroImageView.setImageResource(R.drawable.ic_euro_2_black);

                mPriceThreeEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mThreeEuroImageView.setImageResource(R.drawable.ic_euro_3_black);

                mPriceFourEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mFourEuroImageView.setImageResource(R.drawable.ic_euro_4_black);
            }
        });
    }

    private void priceTwoEuroLayoutClick() {
        mPriceTwoEuroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrice = 2;

                mPriceOneEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mOneEuroImageView.setImageResource(R.drawable.ic_euro_black);

                mPriceTwoEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
                mTwoEuroImageView.setImageResource(R.drawable.ic_euro_2_white);

                mPriceThreeEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mThreeEuroImageView.setImageResource(R.drawable.ic_euro_3_black);

                mPriceFourEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mFourEuroImageView.setImageResource(R.drawable.ic_euro_4_black);
            }
        });
    }

    private void priceThreeEuroLayoutClick() {
        mPriceThreeEuroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrice = 3;

                mPriceOneEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mOneEuroImageView.setImageResource(R.drawable.ic_euro_black);

                mPriceTwoEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mTwoEuroImageView.setImageResource(R.drawable.ic_euro_2_black);

                mPriceThreeEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
                mThreeEuroImageView.setImageResource(R.drawable.ic_euro_3_white);

                mPriceFourEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mFourEuroImageView.setImageResource(R.drawable.ic_euro_4_black);
            }
        });
    }

    private void priceFourEuroLayoutClick() {
        mPriceFourEuroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrice = 4;

                mPriceOneEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mOneEuroImageView.setImageResource(R.drawable.ic_euro_black);

                mPriceTwoEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mTwoEuroImageView.setImageResource(R.drawable.ic_euro_2_black);

                mPriceThreeEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mThreeEuroImageView.setImageResource(R.drawable.ic_euro_3_black);

                mPriceFourEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
                mFourEuroImageView.setImageResource(R.drawable.ic_euro_4_white);
            }
        });
    }
    //==============================================================================================
    private void sortByLocationLayoutClick() {
        mSortByLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortBy = "location";

                mSortByLocationLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_left_8);
                mSortLocationImageView.setImageResource(R.drawable.filter_location_white);

                mSortByPriceLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black);
                mSortPriceImageView.setImageResource(R.drawable.filter_euro_black);

                mSortByFromAToZLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_right_8);
                mAZTextView.setTextColor(Color.parseColor("#2A2C34"));

                mLineFirstView.setVisibility(View.GONE);
                mLineSecondView.setVisibility(View.GONE);
            }
        });
    }

    private void sortByPriceLayoutClick() {
        mSortByPriceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortBy = "price";

                mSortByLocationLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_left_8);
                mSortLocationImageView.setImageResource(R.drawable.filter_location_black);

                mSortByPriceLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black);
                mSortPriceImageView.setImageResource(R.drawable.filter_euro_white);

                mSortByFromAToZLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_right_8);
                mAZTextView.setTextColor(Color.parseColor("#2A2C34"));

                mLineFirstView.setVisibility(View.GONE);
                mLineSecondView.setVisibility(View.GONE);
            }
        });
    }

    private void sortByFromAToZLayoutClick() {
        mSortByFromAToZLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortBy = "AZ";

                mSortByLocationLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_left_8);
                mSortLocationImageView.setImageResource(R.drawable.filter_location_black);

                mSortByPriceLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black);
                mSortPriceImageView.setImageResource(R.drawable.filter_euro_black);

                mSortByFromAToZLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_right_8);
                mAZTextView.setTextColor(Color.parseColor("#FFFFFF"));

                mLineFirstView.setVisibility(View.GONE);
                mLineSecondView.setVisibility(View.GONE);
            }
        });
    }
    //==============================================================================================
    private void resetTextViewClick() {
        mResetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCuisinesSpinner.setSelection(0);

                mPriceOneEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mOneEuroImageView.setImageResource(R.drawable.ic_euro_black);

                mPriceTwoEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mTwoEuroImageView.setImageResource(R.drawable.ic_euro_2_black);

                mPriceThreeEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mThreeEuroImageView.setImageResource(R.drawable.ic_euro_3_black);

                mPriceFourEuroLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_8);
                mFourEuroImageView.setImageResource(R.drawable.ic_euro_4_black);


                mSortByLocationLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_left_8);
                mSortLocationImageView.setImageResource(R.drawable.filter_location_black);

                mSortByPriceLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black);
                mSortPriceImageView.setImageResource(R.drawable.filter_euro_black);

                mSortByFromAToZLayout.setBackgroundResource(R.drawable.shape_bg_white_b_black_br_right_8);
                mAZTextView.setTextColor(Color.parseColor("#2A2C34"));

                mLineFirstView.setVisibility(View.GONE);
                mLineSecondView.setVisibility(View.GONE);

                mCuisineId = -10;
                mPrice = -10;
                mSortBy = null;
            }
        });
    }

    private void showOptionsButtonClick() {
        mShowOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAction != null) {
                    if (mAction.equals(ConstantsHelper.ACTION_TABLE_RESTAURANT_ACTIVITY)) {
                        Intent intent = new Intent(FilterActivity.this, TableRestaurantsActivity.class);
                        intent.putExtra(HomeFragment.TABLE_OBJECT_KEY, mTable);
                        intent.putExtra(CUISINE_ID_KEY, mCuisineId);
                        intent.putExtra(PRICE_KEY, mPrice);
                        intent.putExtra(SORT_BY_KEY, mSortBy);
                        startActivity(intent);
                    }

//                    else {
//                        Intent intent = new Intent(FilterActivity.this, TableRestaurantsActivity.class);
//                        intent.putExtra(ACTION_FILTER_KEY, ACTION_FILTER_VALUE);
//                        intent.putExtra(CUISINE_ID_KEY, mCuisineId);
//                        intent.putExtra(PRICE_KEY, mPrice);
//                        intent.putExtra(SORT_BY_KEY, mSortKey);
//                        startActivity(intent);
//                    }
                }

                else {
                    Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                    intent.putExtra(ACTION_FILTER_KEY, ACTION_FILTER_VALUE);

                    intent.putExtra(CUISINE_ID_KEY, mCuisineId);
                    intent.putExtra(PRICE_KEY, mPrice);
                    intent.putExtra(SORT_BY_KEY, mSortBy);
                    intent.putExtra(SearchRestaurantActivity.CITY_LATITUDE_KEY, mCityLatitude);
                    intent.putExtra(SearchRestaurantActivity.CITY_LONGITUDE_KEY, mCityLongitude);
                    intent.putExtra(SearchRestaurantActivity.SEARCH_KEYWORDS_KEY, mSearchKeywords);
                    startActivity(intent);
                }
            }
        });
    }
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        mAction = getIntent().getAction();
        mTable = getIntent().getParcelableExtra(HomeFragment.TABLE_OBJECT_KEY);
        mCityLatitude = getIntent().getDoubleExtra(SearchRestaurantActivity.CITY_LATITUDE_KEY, -10);
        mCityLongitude = getIntent().getDoubleExtra(SearchRestaurantActivity.CITY_LONGITUDE_KEY, -10);
        mSearchKeywords = getIntent().getStringExtra(SearchRestaurantActivity.SEARCH_KEYWORDS_KEY);

        mCuisineId = getIntent().getIntExtra(FilterActivity.CUISINE_ID_KEY, -10);
        mPrice = getIntent().getIntExtra(FilterActivity.PRICE_KEY, -10);
        mSortBy = getIntent().getStringExtra(FilterActivity.SORT_BY_KEY);

        findViewsById(); // (1)

        getCuisines(); // (2)

        showLoadingIndicator(); // (3)

        setUpCuisinesSpinner(); // (4)

        if (mPrice == 1) {
            mPriceOneEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
            mOneEuroImageView.setImageResource(R.drawable.ic_euro_1);
        }
        else if (mPrice == 2) {
            mPriceTwoEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
            mTwoEuroImageView.setImageResource(R.drawable.ic_euro_2_white);
        }
        else if (mPrice == 3) {
            mPriceThreeEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
            mThreeEuroImageView.setImageResource(R.drawable.ic_euro_3_white);
        }
        else if (mPrice == 4) {
            mPriceFourEuroLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_8);
            mFourEuroImageView.setImageResource(R.drawable.ic_euro_4_white);
        }

        if (mSortBy != null) {
            if (mSortBy.equals("location")) {
                mSortByLocationLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_left_8);
                mSortLocationImageView.setImageResource(R.drawable.filter_location_white);
            }
            else if (mSortBy.equals("price")) {
                mSortByPriceLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black);
                mSortPriceImageView.setImageResource(R.drawable.filter_euro_white);
            }
            else if (mSortBy.equals("AZ")) {
                mSortByFromAToZLayout.setBackgroundResource(R.drawable.shape_bg_black_b_black_br_right_8);
                mAZTextView.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
        //==========================================================================================
        arrowBackClick();

        priceOneEuroLayoutClick();

        priceTwoEuroLayoutClick();

        priceThreeEuroLayoutClick();

        priceFourEuroLayoutClick();


        sortByLocationLayoutClick();

        sortByPriceLayoutClick();

        sortByFromAToZLayoutClick();


        resetTextViewClick();


        showOptionsButtonClick();
    }
}
