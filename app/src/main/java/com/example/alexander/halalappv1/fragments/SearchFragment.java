package com.example.alexander.halalappv1.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.FavouriteRestaurantsActivity;
import com.example.alexander.halalappv1.activities.FilterActivity;
import com.example.alexander.halalappv1.activities.RestaurantProfileActivity;
import com.example.alexander.halalappv1.activities.SearchRestaurantActivity;
import com.example.alexander.halalappv1.activities.SelectLocationActivity;
import com.example.alexander.halalappv1.adapters.RestaurantsMapAdapter;
import com.example.alexander.halalappv1.adapters.SearchRestaurantAdapter;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class SearchFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private static final String TAG = "XXXXX";
    public static final String RESTAURANT_OBJECT_KEY = "RestaurantObject";
    public static final String RESTAURANT_ID_KEY = "RestaurantId";

    public SearchFragment() {}
    //==============================================================================================
    private RecyclerView mRestaurantsMapRecyclerView;
    private RestaurantsMapAdapter mRestaurantsMapAdapter;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Marker mMarker;
    private boolean isMapReady = false;
    private boolean isMapVisible = false;
    private ArrayList<Integer> mIndexList = new ArrayList<>();

    private void setUpRestaurantsMapRecyclerView(View rootView) {
        mRestaurantsMapRecyclerView = rootView.findViewById(R.id.rv_search_fragment_restaurants_map_list);
        final LinearLayoutManager restaurantMaplayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRestaurantsMapRecyclerView.setLayoutManager(restaurantMaplayoutManager);
        mRestaurantsMapAdapter = new RestaurantsMapAdapter(getContext(), new RestaurantsMapAdapter.OnRestaurantMapListClickListener() {
            @Override
            public void onMapListItemClick(int position) {
                Restaurant restaurant = mRestaurantList.get(position);
                Intent intent = new Intent(getActivity(), RestaurantProfileActivity.class);
                intent.putExtra(RESTAURANT_OBJECT_KEY, restaurant);
                intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                startActivity(intent);
            }
        });
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRestaurantsMapRecyclerView);

        mRestaurantsMapRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (isMapReady) {
                    if (newState == SCROLL_STATE_IDLE) {
                        int scrollPosition = restaurantMaplayoutManager.findFirstCompletelyVisibleItemPosition();
                        mIndexList.add(scrollPosition);
                        if (scrollPosition != -1) {
                            if (scrollPosition == mIndexList.get(mIndexList.size() - 1)) {
                                double latitude = (mRestaurantList.get(scrollPosition).getLatitude());
                                double longitude = (mRestaurantList.get(scrollPosition).getLongitude());
                                LatLng latLng = new LatLng(latitude, longitude);

                                mMarker = mGoogleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .visible(true)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_black)));
                                CameraPosition target = CameraPosition.builder().target(latLng).zoom(10).build();
                                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target));
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mMarker.remove();
                if (dx > 0) {
                    mMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng((mRestaurantList.get(0).getLatitude()), (mRestaurantList.get(0).getLongitude())))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pink)));
                }
                if (recyclerView.canScrollHorizontally(-1)) {
                    mMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng((mRestaurantList.get(mRestaurantList.size() - 1).getLatitude()), (mRestaurantList.get(mRestaurantList.size() - 1).getLongitude())))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pink)));
                }
            }
        });
    }

    private void setUpMapView(View rootView, Bundle savedInstanceState) {
        mMapView = rootView.findViewById(R.id.map);
        mMapView.setVisibility(View.GONE);
        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //==============================================================================================
    private ConstraintLayout mHeaderLayout;
    private TextView mSearchTextView;
    private ConstraintLayout mSearchLayout;
    private Button mCurrentLocationButton;
    private RecyclerView mRestaurantRecyclerView;
    private ConstraintLayout mMapFilterLayout;
    private Button mMapButton;
    private Button mFilterButton;
    private ConstraintLayout mLoadingIndicator;
    private ConstraintLayout mPermissionNotGrantedLayout;
    private ConstraintLayout mGpsDisabledLayout;
    private ConstraintLayout mDataNotFoundLayout;
    private ConstraintLayout mLocationNotDetectedLayout;

    private TextView mErrorMessageTextView;
    private TextView mActionGrantPermissionTextView;
    private TextView mActionRetryTextView;

    private void findViewsById(View rootView) {
        mHeaderLayout = rootView.findViewById(R.id.search_fragment_header_layout);
        mSearchTextView = rootView.findViewById(R.id.tv_fragment_search_search);
        mSearchLayout = rootView.findViewById(R.id.search_fragment_search_layout);
        mCurrentLocationButton = rootView.findViewById(R.id.btn_fragment_search_current_location);
        mRestaurantRecyclerView = rootView.findViewById(R.id.rv_search_fragment_restaurants_list);
        mMapFilterLayout = rootView.findViewById(R.id.search_fragment_map_filter_layout);
        mMapButton = rootView.findViewById(R.id.btn_search_fragment_map);
        mFilterButton = rootView.findViewById(R.id.btn_search_fragment_filter);
        mLoadingIndicator = rootView.findViewById(R.id.pb_search_fragment_loading_indicator);

        mPermissionNotGrantedLayout = rootView.findViewById(R.id.search_fragment_permission_not_granted_layout);
        mGpsDisabledLayout = rootView.findViewById(R.id.search_fragment_gps_disabled_layout);
        mDataNotFoundLayout = rootView.findViewById(R.id.search_fragment_data_not_found_layout);
        mLocationNotDetectedLayout = rootView.findViewById(R.id.search_fragment_location_not_detected_layout);

        mActionGrantPermissionTextView = rootView.findViewById(R.id.tv_permission_not_granted_action_grant_permission);
        mActionRetryTextView = rootView.findViewById(R.id.tv_location_not_detected_action_retry);
        mErrorMessageTextView = rootView.findViewById(R.id.tv_search_fragment_error_message);
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void showData() {
        mRestaurantRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideData() {
        mRestaurantRecyclerView.setVisibility(View.GONE);
    }

    private void showPermissionNotGrantedError() {
        mPermissionNotGrantedLayout.setVisibility(View.VISIBLE);
    }

    private void hidePermissionNotGrantedError() {
        mPermissionNotGrantedLayout.setVisibility(View.GONE);
    }

    private void showGpsDisabledError() {
        mGpsDisabledLayout.setVisibility(View.VISIBLE);
    }

    private void hideGpsDisabledError() {
        mGpsDisabledLayout.setVisibility(View.GONE);
    }

    private void showDataNotFoundError() {
        mDataNotFoundLayout.setVisibility(View.VISIBLE);
    }

    private void hideDataNotFoundError() {
        mDataNotFoundLayout.setVisibility(View.GONE);
    }

    private void showLocationNotDetectedError() {
        mLocationNotDetectedLayout.setVisibility(View.VISIBLE);
    }

    private void hideLocationNotDetectedError() {
        mLocationNotDetectedLayout.setVisibility(View.GONE);
    }

    private void showMapFilterLayout() {
        mMapFilterLayout.setVisibility(View.VISIBLE);
    }

    private void hideMapFilterLayout() {
        mMapFilterLayout.setVisibility(View.GONE);
    }
    //==============================================================================================
    private SearchRestaurantAdapter mSearchRestaurantAdapter;
    private List<Restaurant> mRestaurantList;

    private void setUpRestaurantRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRestaurantRecyclerView.setLayoutManager(layoutManager);
        mSearchRestaurantAdapter = new SearchRestaurantAdapter(getContext(), new SearchRestaurantAdapter.OnRestaurantListClickListener() {
            @Override
            public void onFavoriteButtonClick(int itemPosition) {
                isNetworkOk = NetworkHelper.hasNetworkAccess(getContext());
                if (isNetworkOk) {
                    if (isLoggedIn) {
                        showLoadingIndicator();
                        markAsFavourite(mUserId, mRestaurantList.get(itemPosition).getId(), itemPosition);
                    } else {
                        Intent intent = new Intent(getContext(), FavouriteRestaurantsActivity.class);
                        intent.putExtra(RESTAURANT_ID_KEY, mRestaurantList.get(itemPosition).getId());
                        intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onListItemClick(int itemPosition) {
                Restaurant restaurant = mRestaurantList.get(itemPosition);
                Intent intent = new Intent(getActivity(), RestaurantProfileActivity.class);
                intent.putExtra(RESTAURANT_OBJECT_KEY, restaurant);
                intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                startActivity(intent);
            }
        });
    }
    //==============================================================================================
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager mLocationManager;
    private Location mLastLocation;
    private double mLatitude;
    private double mLongitude;
    private boolean isGpsEnabled;

    private void askForLocationPermissionAndGetData() {
        mGpsLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager != null) {
            isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        if (! checkPermissions()) {
            hideLoadingIndicator();
            hideData();
            hideMapFilterLayout();
            hideGpsDisabledError();
            hideDataNotFoundError();
            hideLocationNotDetectedError();
            showPermissionNotGrantedError();
            requestPermissions();
        } else {
            if (isGpsEnabled) {
                hidePermissionNotGrantedError();
                hideGpsDisabledError();
                hideDataNotFoundError();
                hideLocationNotDetectedError();
                showData();
                getLastLocation();
            } else {
                hideLoadingIndicator();
                hideData();
                hideMapFilterLayout();
                hidePermissionNotGrantedError();
                hideDataNotFoundError();
                hideLocationNotDetectedError();
                showGpsDisabledError();
            }
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        startLocationPermissionRequest();
    }

    private void startLocationPermissionRequest() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                hidePermissionNotGrantedError();
                if (isGpsEnabled) {
                    hideGpsDisabledError();
                    getLastLocation();
                } else {
                    hideLoadingIndicator();
                    hideData();
                    hideMapFilterLayout();
                    hideDataNotFoundError();
                    hideLocationNotDetectedError();
                    showGpsDisabledError();
                }
            } else {
                // Permission denied.
                hideLoadingIndicator();
                hideData();
                hideMapFilterLayout();
                hideGpsDisabledError();
                hideDataNotFoundError();
                hideLocationNotDetectedError();
                showPermissionNotGrantedError();
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLastLocation = task.getResult();
                    mLatitude = mLastLocation.getLatitude();
                    mLongitude = mLastLocation.getLongitude();

                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideDataNotFoundError();
                    hideLocationNotDetectedError();
                    showLoadingIndicator();
                    showData();
                    getRestaurants(mLatitude, mLongitude);
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.getException());
                    hideLoadingIndicator();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideDataNotFoundError();
                    showLocationNotDetectedError();
                }
            }
        });
    }

    private void getRestaurants(final double latitude, final double longitude) {
        int userId = SharedPreferencesHelper.getSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, -10);
        if (userId == -10) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<ArrayList<Restaurant>> restaurantCall = webService.getRestaurants(0, 0, latitude, longitude, "", 0, 0, "");
            restaurantCall.enqueue(new Callback<ArrayList<Restaurant>>() {
                @Override
                public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                    Log.d(TAG + "_onResponse", String.valueOf(response));
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        mRestaurantList = response.body();
                        if (mRestaurantList != null && mRestaurantList.size() > 0) {
//                            mSearchRestaurantAdapter.setRestaurantList(mRestaurantList);
//                            mRestaurantRecyclerView.setAdapter(mSearchRestaurantAdapter);

                            mRestaurantsMapAdapter.setRestaurantList(mRestaurantList);
                            mRestaurantsMapRecyclerView.setAdapter(mRestaurantsMapAdapter);

                            mMapButton.setEnabled(true);
                            mFilterButton.setEnabled(true);
                        } else {
                            Log.d(TAG, "mRestaurantList is null or empty");
                            hideData();
                            hideMapFilterLayout();
                            hidePermissionNotGrantedError();
                            hideGpsDisabledError();
                            hideLocationNotDetectedError();
                            showDataNotFoundError();
                        }
                    } else {
                        Log.d(TAG, "response.isSuccessful() == false");
                        hideData();
                        hideMapFilterLayout();
                        hidePermissionNotGrantedError();
                        hideGpsDisabledError();
                        hideLocationNotDetectedError();
                        showDataNotFoundError();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                    Log.d(TAG + "_onFailure", String.valueOf(t.getMessage()));
                    hideLoadingIndicator();
                    hideData();
                    hideMapFilterLayout();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideLocationNotDetectedError();
                    showDataNotFoundError();
                }
            });
        } else {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<ArrayList<Restaurant>> restaurantCall = webService.getRestaurants(userId, 0, latitude, longitude, "", 0, 0, "");
            restaurantCall.enqueue(new Callback<ArrayList<Restaurant>>() {
                @Override
                public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                    Log.d(TAG + "_onResponse", String.valueOf(response));
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        mRestaurantList = response.body();
                        if (mRestaurantList != null && mRestaurantList.size() > 0) {
//                            mSearchRestaurantAdapter.setRestaurantList(mRestaurantList);
//                            mRestaurantRecyclerView.setAdapter(mSearchRestaurantAdapter);

                            mRestaurantsMapAdapter.setRestaurantList(mRestaurantList);
                            mRestaurantsMapRecyclerView.setAdapter(mRestaurantsMapAdapter);

                            mMapButton.setEnabled(true);
                            mFilterButton.setEnabled(true);
                        } else {
                            hideData();
                            hideMapFilterLayout();
                            hidePermissionNotGrantedError();
                            hideGpsDisabledError();
                            hideLocationNotDetectedError();
                            showDataNotFoundError();
                        }
                    } else {
                        hideData();
                        hideMapFilterLayout();
                        hidePermissionNotGrantedError();
                        hideGpsDisabledError();
                        hideLocationNotDetectedError();
                        showDataNotFoundError();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                    Log.d(TAG + "_onFailure", String.valueOf(t.getMessage()));
                    hideLoadingIndicator();
                    hideData();
                    hideMapFilterLayout();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideLocationNotDetectedError();
                    showDataNotFoundError();
                }
            });
        }
    }

    private void getRestaurants(int userId, int tableId, double latitude, double longitude, String searchKeyword, int cuisineId, int price, String sortBy) {
        if (userId == -10 && tableId != -10) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<ArrayList<Restaurant>> restaurantsListCall = webService.getRestaurants(0, tableId , latitude, longitude, searchKeyword, cuisineId, price, sortBy);
            restaurantsListCall.enqueue(new Callback<ArrayList<Restaurant>>() {
                @Override
                public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                    Log.d(TAG + "_onResponse", String.valueOf(response));
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        mRestaurantList = response.body();
                        if (mRestaurantList != null && mRestaurantList.size() > 0) {
//                            mSearchRestaurantAdapter.setRestaurantList(mRestaurantList);
//                            mRestaurantRecyclerView.setAdapter(mSearchRestaurantAdapter);

                            mRestaurantsMapAdapter.setRestaurantList(mRestaurantList);
                            mRestaurantsMapRecyclerView.setAdapter(mRestaurantsMapAdapter);

                            mMapButton.setEnabled(true);
                            mFilterButton.setEnabled(true);
                        } else {
                            Log.d(TAG, "mRestaurantList is null or empty");
                            hideData();
                            hideMapFilterLayout();
                            hidePermissionNotGrantedError();
                            hideGpsDisabledError();
                            hideLocationNotDetectedError();
                            showDataNotFoundError();
                            mMapButton.setEnabled(false);
                            mFilterButton.setEnabled(true);
                        }
                    } else {
                        Log.d(TAG, "response.isSuccessful() == false");
                        hideData();
                        hideMapFilterLayout();
                        hidePermissionNotGrantedError();
                        hideGpsDisabledError();
                        hideLocationNotDetectedError();
                        showDataNotFoundError();
                        mMapButton.setEnabled(false);
                        mFilterButton.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                    Log.d(TAG + "_onFailure", String.valueOf(t.getMessage()));
                    hideLoadingIndicator();
                    hideData();
                    hideMapFilterLayout();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideLocationNotDetectedError();
                    showDataNotFoundError();
                    mMapButton.setEnabled(false);
                    mFilterButton.setEnabled(true);
                }
            });
        } else if (userId != -10 && tableId != -10) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<ArrayList<Restaurant>> restaurantsListCall = webService.getRestaurants(userId, tableId , latitude, longitude, searchKeyword, cuisineId, price, sortBy);
            restaurantsListCall.enqueue(new Callback<ArrayList<Restaurant>>() {
                @Override
                public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                    Log.d(TAG + "_onResponse", String.valueOf(response));
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        mRestaurantList = response.body();
                        if (mRestaurantList != null && mRestaurantList.size() > 0) {
//                            mSearchRestaurantAdapter.setRestaurantList(mRestaurantList);
//                            mRestaurantRecyclerView.setAdapter(mSearchRestaurantAdapter);

                            mRestaurantsMapAdapter.setRestaurantList(mRestaurantList);
                            mRestaurantsMapRecyclerView.setAdapter(mRestaurantsMapAdapter);

                            mMapButton.setEnabled(true);
                            mFilterButton.setEnabled(true);
                        } else {
                            hideData();
                            hideMapFilterLayout();
                            hidePermissionNotGrantedError();
                            hideGpsDisabledError();
                            hideLocationNotDetectedError();
                            showDataNotFoundError();
                            mMapButton.setEnabled(false);
                            mFilterButton.setEnabled(true);
                        }
                    } else {
                        hideData();
                        hideMapFilterLayout();
                        hidePermissionNotGrantedError();
                        hideGpsDisabledError();
                        hideLocationNotDetectedError();
                        showDataNotFoundError();
                        mMapButton.setEnabled(false);
                        mFilterButton.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                    Log.d(TAG + "_onFailure", String.valueOf(t.getMessage()));
                    hideLoadingIndicator();
                    hideData();
                    hideMapFilterLayout();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideLocationNotDetectedError();
                    showDataNotFoundError();
                    mMapButton.setEnabled(false);
                    mFilterButton.setEnabled(true);
                }
            });
        }
    }

    private void markAsFavourite(int userId, int restaurantId, final int itemPosition) {
        if (userId != -10) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<Object> resultCall = webService.markAsFavourite(userId, restaurantId);
            resultCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        Log.d(TAG, String.valueOf(response.body()));
                        if (mRestaurantList.get(itemPosition).getFavourite().equals("false")) {
                            mSearchRestaurantAdapter.addItemToFavoriteItemsList(itemPosition);
                            mSearchRestaurantAdapter.removeItemFromUnFavoriteItemsList(itemPosition);
                            mRestaurantList.get(itemPosition).setFavourite("true");
                            mSearchRestaurantAdapter.notifyDataSetChanged();
                        } else {
                            mSearchRestaurantAdapter.addItemToUnFavoriteItemsList(itemPosition);
                            mSearchRestaurantAdapter.removeItemFromFavoriteItemsList(itemPosition);
                            mRestaurantList.get(itemPosition).setFavourite("false");
                            mSearchRestaurantAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    hideLoadingIndicator();
                    Log.d(TAG, "Failed to get results");
                    Log.d(TAG, t.getMessage());
                }
            });
        }
    }
    //==============================================================================================
    private LocationManager mGpsLocationManager;

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onProviderDisabled(String provider) {
        hideData();
        hideMapFilterLayout();
        hidePermissionNotGrantedError();
        hideDataNotFoundError();
        hideLocationNotDetectedError();
        showGpsDisabledError();
    }

    @Override
    public void onProviderEnabled(String provider) {
        hidePermissionNotGrantedError();
        hideGpsDisabledError();
        hideDataNotFoundError();
        hideLocationNotDetectedError();
        showData();
        showMapFilterLayout();
        showLoadingIndicator();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLastLocation();
            }
        }, 4000);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                hideData();
                hideMapFilterLayout();
                hidePermissionNotGrantedError();
                hideDataNotFoundError();
                hideLocationNotDetectedError();
                showGpsDisabledError();
                break;

            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                hideData();
                hideMapFilterLayout();
                hidePermissionNotGrantedError();
                hideDataNotFoundError();
                hideLocationNotDetectedError();
                showGpsDisabledError();
                break;

            case LocationProvider.AVAILABLE:
                hidePermissionNotGrantedError();
                hideGpsDisabledError();
                hideDataNotFoundError();
                hideLocationNotDetectedError();
                showData();
                showMapFilterLayout();
                showLoadingIndicator();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLastLocation();
                    }
                }, 3000);
                break;
        }
    }
    //==============================================================================================
    private int mUserId;
    private int mTableId = 0;
    private String mAction;
    private String mSearchAction;
    private String mFilterAction;

    private String mCityName;
    private double mCityLatitude;
    private double mCityLongitude;
    private String mSearchKeywords;
    private int mCuisineId;
    private int mPrice;
    private String mSortBy;

    private boolean isNetworkOk;
    private boolean isLoggedIn;
    //==============================================================================================
    private void searchLayoutClick() {
        mSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchRestaurantActivity.class);
                intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                intent.putExtra(FilterActivity.CUISINE_ID_KEY, mCuisineId);
                intent.putExtra(FilterActivity.PRICE_KEY, mPrice);
                intent.putExtra(FilterActivity.SORT_BY_KEY, mSortBy);
                intent.putExtra("XXXXX", mSearchAction);
                startActivity(intent);
            }
        });
    }

    private void currentLocationButtonClick() {
        mCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectLocationActivity.class);
                intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    private void mapButtonClick() {
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMapVisible) {
                    isMapVisible = false;
                    mHeaderLayout.setVisibility(View.VISIBLE);
                    mRestaurantRecyclerView.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                    mRestaurantsMapRecyclerView.setVisibility(View.GONE);
                    mMapButton.setText(getResources().getString(R.string.btn_search_fragment_map_text));
                } else {
                    isMapVisible = true;
                    mHeaderLayout.setVisibility(View.GONE);
                    mRestaurantRecyclerView.setVisibility(View.GONE);
                    mMapView.setVisibility(View.VISIBLE);
                    mRestaurantsMapRecyclerView.setVisibility(View.VISIBLE);
                    mMapButton.setText(getResources().getString(R.string.btn_search_fragment_list_text));

                    for (int i = 0; i < mRestaurantList.size(); i ++) {
                        if (i == 0) {
                            mMarker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng((mRestaurantList.get(i).getLatitude()), (mRestaurantList.get(i).getLongitude())))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_black)));

                            CameraPosition target = CameraPosition.builder()
                                    .target(new LatLng((mRestaurantList.get(i).getLatitude()), (mRestaurantList.get(i).getLongitude())))
                                    .zoom(9)
                                    .build();
                            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
                        }

                        else {
                            mMarker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng((mRestaurantList.get(i).getLatitude()), (mRestaurantList.get(i).getLongitude())))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_pink)));

                            CameraPosition target = CameraPosition.builder()
                                    .target(new LatLng((mRestaurantList.get(i).getLatitude()), (mRestaurantList.get(i).getLongitude())))
                                    .zoom(9)
                                    .build();
                            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
                        }
                    }

                    if (isMapReady) {
                        double latitude = (mRestaurantList.get(0).getLatitude());
                        double longitude = (mRestaurantList.get(0).getLongitude());
                        LatLng latLng = new LatLng(latitude, longitude);

                        mMarker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .visible(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_black)));
                        CameraPosition target = CameraPosition.builder().target(latLng).zoom(10).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(target));
                    }
                }
            }
        });
    }

    private void filterButtonClick() {
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putExtra(SearchRestaurantActivity.CITY_LATITUDE_KEY, mCityLatitude);
                intent.putExtra(SearchRestaurantActivity.CITY_LONGITUDE_KEY, mCityLongitude);
                intent.putExtra(SearchRestaurantActivity.SEARCH_KEYWORDS_KEY, mSearchKeywords);

                intent.putExtra(FilterActivity.CUISINE_ID_KEY, mCuisineId);
                intent.putExtra(FilterActivity.PRICE_KEY, mPrice);
                intent.putExtra(FilterActivity.SORT_BY_KEY, mSortBy);

                intent.setAction(null);
                startActivity(intent);
            }
        });
    }

    private void actionGrantPermissionClick() {
        mActionGrantPermissionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });
    }

    private void actionRetryClick() {
        mActionRetryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLocationNotDetectedError();
                showLoadingIndicator();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLastLocation();
                    }
                }, 4000);
            }
        });
    }
    //==============================================================================================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        findViewsById(rootView); // (1)

        setUpRestaurantRecyclerView(); // (2)
        //==========================================================================================
        setUpRestaurantsMapRecyclerView(rootView); // (3)

        setUpMapView(rootView, savedInstanceState); // (4)
        //==========================================================================================
        mUserId = SharedPreferencesHelper.getSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, -10);
        mCityName = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_SELECTED_CITY, null);
        mCityLatitude = Double.parseDouble(String.valueOf(SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_CITY_LATITUDE, "-10")));
        mCityLongitude = Double.parseDouble(String.valueOf(SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_CITY_LONGITUDE, "-10")));

        mAction = getActivity().getIntent().getStringExtra(SelectLocationActivity.KEY_SEARCH_BY);
        mSearchAction = getActivity().getIntent().getStringExtra(SearchRestaurantActivity.ACTION_SEARCH_KEY);
        mFilterAction = getActivity().getIntent().getStringExtra(FilterActivity.ACTION_FILTER_KEY);

        mMapButton.setEnabled(false);
        mFilterButton.setEnabled(false);
        //==========================================================================================
        isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, false);
        isNetworkOk = NetworkHelper.hasNetworkAccess(getContext());
        if (isNetworkOk) {
            if (mCityName != null) {
                mCurrentLocationButton.setText(mCityName);
            }

            if (getActivity().getIntent().getStringExtra("BBBBB") != null) {
                mSearchTextView.setText(getActivity().getIntent().getStringExtra("BBBBB"));
            }

            if (mFilterAction == null && mSearchAction == null) {
                if (mAction == null) {
                    if (mCityLatitude != -10 && mCityLongitude != -10) {
                        showLoadingIndicator();
                        getRestaurants(mUserId, mTableId, mCityLatitude, mCityLongitude, mSearchKeywords, mCuisineId, mPrice, mSortBy);
                        mCurrentLocationButton.setText(mCityName);

                    } else {
                        askForLocationPermissionAndGetData();
                        mCurrentLocationButton.setText(getResources().getString(R.string.btn_home_current_location_text));
                    }
                } else {
                    if (mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_CITY + "-" + ConstantsHelper.ACTION_SEARCH_FRAGMENT) ||
                            mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_CITY + "-" + ConstantsHelper.ACTION_HOME_FRAGMENT)) {
                        mCurrentLocationButton.setText(mCityName);
                        showLoadingIndicator();
                        getRestaurants(mUserId, mTableId, mCityLatitude, mCityLongitude, mSearchKeywords, mCuisineId, mPrice, mSortBy);
                    } else if (mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_LOCATION + "-" + ConstantsHelper.ACTION_SEARCH_FRAGMENT) ||
                            mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_LOCATION + "-" + ConstantsHelper.ACTION_HOME_FRAGMENT)) {
                        askForLocationPermissionAndGetData();
                        mCurrentLocationButton.setText(getResources().getString(R.string.btn_home_current_location_text));
                    }
                }
            }
            //==========================================================================================
            if (mFilterAction != null) {
                mSearchKeywords = getActivity().getIntent().getStringExtra(SearchRestaurantActivity.SEARCH_KEYWORDS_KEY);
                mCuisineId = getActivity().getIntent().getIntExtra(FilterActivity.CUISINE_ID_KEY, -10);
                mPrice = getActivity().getIntent().getIntExtra(FilterActivity.PRICE_KEY, -10);
                mSortBy = getActivity().getIntent().getStringExtra(FilterActivity.SORT_BY_KEY);

                if (mFilterAction.equals(FilterActivity.ACTION_FILTER_VALUE)) {
                    if (mCityLatitude == -10) {
                        mCityLatitude = 0;
                    }
                    if (mCityLongitude == -10) {
                        mCityLongitude = 0;
                    }
                    if (mCuisineId == -10) {
                        mCuisineId = 0;
                    }
                    if (mPrice == -10) {
                        mPrice = 0;
                    }

                    showLoadingIndicator();
                    getRestaurants(mUserId, mTableId, mCityLatitude, mCityLongitude, mSearchKeywords, mCuisineId, mPrice, mSortBy);
                }
            } else if (mSearchAction != null) {
                mSearchKeywords = getActivity().getIntent().getStringExtra(SearchRestaurantActivity.SEARCH_KEYWORDS_KEY);
                mCuisineId = getActivity().getIntent().getIntExtra(FilterActivity.CUISINE_ID_KEY, -10);
                mPrice = getActivity().getIntent().getIntExtra(FilterActivity.PRICE_KEY, -10);
                mSortBy = getActivity().getIntent().getStringExtra(FilterActivity.SORT_BY_KEY);

                if (mSearchAction.equals(SearchRestaurantActivity.ACTION_SEARCH_VALUE)) {
                    if (mCityLatitude == -10) {
                        mCityLatitude = 0;
                    }
                    if (mCityLongitude == -10) {
                        mCityLongitude = 0;
                    }
                    if (mCuisineId == -10) {
                        mCuisineId = 0;
                    }
                    if (mPrice == -10) {
                        mPrice = 0;
                    }

                    showLoadingIndicator();
                    getRestaurants(mUserId, mTableId, mCityLatitude, mCityLongitude, mSearchKeywords, mCuisineId, mPrice, mSortBy);
                } else if (mSearchAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_LOCATION)) {
                    askForLocationPermissionAndGetData();
                    mCurrentLocationButton.setText(getResources().getString(R.string.btn_home_current_location_text));
                }
            }
        } else {
            hideData();
            hideMapFilterLayout();
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mErrorMessageTextView.setText(getResources().getString(R.string.toast_message_no_internet_connection));

            mSearchLayout.setEnabled(false);
            mCurrentLocationButton.setEnabled(false);
        }
        //==========================================================================================
        searchLayoutClick();

        currentLocationButtonClick();

        mapButtonClick();

        filterButtonClick();

        actionGrantPermissionClick();

        actionRetryClick();
        //==========================================================================================
        return rootView;
    }
    //==============================================================================================
    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mGoogleMap = googleMap;
    }

    @Override
    public void onResume() {
        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (mGpsLocationManager != null) {
                mGpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000000, 1000000f, this);
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mGpsLocationManager != null) {
            mGpsLocationManager.removeUpdates(this);
        }
        super.onPause();
    }

    //==============================================================================================
    private double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double theta = longitude1 - longitude2;
        double distance = Math.sin(deg2rad(latitude1))
                * Math.sin(deg2rad(latitude2))
                + Math.cos(deg2rad(latitude1))
                * Math.cos(deg2rad(latitude2))
                * Math.cos(deg2rad(theta));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515;
        return (distance);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
