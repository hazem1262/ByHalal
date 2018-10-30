package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.RestaurantsMapAdapter;
import com.example.alexander.halalappv1.fragments.HomeFragment;
import com.example.alexander.halalappv1.adapters.SearchRestaurantAdapter;
import com.example.alexander.halalappv1.model.modifiedmodels.RestaurantsList1;
import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.services.CategoryRestaurents;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.example.alexander.halalappv1.fragments.HomeFragment.CATEGORY_NAME;
import static com.example.alexander.halalappv1.fragments.SearchFragment.RESTAURANT_ID_KEY;

public class TableRestaurantsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "xxxxx";
    public static final String RESTAURANT_OBJECT_KEY = "RestaurantObject";
    //==============================================================================================
    private TextView mTableNameTextView;
    private TextView mArrowBackImageView;
    private RecyclerView mRestaurantsRecyclerView;
    private Button mMapButton;
    private Button mFilterButton;
    private ConstraintLayout mLoadingIndicator;

    private boolean isNetworkOk;
    private boolean isLoggedIn;

    private void findViewsById() {
        mTableNameTextView = findViewById(R.id.tv_table_restaurants_activity_table_name);
        mArrowBackImageView = findViewById(R.id.iv_table_restaurants_activity_arrow_back);
        mRestaurantsRecyclerView = findViewById(R.id.rv_table_restaurants_activity_restaurants_list);
        mMapButton = findViewById(R.id.btn_table_restaurants_activity_map);
        mFilterButton = findViewById(R.id.btn_table_restaurants_activity_filter);
        mLoadingIndicator = findViewById(R.id.pb_table_restaurants_activity_loading_indicator);

        mRestaurantsMapRecyclerView = findViewById(R.id.rv_table_restaurants_activity_restaurants_map_list);
        mMapView = findViewById(R.id.table_restaurants_activity_map);
    }

    private SearchRestaurantAdapter mSearchRestaurantAdapter;
    private ArrayList<Restaurant> mRestaurantsList;

    private void setUpRestaurantsRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRestaurantsRecyclerView.setLayoutManager(layoutManager);
        mSearchRestaurantAdapter = new SearchRestaurantAdapter(this, new SearchRestaurantAdapter.OnRestaurantListClickListener() {
            @Override
            public void onFavoriteButtonClick(int itemPosition) {
                isNetworkOk = NetworkHelper.hasNetworkAccess(TableRestaurantsActivity.this);
                isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(TableRestaurantsActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, false);
                if (isNetworkOk) {
                    if (isLoggedIn) {
                        int userId = SharedPreferencesHelper.getSharedPreferenceInt(TableRestaurantsActivity.this, ConstantsHelper.KEY_USER_ID, -10);
                        showLoadingIndicator();
//                        markAsFavourite(userId, mRestaurantsList.get(itemPosition).getId(), itemPosition);
                    } else {
                        Intent intent = new Intent(TableRestaurantsActivity.this, FavouriteRestaurantsActivity.class);
                        intent.putExtra(RESTAURANT_ID_KEY, mRestaurantsList.get(itemPosition).getId());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(TableRestaurantsActivity.this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onListItemClick(int itemPosition) {
                Restaurant restaurant = mRestaurantsList.get(itemPosition);
                Intent intent = new Intent(TableRestaurantsActivity.this, RestaurantProfileActivity.class);
                intent.putExtra(HomeFragment.TABLE_ID_KEY, mTableId);
                intent.putExtra(HomeFragment.TABLE_OBJECT_KEY, mTable);
//                intent.putExtra(RESTAURANT_OBJECT_KEY, restaurant);
                intent.setAction(ConstantsHelper.ACTION_TABLE_RESTAURANT_ACTIVITY);
                startActivity(intent);
            }
        });
    }
    //==============================================================================================
    private RecyclerView mRestaurantsMapRecyclerView;
    private RestaurantsMapAdapter mRestaurantsMapAdapter;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private boolean isMapReady = false;
    private boolean isMapVisible = false;
    private ArrayList<Integer> mIndexList = new ArrayList<>();

    private void setUpRestaurantsMapRecyclerView() {
        final LinearLayoutManager restaurantMaplayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRestaurantsMapRecyclerView.setLayoutManager(restaurantMaplayoutManager);
        mRestaurantsMapAdapter = new RestaurantsMapAdapter(this, new RestaurantsMapAdapter.OnRestaurantMapListClickListener() {
            @Override
            public void onMapListItemClick(int position) {
                Restaurant restaurant = mRestaurantsList.get(position);
                Intent intent = new Intent(TableRestaurantsActivity.this, RestaurantProfileActivity.class);
                intent.putExtra(HomeFragment.TABLE_ID_KEY, mTableId);
                intent.putExtra(HomeFragment.TABLE_OBJECT_KEY, mTable);
//                intent.putExtra(RESTAURANT_OBJECT_KEY, restaurant);
                intent.setAction(ConstantsHelper.ACTION_TABLE_RESTAURANT_ACTIVITY);
                startActivity(intent);
            }
        });
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRestaurantsMapRecyclerView);

//        mRestaurantsMapRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (isMapReady) {
//                    if (newState == SCROLL_STATE_IDLE) {
//                        int scrollPosition = restaurantMaplayoutManager.findFirstCompletelyVisibleItemPosition();
//                        mIndexList.add(scrollPosition);
//                        if (scrollPosition != -1) {
//                            if (scrollPosition == mIndexList.get(mIndexList.size() - 1)) {
//                                double latitude = (mRestaurantsList.get(scrollPosition).getLatitude());
//                                double longitude = (mRestaurantsList.get(scrollPosition).getLongitude());
//                                LatLng latLng = new LatLng(latitude, longitude);
//
//                                mMarker = mGoogleMap.addMarker(new MarkerOptions()
//                                        .position(latLng)
//                                        .visible(true)
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_black)));
//                                CameraPosition target = CameraPosition.builder().target(latLng).zoom(15).build();
//                                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target));
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                mMarker.remove();
//                if (dx > 0) {
//                    mMarker = mGoogleMap.addMarker(new MarkerOptions()
//                            .position(new LatLng((mRestaurantsList.get(0).getLatitude()), (mRestaurantsList.get(0).getLongitude())))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pink)));
//                }
//                if (recyclerView.canScrollHorizontally(-1)) {
//                    mMarker = mGoogleMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(mRestaurantsList.get(mRestaurantsList.size() - 1).getLatitude(), (mRestaurantsList.get(mRestaurantsList.size() - 1).getLongitude())))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pink)));
//                }
//            }
//        });
    }

    private void setUpMapView(Bundle savedInstanceState) {
        mMapView.setVisibility(View.GONE);
        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //==============================================================================================
    private RestaurantsList1 mTable;
    private int mUserId;
    private int mTableId;
    private String mTableName;

    private int mCuisineId;
    private int mPrice;
    private String mSortBy;
    //==============================================================================================
    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

//    private void mapButtonClick() {
//        mMapButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isMapVisible) {
//                    isMapVisible = false;
//                    mRestaurantsRecyclerView.setVisibility(View.VISIBLE);
//                    mMapView.setVisibility(View.GONE);
//                    mRestaurantsMapRecyclerView.setVisibility(View.GONE);
//                    mMapButton.setText(getResources().getString(R.string.btn_search_fragment_map_text));
//                } else {
//                    isMapVisible = true;
//                    mRestaurantsRecyclerView.setVisibility(View.GONE);
//                    mMapView.setVisibility(View.VISIBLE);
//                    mRestaurantsMapRecyclerView.setVisibility(View.VISIBLE);
//                    mMapButton.setText(getResources().getString(R.string.btn_search_fragment_list_text));
//
//                    for (int i = 0; i < mRestaurantsList.size(); i ++) {
//                        if (i == 0) {
//                            mMarker = mGoogleMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng((mRestaurantsList.get(i).getLatitude()), (mRestaurantsList.get(i).getLongitude())))
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_black)));
//
//                            CameraPosition target = CameraPosition.builder()
//                                    .target(new LatLng((mRestaurantsList.get(i).getLatitude()), (mRestaurantsList.get(i).getLongitude())))
//                                    .zoom(9)
//                                    .build();
//                            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
//                        }
//
//                        else {
//                            mMarker = mGoogleMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng((mRestaurantsList.get(i).getLatitude()), (mRestaurantsList.get(i).getLongitude())))
//                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pink)));
//
//                            CameraPosition target = CameraPosition.builder()
//                                    .target(new LatLng((mRestaurantsList.get(i).getLatitude()), (mRestaurantsList.get(i).getLongitude())))
//                                    .zoom(9)
//                                    .build();
//                            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
//                        }
//                    }
//
//                    if (isMapReady) {
//                        double latitude = (mRestaurantsList.get(0).getLatitude());
//                        double longitude = (mRestaurantsList.get(0).getLongitude());
//                        LatLng latLng = new LatLng(latitude, longitude);
//
//                        mMarker = mGoogleMap.addMarker(new MarkerOptions()
//                                .position(latLng)
//                                .visible(true)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_black)));
//                        CameraPosition target = CameraPosition.builder().target(latLng).zoom(15).build();
//                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target));
//                    }
//                }
//            }
//        });
//    }

    private void filterButtonClick() {
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableRestaurantsActivity.this, FilterActivity.class);
                intent.putExtra(HomeFragment.TABLE_OBJECT_KEY, mTable);
                intent.setAction(ConstantsHelper.ACTION_TABLE_RESTAURANT_ACTIVITY);

                intent.putExtra(FilterActivity.CUISINE_ID_KEY, mCuisineId);
                intent.putExtra(FilterActivity.PRICE_KEY, mPrice);
                intent.putExtra(FilterActivity.SORT_BY_KEY, mSortBy);

                startActivity(intent);
            }
        });
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

//    private void getListRestaurants(int userId, int tableId, double latitude, double longitude, String searchKeyword, int cuisineId, int price, String sortBy) {
//        if (userId != -10 && tableId != -10) {
//            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
//            Call<ArrayList<Restaurant>> restaurantsListCall = webService.getRestaurants(userId, tableId , latitude, longitude, searchKeyword, cuisineId, price, sortBy);
//            restaurantsListCall.enqueue(new Callback<ArrayList<Restaurant>>() {
//                @Override
//                public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
//                    hideLoadingIndicator();
//                    if (response.isSuccessful()) {
//                        mRestaurantsList = response.body();
//                        if (mRestaurantsList != null && mRestaurantsList.size() > 0) {
//                            mSearchRestaurantAdapter.setRestaurantList(mRestaurantsList);
//                            mRestaurantsRecyclerView.setAdapter(mSearchRestaurantAdapter);
//
//                            mRestaurantsMapAdapter.setRestaurantList(mRestaurantsList);
//                            mRestaurantsMapRecyclerView.setAdapter(mRestaurantsMapAdapter);
//
//                            mTable.setRestaurants(mRestaurantsList);
//                        }
//
//                        else {
//                            Toast.makeText(TableRestaurantsActivity.this, getResources().getString(R.string.toast_message_restaurants_list_is_empty), Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    else {
//                        Toast.makeText(TableRestaurantsActivity.this, getResources().getString(R.string.toast_message_error_getting_your_data), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
//                    hideLoadingIndicator();
//                    Toast.makeText(TableRestaurantsActivity.this, getResources().getString(R.string.toast_message_error_getting_your_data), Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }

//    private void markAsFavourite(int userId, int restaurantId, final int itemPosition) {
//        if (userId != -10 && restaurantId >= 0) {
//            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
//            Call<Object> resultCall = webService.markAsFavourite(userId, restaurantId);
//            resultCall.enqueue(new Callback<Object>() {
//                @Override
//                public void onResponse(Call<Object> call, Response<Object> response) {
//                    hideLoadingIndicator();
//                    if (response.isSuccessful()) {
//                        Log.d(TAG, String.valueOf(response.body()));
//                        if (mRestaurantsList.get(itemPosition).getFavourite().equals("false")) {
//                            mSearchRestaurantAdapter.addItemToFavoriteItemsList(itemPosition);
//                            mSearchRestaurantAdapter.removeItemFromUnFavoriteItemsList(itemPosition);
//                            mRestaurantsList.get(itemPosition).setFavourite("true");
//                            mSearchRestaurantAdapter.notifyDataSetChanged();
//                        } else {
//                            mSearchRestaurantAdapter.addItemToUnFavoriteItemsList(itemPosition);
//                            mSearchRestaurantAdapter.removeItemFromFavoriteItemsList(itemPosition);
//                            mRestaurantsList.get(itemPosition).setFavourite("false");
//                            mSearchRestaurantAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Object> call, Throwable t) {
//                    hideLoadingIndicator();
//                    Log.d(TAG, "Failed to get results");
//                    Log.d(TAG, t.getMessage());
//                }
//            });
//        }
//    }
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_restaurants);

        mUserId = SharedPreferencesHelper.getSharedPreferenceInt(this, ConstantsHelper.KEY_USER_ID, -10);
        mTableId = getIntent().getIntExtra(HomeFragment.TABLE_ID_KEY, -10);
        requestRestaurents(mTableId);

        mCuisineId = getIntent().getIntExtra(FilterActivity.CUISINE_ID_KEY, -10);
        mPrice = getIntent().getIntExtra(FilterActivity.PRICE_KEY, -10);
        mSortBy = getIntent().getStringExtra(FilterActivity.SORT_BY_KEY);
        //==========================================================================================
        findViewsById(); // (1)


        setUpRestaurantsRecyclerView(); // (2)

        setUpRestaurantsMapRecyclerView(); // (3)

        setUpMapView(savedInstanceState); // (4)
        //==========================================================================================
        arrowBackClick();

//        mapButtonClick();

        filterButtonClick();

        if (mTable != null) {
            mTableName = mTable.getListNameEn();
            if (!TextUtils.isEmpty(mTableName)) {
                mTableNameTextView.setText(mTableName + "(" + mTable.getRestaurants().size() + ")" ) ;
            }

            if (mCuisineId != -10 && mPrice != -10 && ! TextUtils.isEmpty(mSortBy)) {
                showLoadingIndicator();
//                getListRestaurants(mUserId, mTable.getListId(), 0, 0, "", mCuisineId, mPrice, mSortBy);
            }

            else {
//                mRestaurantsList = (ArrayList<Restaurant>) mTable.getRestaurants();
//                if (mRestaurantsList != null && mRestaurantsList.size() > 0) {
//                    mSearchRestaurantAdapter.setRestaurantList(mRestaurantsList);
//                    mRestaurantsRecyclerView.setAdapter(mSearchRestaurantAdapter);
//
//                    mRestaurantsMapAdapter.setRestaurantList(mRestaurantsList);
//                    mRestaurantsMapRecyclerView.setAdapter(mRestaurantsMapAdapter);
                }
            }
        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mGoogleMap = googleMap;
    }


    public void requestRestaurents(int categoryId){
        CategoryRestaurents webService = RetrofitWebService.retrofit.create(CategoryRestaurents.class);
        Call<ArrayList<Restaurant>> categoryRestaurents = webService.getCategoryRestaurents(categoryId);
        categoryRestaurents.enqueue(new Callback<ArrayList<Restaurant>>() {
            @Override
            public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                mSearchRestaurantAdapter.setRestaurantList(response.body());
                mRestaurantsRecyclerView.setAdapter(mSearchRestaurantAdapter);
                String categoryName = getIntent().getStringExtra(CATEGORY_NAME);
                mTableNameTextView.setText(categoryName + "(" + response.body().size() + ")" ) ;
            }

            @Override
            public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {

            }
        });

    }
}
