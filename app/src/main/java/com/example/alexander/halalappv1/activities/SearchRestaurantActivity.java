package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.CategoriesAdapter;
import com.example.alexander.halalappv1.adapters.SpinnerCitiesAdapter;
import com.example.alexander.halalappv1.model.City;
import com.example.alexander.halalappv1.model.newModels.Category;
import com.example.alexander.halalappv1.services.GetCategoriesService;
import com.example.alexander.halalappv1.services.Polices;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRestaurantActivity extends AppCompatActivity {

    public static final String CITY_LATITUDE_KEY = "CityLatitude";
    public static final String CITY_LONGITUDE_KEY = "CityLongitude";
    public static final String SEARCH_KEYWORDS_KEY = "SearchKeywords";

    public static final String ACTION_SEARCH_KEY = "ActionSearchKey";
    public static final String ACTION_SEARCH_VALUE = "ActionSearchValue";

    private ImageView mArrowBackImageView;
    private EditText mSearchRestaurantEditText;
    private TextView mSelectLocationTextView;
    private ConstraintLayout mAllRestaurantsLayout;
    private ConstraintLayout mMyFavouritesLayout;
    private Button mSearchButton;

    private String mAction;
    private String mSearchKeywords;
    private City mCity;
    private String mSelectedCity;
    private String mCityLatitude;
    private String mCityLongitude;

    private int mCuisineId;
    private int mPrice;
    private String mSortBy;

    private String mLocationAction;

    // new design views and components
    private RecyclerView categoriesRecyclerView;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Category> categoriesList;
    private TextView categoriesTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);
        mAction = getIntent().getAction();
        mCity = getIntent().getParcelableExtra(SelectLocationActivity.KEY_CITY_OBJECT);
        mCuisineId = getIntent().getIntExtra(FilterActivity.CUISINE_ID_KEY, -10);
        mPrice = getIntent().getIntExtra(FilterActivity.PRICE_KEY, -10);
        mSortBy = getIntent().getStringExtra(FilterActivity.SORT_BY_KEY);
        mLocationAction = getIntent().getStringExtra(SelectLocationActivity.KEY_SEARCH_BY);

        // this is related to the new design
        initNewDesignViews();
                    //\\

        findViewsById(); // (1)

//        getCityDataFromSharedPreferencesAndUpdateView(); // (2)

        arrowBackClick();

//        selectLocationTextViewClick();

//        allRestaurantsLayoutClick();

//        myFavouritesLayout();

//        searchButtonClick();
    }

    // new design views initialization
    private void initNewDesignViews(){
        categoriesList = new ArrayList<>();
        // init recycler's adapter
        categoriesAdapter = new CategoriesAdapter(categoriesList,this);
        // init recycler
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        // init categories title
        categoriesTitle = findViewById(R.id.cat_title);
        // get all categories from api
        getAllCategories();

    }


    private void findViewsById() {
        mArrowBackImageView = findViewById(R.id.iv_search_restaurant_arrow_back);
        mSearchRestaurantEditText = findViewById(R.id.et_search_restaurant_search);
//        mSelectLocationTextView = findViewById(R.id.tv_search_restaurant_location);
//        mAllRestaurantsLayout = findViewById(R.id.search_restaurant_all_restaurants_layout);
//        mMyFavouritesLayout = findViewById(R.id.search_restaurant_my_favourites_layout);
//        mSearchButton = findViewById(R.id.btn_search_restaurant_search);
    }

    /*private void getCityDataFromSharedPreferencesAndUpdateView() {
        mSelectedCity = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_SELECTED_CITY, null);
        mCityLatitude = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_CITY_LATITUDE, null);
        mCityLongitude = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_CITY_LONGITUDE, null);

        if (mLocationAction == null) {
            if (getIntent().getStringExtra("XXXXX") != null) {
                if (getIntent().getStringExtra("XXXXX").equals("search_by_location_value")) {
                    mSelectLocationTextView.setText(getResources().getString(R.string.tv_search_restaurant_location_text));
                } else if (getIntent().getStringExtra("XXXXX").equals("ActionSearchValue")) {
                    mSelectLocationTextView.setText(mSelectedCity);
                }
            } else {
                if (mSelectedCity != null) {
                    mSelectLocationTextView.setText(mSelectedCity);
                } else {
                    mSelectLocationTextView.setText(getResources().getString(R.string.tv_search_restaurant_select_city_text));
                }
            }
        } else {
            if (getIntent().getStringExtra("XXXXX") != null) {
                if (getIntent().getStringExtra("XXXXX").equals("search_by_location_value")) {
                    mSelectLocationTextView.setText(getResources().getString(R.string.tv_search_restaurant_location_text));
                } else if (getIntent().getStringExtra("XXXXX").equals("ActionSearchValue")) {
                    mSelectLocationTextView.setText(mSelectedCity);
                }
            } else {
                mSelectLocationTextView.setText(getResources().getString(R.string.tv_search_restaurant_location_text));
            }
        }

        if (TextUtils.isEmpty(mSelectLocationTextView.getText())) {
            mSelectLocationTextView.setText(getResources().getString(R.string.tv_search_restaurant_select_city_text));
        }
    }*/
    //==============================================================================================
    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAction != null) {
                    if (mAction.equals(ConstantsHelper.ACTION_HOME_FRAGMENT)) {
                        Intent intent = new Intent(SearchRestaurantActivity.this, MainActivity.class);
                        intent.putExtra(ACTION_SEARCH_KEY, getIntent().getStringExtra("XXXXX"));
                        intent.setAction(mAction);
                        startActivity(intent);
                    } else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)){
                        Intent intent = new Intent(SearchRestaurantActivity.this, MainActivity.class);
                        intent.putExtra(ACTION_SEARCH_KEY, getIntent().getStringExtra("XXXXX"));
                        intent.setAction(mAction);
                        startActivity(intent);
                    }
                }
            }
        });
    }

   /* private void selectLocationTextViewClick() {
        mSelectLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchRestaurantActivity.this, SelectLocationActivity.class);
                startActivity(intent);
            }
        });
    }*/
/*

                // This part is related to the old design \\

    private void allRestaurantsLayoutClick() {
        mAllRestaurantsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAction != null) {
                    Intent intent = new Intent(SearchRestaurantActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                    intent.putExtra("BBBBB", getResources().getString(R.string.tv_search_restaurant_all_restaurants_label_text));
                    startActivity(intent);
                }
            }
        });
    }

    private void myFavouritesLayout() {
        mMyFavouritesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchRestaurantActivity.this, FavouriteRestaurantsActivity.class);
                intent.setAction(mAction + "-" + ConstantsHelper.ACTION_SEARCH_RESTAURANT_ACTIVITY);
                startActivity(intent);
            }
        });
    }*/

   /* private void searchButtonClick() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationAction == null) {
                    mSearchKeywords = mSearchRestaurantEditText.getText().toString();
                    Intent intent = new Intent(SearchRestaurantActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                    intent.putExtra(ACTION_SEARCH_KEY, ACTION_SEARCH_VALUE);

                    intent.putExtra(CITY_LATITUDE_KEY, mCityLatitude);
                    intent.putExtra(CITY_LONGITUDE_KEY, mCityLongitude);
                    intent.putExtra(SEARCH_KEYWORDS_KEY, mSearchKeywords);
                    intent.putExtra(FilterActivity.CUISINE_ID_KEY, mCuisineId);
                    intent.putExtra(FilterActivity.PRICE_KEY, mPrice);
                    intent.putExtra(FilterActivity.SORT_BY_KEY, mSortBy);
                    startActivity(intent);
                }

                else {
                    mSearchKeywords = mSearchRestaurantEditText.getText().toString();
                    Intent intent = new Intent(SearchRestaurantActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                    intent.putExtra(ACTION_SEARCH_KEY, mLocationAction);

                    intent.putExtra(CITY_LATITUDE_KEY, mCityLatitude);
                    intent.putExtra(CITY_LONGITUDE_KEY, mCityLongitude);
                    intent.putExtra(SEARCH_KEYWORDS_KEY, mSearchKeywords);
                    intent.putExtra(FilterActivity.CUISINE_ID_KEY, mCuisineId);
                    intent.putExtra(FilterActivity.PRICE_KEY, mPrice);
                    intent.putExtra(FilterActivity.SORT_BY_KEY, mSortBy);
                    startActivity(intent);
                }
            }
        });
    }*/


    @Override
    public void onBackPressed() {
        if (mAction != null) {
            if (mAction.equals(ConstantsHelper.ACTION_HOME_FRAGMENT)) {
                Intent intent = new Intent(SearchRestaurantActivity.this, MainActivity.class);
                intent.putExtra(ACTION_SEARCH_KEY, getIntent().getStringExtra("XXXXX"));
                intent.setAction(mAction);
                startActivity(intent);
            }
            else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)){
                Intent intent = new Intent(SearchRestaurantActivity.this, MainActivity.class);
                intent.putExtra(ACTION_SEARCH_KEY, getIntent().getStringExtra("XXXXX"));
                intent.setAction(mAction);
                startActivity(intent);
            }
        }
    }

    // get categories from api
    private void getAllCategories() {
        GetCategoriesService webService = RetrofitWebService.retrofit.create(GetCategoriesService.class);
        Call<JsonArray> call = webService.getCategories();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray jsonArray = response.body();
                    if (jsonArray != null) {
                        Gson gson = new Gson();
                        ArrayList<Category> tempList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Category>>(){}.getType());
                        categoriesList.clear();
                        categoriesList.addAll(tempList);
                        categoriesAdapter.notifyDataSetChanged();
                        // set categories title with number of categories
                        String newCategoryTitle = getResources().getString(R.string.nos_categories) + " ("+ categoriesList.size() + ")";
                        categoriesTitle.setText(newCategoryTitle);
                        Log.i("AllCat",jsonArray.toString()+" ---> results");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(SearchRestaurantActivity.this, t.getLocalizedMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
