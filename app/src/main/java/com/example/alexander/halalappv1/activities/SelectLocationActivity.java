package com.example.alexander.halalappv1.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.City;
import com.example.alexander.halalappv1.adapters.SelectLocationAdapter;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLocationActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "XXXXX";
    public static final String KEY_LATITUDE = "location_latitude";
    public static final String KEY_LONGITUDE = "location_longitude";
    public static final String KEY_CITY_OBJECT = "city_object";
    public static final String KEY_SEARCH_BY = "search_by_key";
    public static final String VALUE_SEARCH_BY_CITY = "search_by_city_value";
    public static final String VALUE_SEARCH_BY_LOCATION = "search_by_location_value";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 34;
    //==============================================================================================
    private ImageView mArrowBackImageView;
    private ConstraintLayout mCurrentLocationLayout;
    private RecyclerView mLocationsRecyclerView;
    private ConstraintLayout mLoadingIndicator;

    private void findViewsById() {
        mArrowBackImageView = findViewById(R.id.select_location_activity_arrow_back);
        mCurrentLocationLayout = findViewById(R.id.select_location_activity_current_location_layout);
        mLocationsRecyclerView = findViewById(R.id.rv_select_location_activity_locations_list);
        mLoadingIndicator = findViewById(R.id.pb_select_location_activity_loading_indicator);
    }
    //==============================================================================================
    private SelectLocationAdapter mSelectLocationAdapter;
    private ArrayList<City> mCitiesList;

    private void setUpLocationsRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mLocationsRecyclerView.setLayoutManager(layoutManager);
        mSelectLocationAdapter = new SelectLocationAdapter(this, new SelectLocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                City city = mCitiesList.get(position);
                SharedPreferencesHelper.setSharedPreferenceString(SelectLocationActivity.this, ConstantsHelper.KEY_CITY_LATITUDE, city.getCityLatitude());
                SharedPreferencesHelper.setSharedPreferenceString(SelectLocationActivity.this, ConstantsHelper.KEY_CITY_LONGITUDE, city.getCityLongitude());

                if (mAction != null) {
                    Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
                    intent.putExtra(KEY_CITY_OBJECT, city);
                    intent.putExtra(KEY_SEARCH_BY, VALUE_SEARCH_BY_CITY + "-" + mAction);
                    intent.setAction(mAction);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SelectLocationActivity.this, SearchRestaurantActivity.class);
                    intent.putExtra(KEY_CITY_OBJECT, city);
                    startActivity(intent);
                }
            }
        });
    }
    //==============================================================================================
    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }
    //==============================================================================================
    private void getCities() {
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<ArrayList<City>> citiesListCall = webService.getCities();
        citiesListCall.enqueue(new Callback<ArrayList<City>>() {
            @Override
            public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    mCitiesList = response.body();
                    if (mCitiesList != null && mCitiesList.size() > 0) {
                        mSelectLocationAdapter.setCitiesList(mCitiesList);
                        mLocationsRecyclerView.setAdapter(mSelectLocationAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<City>> call, Throwable t) {
                hideLoadingIndicator();
                Log.d(TAG, "Failed to get results");
                Log.d(TAG, String.valueOf(t.getMessage()));
            }
        });
    }
    //==============================================================================================
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
    private Location mLastLocation;
    private double mLatitude;
    private double mLongitude;
    private boolean isGpsEnabled;

    private void checkForGpsAvailabilityAndUpdateView() {
        mGpsLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager != null) {
            isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        if (! checkPermissions()) {
            requestPermissions();
        }

        if (isGpsEnabled && checkPermissions()) {
            mCurrentLocationLayout.setVisibility(View.VISIBLE);
        } else {
            mCurrentLocationLayout.setVisibility(View.GONE);
        }
    }

    public void getCurrentLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (! checkPermissions()) {
            requestPermissions();
        } else {
            showLoadingIndicator();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLastLocation();
                }
            }, 4000);
            getLastLocation();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        startLocationPermissionRequest();
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(SelectLocationActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                if (isGpsEnabled) {
                    getLastLocation();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.toast_message_turn_on_gps_and_try_again), Toast.LENGTH_LONG).show();
                }
            } else {
                // Permission denied.
                mCurrentLocationLayout.setVisibility(View.GONE);
                if (mGpsLocationManager != null) {
                    mGpsLocationManager.removeUpdates(this);
                }
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        hideLoadingIndicator();
        mFusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLastLocation = task.getResult();
                    mLatitude = mLastLocation.getLatitude();
                    mLongitude = mLastLocation.getLongitude();

                    if (mAction != null) {
                        if (mAction.equals(ConstantsHelper.ACTION_HOME_FRAGMENT)) {
                            Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
                            intent.setAction(mAction);
                            intent.putExtra(KEY_LATITUDE, mLatitude);
                            intent.putExtra(KEY_LONGITUDE, mLongitude);
                            intent.putExtra(KEY_SEARCH_BY, VALUE_SEARCH_BY_LOCATION + "-" + mAction);
                            startActivity(intent);
                        }
                        else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                            Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
                            intent.setAction(mAction);
                            intent.putExtra(KEY_LATITUDE, mLatitude);
                            intent.putExtra(KEY_LONGITUDE, mLongitude);
                            intent.putExtra(KEY_SEARCH_BY, VALUE_SEARCH_BY_LOCATION + "-" + mAction);
                            startActivity(intent);
                        }
                    }
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.getException());
                }
            }
        });
    }
    //==============================================================================================
    private LocationManager mGpsLocationManager;

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onProviderDisabled(String provider) {
        mCurrentLocationLayout.setVisibility(View.GONE);
    }

    @Override
    public void onProviderEnabled(String provider) {
        mCurrentLocationLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                mCurrentLocationLayout.setVisibility(View.GONE);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                mCurrentLocationLayout.setVisibility(View.GONE);
                break;
            case LocationProvider.AVAILABLE:
                mCurrentLocationLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
    //==============================================================================================
    private String mAction;

    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void currentLocationLayoutClick() {
        mCurrentLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesHelper.removeFromSharedPreference(SelectLocationActivity.this, ConstantsHelper.KEY_SELECTED_CITY);
                SharedPreferencesHelper.removeFromSharedPreference(SelectLocationActivity.this, ConstantsHelper.KEY_CITY_LATITUDE);
                SharedPreferencesHelper.removeFromSharedPreference(SelectLocationActivity.this, ConstantsHelper.KEY_CITY_LONGITUDE);
                if (mAction != null) {
                    if (mAction.equals(ConstantsHelper.ACTION_HOME_FRAGMENT)) {
                        Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
                        intent.setAction(mAction);
                        intent.putExtra(KEY_SEARCH_BY, VALUE_SEARCH_BY_LOCATION + "-" + mAction);
                        startActivity(intent);
                    } else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                        Intent intent = new Intent(SelectLocationActivity.this, MainActivity.class);
                        intent.setAction(mAction);
                        intent.putExtra(KEY_SEARCH_BY, VALUE_SEARCH_BY_LOCATION + "-" + mAction);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(SelectLocationActivity.this, SearchRestaurantActivity.class);
                    intent.putExtra(KEY_SEARCH_BY, VALUE_SEARCH_BY_LOCATION);
                    startActivity(intent);
                }
            }
        });
    }
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        mAction = getIntent().getAction();

        findViewsById(); // (1)

        setUpLocationsRecyclerView(); // (2)

        showLoadingIndicator(); // (3)

        checkForGpsAvailabilityAndUpdateView(); // (4)

        getCities(); // (5)
        //==========================================================================================
        arrowBackClick();

        currentLocationLayoutClick();
    }

    @Override
    protected void onResume() {
        if (checkPermissions()) {
            if (mGpsLocationManager != null) {
                mGpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000000, 1000000f, this);
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mGpsLocationManager != null) {
            mGpsLocationManager.removeUpdates(this);
        }
        super.onPause();
    }
}
