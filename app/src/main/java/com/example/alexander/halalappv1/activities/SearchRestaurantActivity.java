package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.CategoriesAdapter;
import com.example.alexander.halalappv1.adapters.HomeRestaurantAdapter;
import com.example.alexander.halalappv1.model.City;
import com.example.alexander.halalappv1.model.newModels.Category;
import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.services.GetCategoriesService;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.services.SearchRestaurantService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRestaurantActivity extends AppCompatActivity implements HomeRestaurantAdapter.OnRestaurantClickListener {

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
    private RelativeLayout searchLayout;
    private String mAction;
    private City mCity;
    private TextView searchHeader;

    private int mCuisineId;
    private int mPrice;
    private String mSortBy;

    private String mLocationAction;

    // new design views and components
    private RecyclerView categoriesRecyclerView, searchRecyclerView;
    private CategoriesAdapter categoriesAdapter;
    private HomeRestaurantAdapter searchResultAdapter;
    private ArrayList<Category> categoriesList;
    private ArrayList<Restaurant> searchResultList;
    private TextView categoriesTitle, searchClear;
    private ImageView searchIcon;
    private RelativeLayout editTextSearchLayout;
    public static final String RESTAURENT_KEY = "RestaurentKey";
    public static final String SEARCH_KEY = "searchKey";
    private String mSearchKeywords;



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
        arrowBackClick();

        if (mAction.equals(ConstantsHelper.ACTION_HOME_CATEGORIES)){
            editTextSearchLayout.setVisibility(View.INVISIBLE);
            searchHeader.setText("");
        }

    }

    // new design views initialization
    private void initNewDesignViews(){
        categoriesList = new ArrayList<>();
        searchResultList = new ArrayList<>();
        // init recycler's adapter
        categoriesAdapter = new CategoriesAdapter(categoriesList,this);
        searchResultAdapter = new HomeRestaurantAdapter(this,this);
        searchResultAdapter.setRestaurantList(searchResultList);
        // init recycler
        categoriesRecyclerView = findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        // search result recycler init
        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setAdapter(searchResultAdapter);
        // init categories title
        categoriesTitle = findViewById(R.id.cat_title);
        //relative layout to handle focus and ignore the focus on search edit text when activity created
        searchLayout = findViewById(R.id.activity_search_layout);
        // other views initialization
        mArrowBackImageView = findViewById(R.id.iv_search_restaurant_arrow_back);
        mSearchRestaurantEditText = findViewById(R.id.et_search_restaurant_search);
        searchIcon = findViewById(R.id.search_restaurant_search_icon);
        searchClear = findViewById(R.id.search_restaurant_clear);
        editTextSearchLayout = findViewById(R.id.search_edit_frame);
        searchHeader = findViewById(R.id.tv_search_restaurant_header_label);
        // actions on search edit texts icons
        watchSearchEditText();
        clearSearchET();
        searchAction();
        // get all categories from api
        getAllCategories();

    }

    private void watchSearchEditText(){
        mSearchRestaurantEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mSearchRestaurantEditText.getText().length() > 0)
                    searchClear.setVisibility(View.VISIBLE);
                else
                    searchClear.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mSearchRestaurantEditText.getText().length() > 0)
                    searchClear.setVisibility(View.VISIBLE);
                else
                    searchClear.setVisibility(View.GONE);
            }
        });
    }

    private void clearSearchET(){
        searchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchRestaurantEditText.setText("");
            }
        });
    }

    private void searchAction(){
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchKeywords = mSearchRestaurantEditText.getText().toString();
                getSearchData(mSearchKeywords);
            }
        });
    }

    //==============================================================================================
    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                        searchRecyclerView.setVisibility(View.GONE);
                        categoriesRecyclerView.setVisibility(View.VISIBLE);
                        categoriesTitle.setVisibility(View.VISIBLE);
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


    // get search result from api
    private void getSearchData(String searchKeywords) {
        SearchRestaurantService webService = RetrofitWebService.retrofit.create(SearchRestaurantService.class);
        Call<JsonArray> call = webService.getSearchResponse(searchKeywords);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray jsonArray = response.body();
                    if (jsonArray != null) {
                        Gson gson = new Gson();
                        ArrayList<Restaurant> tempList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Restaurant>>(){}.getType());
                            searchResultList.clear();
                            searchResultList.addAll(tempList);
                            searchResultAdapter.notifyDataSetChanged();
                        if (tempList.size() > 0) {
                            searchRecyclerView.setVisibility(View.VISIBLE);
                            categoriesRecyclerView.setVisibility(View.GONE);
                            categoriesTitle.setVisibility(View.GONE);
                        }else{
                            searchRecyclerView.setVisibility(View.GONE);
                            categoriesRecyclerView.setVisibility(View.VISIBLE);
                            categoriesTitle.setVisibility(View.VISIBLE);
                            Toast.makeText(SearchRestaurantActivity.this, getResources().getString(R.string.no_results), Toast.LENGTH_SHORT).show();
                        }
                        Log.i("searchResult",jsonArray.toString()+" ---> results");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(SearchRestaurantActivity.this, t.getLocalizedMessage()+"", Toast.LENGTH_SHORT).show();
                Log.i("searchResult",t.getLocalizedMessage()+"");
            }
        });
    }

    // when search item clicked
    @Override
    public void onRestaurantClick(int parentPosition, int childPosition) {
        Intent intent = new Intent(this, RestaurantProfileActivity.class);
        intent.putExtra(RESTAURENT_KEY, childPosition);
        intent.setAction(ConstantsHelper.ACTION_SEARCH_ACTIVITY);
        startActivity(intent);
    }
}
