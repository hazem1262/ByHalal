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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.RestaurantProfileActivity;
import com.example.alexander.halalappv1.activities.SearchRestaurantActivity;
import com.example.alexander.halalappv1.activities.SelectLocationActivity;
import com.example.alexander.halalappv1.activities.TableRestaurantsActivity;
import com.example.alexander.halalappv1.adapters.CategoriesAdapter;
import com.example.alexander.halalappv1.model.City;
import com.example.alexander.halalappv1.adapters.RestaurantsListAdapter;
import com.example.alexander.halalappv1.model.modifiedmodels.RestaurantsList1;
import com.example.alexander.halalappv1.model.modifiedmodels.homefragment.ListHeader;
import com.example.alexander.halalappv1.model.newModels.CategoriesWithRestaurant;
import com.example.alexander.halalappv1.model.newModels.Category;
import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.model.newModels.RestaurantOfTheWeek;
import com.example.alexander.halalappv1.model.newModels.RestaurantsList;
import com.example.alexander.halalappv1.services.MainPageWebService;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.utils.ConstantsHelper.ACTION_HOME_CATEGORIES;

public class HomeFragment extends Fragment implements LocationListener {

    public HomeFragment() {}

    private static final String TAG = "xxxxx";
    public static final String TABLE_ID_KEY = "RestaurantsListObject";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String RESTAURENT_KEY = "RestaurentKey";
    public static final String TABLE_OBJECT_KEY = "TableObject";
    public static final String RESTAURANT_OBJECT_KEY = "RestaurantObject";
    //==============================================================================================
    private ConstraintLayout mContainerLayout;
    private LinearLayout ourSelectionLinearLayout;
    private ImageView searchViewImage;
    private ConstraintLayout mSearchLayout;
    private TextView mSearchTextView;
    private Button mCurrentLocationButton;
    private RecyclerView mTablesRecyclerView;
    private ConstraintLayout mLoadingIndicator;
    private ConstraintLayout mPermissionNotGrantedLayout;
    private ConstraintLayout mGpsDisabledLayout;
    private ConstraintLayout mDataNotFoundLayout;
    private ConstraintLayout mLocationNotDetectedLayout;
    private LinearLayout discount;

    private LinearLayout bonjourLayout;
    private TextView mActionGrantPermissionTextView;
    private TextView mActionRetryTextView;
    private TextView mErrorMessageTextView;
    private TextView mGoodMorningTextView;
    private TextView seeAllCategories;

    //list header
    private TextView listHeaderName;
    private TextView listHeaderRestaurentName;
    private TextView listHeaderRestaurentType;
    private TextView listHeaderRestaurentDiscount;
    private ImageView listHeaderRestaurentImage;
	private LinearLayout listHeaderLayout;
    private RecyclerView categoriesRecyclerView, searchRecyclerView;
    private CategoriesAdapter categoriesAdapter;
    private ArrayList<Category> categoriesList;




    private void findViewsById(View rootView) {
//        ourSelectionLinearLayout = rootView.findViewById(R.id.ourSelectionLinearLayout);
        searchViewImage = rootView.findViewById(R.id.iv_home_fragment_search_icon);
        mCurrentLocationButton = rootView.findViewById(R.id.btn_home_fragment_current_location);
        mTablesRecyclerView = rootView.findViewById(R.id.rv_home_fragment_tables_list);
        mTablesRecyclerView.setHasFixedSize(true);
        mTablesRecyclerView.setItemViewCacheSize(5);
        mTablesRecyclerView.setDrawingCacheEnabled(true);
        mTablesRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(3);
        mTablesRecyclerView.setLayoutManager(layoutManager);
        bonjourLayout = rootView.findViewById(R.id.bonjourLayout);

        mLoadingIndicator = rootView.findViewById(R.id.pb_home_fragment_loading_indicator);
        mPermissionNotGrantedLayout = rootView.findViewById(R.id.home_fragment_permission_not_granted_layout);
        mGpsDisabledLayout = rootView.findViewById(R.id.home_fragment_gps_disabled_layout);
        mDataNotFoundLayout = rootView.findViewById(R.id.home_fragment_data_not_found_layout);
        mLocationNotDetectedLayout = rootView.findViewById(R.id.home_fragment_location_not_detected_layout);

        mActionGrantPermissionTextView = rootView.findViewById(R.id.tv_permission_not_granted_action_grant_permission);
        mActionRetryTextView = rootView.findViewById(R.id.tv_location_not_detected_action_retry);
        mErrorMessageTextView = rootView.findViewById(R.id.tv_home_fragment_error_message);
        mGoodMorningTextView = rootView.findViewById(R.id.tv_good_morning_label_text);
        seeAllCategories = rootView.findViewById(R.id.see_all_category);

        //list header
        listHeaderName = rootView.findViewById(R.id.sectionHeader);
        listHeaderRestaurentName = rootView.findViewById(R.id.headerResturantName);
        listHeaderRestaurentType = rootView.findViewById(R.id.headerRestaurentType);
        listHeaderRestaurentDiscount = rootView.findViewById(R.id.headerDiscount);
        listHeaderRestaurentImage = rootView.findViewById(R.id.restaurentListHeaderImage);
		listHeaderLayout = rootView.findViewById(R.id.headerRestaurent);
        discount = rootView.findViewById(R.id.discount);

        String firstName = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_FIRST_NAME, null);
        if (firstName != null) {
            mGoodMorningTextView.setText(getResources().getString(R.string.tv_good_morning_label_text) + ", " + firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " !");
        } else {
            mGoodMorningTextView.setText(getResources().getString(R.string.tv_good_morning_label_text) + "!");
        }

        // init category recycler view
        // init recycler
        /*categoriesList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categoriesList,getContext());
        categoriesRecyclerView = rootView.findViewById(R.id.categories_recycler_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        categoriesRecyclerView.setHasFixedSize(ic_true);
        categoriesRecyclerView.setAdapter(categoriesAdapter);*/
    }
    //==============================================================================================
    private RestaurantsListAdapter mTablesAdapter;
    private ArrayList<CategoriesWithRestaurant> mTablesList;

    private void setUpTablesRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);

        mTablesAdapter = new RestaurantsListAdapter(getContext(), new RestaurantsListAdapter.OnTablesClickListener() {
            @Override
            public void onTableClick(int position, String categoryName) {
                Intent intent = new Intent(getActivity(), TableRestaurantsActivity.class);
                intent.putExtra(TABLE_ID_KEY, position);
                intent.putExtra(CATEGORY_NAME, categoryName);
                startActivity(intent);
            }

            @Override
            public void onRestaurantClick(int parentPosition, int childPosition) {
                Intent intent = new Intent(getActivity(), RestaurantProfileActivity.class);
                intent.putExtra(RESTAURENT_KEY, childPosition);
                intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
                startActivity(intent);
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

    private void showData() {
//		listHeaderLayout.setVisibility(View.VISIBLE);
        mTablesRecyclerView.setVisibility(View.VISIBLE);
//        categoriesRecyclerView.setVisibility(View.VISIBLE);
//        ourSelectionLinearLayout.setVisibility(View.VISIBLE);
    }

    private void hideData() {
//		listHeaderLayout.setVisibility(View.GONE);
        mTablesRecyclerView.setVisibility(View.GONE);
//        categoriesRecyclerView.setVisibility(View.GONE);
//        ourSelectionLinearLayout.setVisibility(View.GONE);

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
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new BasePermissionListener(){
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                super.onPermissionGranted(response);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                super.onPermissionDenied(response);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                super.onPermissionRationaleShouldBeShown(permission, token);
                            }
                        }).check();
                hidePermissionNotGrantedError();
                if (isGpsEnabled) {
                    hideGpsDisabledError();
                    getLastLocation();
                } else {
                    hideLoadingIndicator();
                    hideData();
                    hideDataNotFoundError();
                    hideLocationNotDetectedError();
                    showGpsDisabledError();
                }

            } else {
                // Permission denied.
                hideLoadingIndicator();
                hideData();
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
                    getTablesList(mLatitude, mLongitude);
                } else {
                    Log.w(TAG, task.getException());
                    hideLoadingIndicator();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideDataNotFoundError();
                    showLocationNotDetectedError();
                }
            }
        });
    }

    private void getTablesList(final double latitude, final double longitude) {
        int userId = SharedPreferencesHelper.getSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, -10);

        if (userId == -10) {
			MainPageWebService webService = RetrofitWebService.retrofit.create(MainPageWebService.class);
            Call<RestaurantsList> tablesListCall = webService.getTablesList(0, latitude, longitude);
            tablesListCall.enqueue(new Callback<RestaurantsList>() {
                @Override
                public void onResponse(Call<RestaurantsList> call, Response<RestaurantsList> response) {
                    hideLoadingIndicator();
                    Log.d(TAG + "_onResponse", String.valueOf(response));
                    if (response.isSuccessful()) {
                        mTablesList = response.body().getCategoriesWithRestaurants();
                        if (mTablesList != null && mTablesList.size() > 0) {
//                            fillRestaurentHeader(response.body().getRestaurantOfTheWeek());
//							addFooterList(response.body().getCategories());

							ArrayList<Object> allData = new ArrayList<>();
							allData.add(response.body().getRestaurantOfTheWeek());
							allData.addAll(removeEmptyCategories(mTablesList));
							allData.add(response.body().getCategories());
                            mTablesAdapter.setRestaurantsLists(allData);
                            mTablesAdapter.setHasStableIds(true);
                            mTablesRecyclerView.setAdapter(mTablesAdapter);
							showData();
                        } else {
                            Log.d(TAG, "mTablesList is null or empty");
                            hideData();
                            hidePermissionNotGrantedError();
                            hideGpsDisabledError();
                            hideLocationNotDetectedError();
                            showDataNotFoundError();
                        }
                    } else {
                        Log.d(TAG, "response.isSuccessful() == false");
                        hideData();
                        hidePermissionNotGrantedError();
                        hideGpsDisabledError();
                        hideLocationNotDetectedError();
                        showDataNotFoundError();
                    }

                }

                @Override
                public void onFailure(Call<RestaurantsList> call, Throwable t) {
                    Log.d(TAG + "_onFailure", String.valueOf(t.getMessage()));
                    hideLoadingIndicator();
                    hideData();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideLocationNotDetectedError();
                    showDataNotFoundError();
                }
            });
        } else {
			MainPageWebService webService = RetrofitWebService.retrofit.create(MainPageWebService.class);
            Call<RestaurantsList> tablesListCall = webService.getTablesList(userId, latitude, longitude);
            tablesListCall.enqueue(new Callback<RestaurantsList>() {
                @Override
                public void onResponse(Call<RestaurantsList> call, Response<RestaurantsList> response) {
                    hideLoadingIndicator();
                    Log.d(TAG + "_onResponse", String.valueOf(response));
                    if (response.isSuccessful()) {
                        mTablesList = response.body().getCategoriesWithRestaurants();
                        if (mTablesList != null && mTablesList.size() > 0) {
//                            fillRestaurentHeader(response.body().getRestaurantOfTheWeek());
//							addFooterList(response.body().getCategories());
                            ArrayList<Object> allData = new ArrayList<>();
                            allData.add(response.body().getRestaurantOfTheWeek());
                            allData.addAll(removeEmptyCategories(mTablesList));
                            allData.add(response.body().getCategories());
                            mTablesAdapter.setRestaurantsLists(allData);
                            mTablesAdapter.setHasStableIds(true);
                            mTablesRecyclerView.setAdapter(mTablesAdapter);
							showData();
                        } else {
                            hideData();
                            hidePermissionNotGrantedError();
                            hideGpsDisabledError();
                            hideLocationNotDetectedError();
                            showDataNotFoundError();
                        }
                    } else {
                        hideData();
                        hidePermissionNotGrantedError();
                        hideGpsDisabledError();
                        hideLocationNotDetectedError();
                        showDataNotFoundError();
                    }
                }

                @Override
                public void onFailure(Call<RestaurantsList> call, Throwable t) {
                    Log.d(TAG + "_onFailure", String.valueOf(t.getMessage()));
                    hideLoadingIndicator();
                    hideData();
                    hidePermissionNotGrantedError();
                    hideGpsDisabledError();
                    hideLocationNotDetectedError();
                    showDataNotFoundError();
                }
            });
        }
    }
    //==============================================================================================

    private ArrayList<Object> removeEmptyCategories(ArrayList<CategoriesWithRestaurant> restaurantsLists){
        ArrayList<Object> restaurantsListsWithoutEmpty = new ArrayList<Object>();
        for (int i =0 ; i<restaurantsLists.size(); i++){
            if (restaurantsLists.get(i).getRestaurants().size()>0){
                restaurantsListsWithoutEmpty.add(restaurantsLists.get(i));
            }
        }
        return restaurantsListsWithoutEmpty;
    }
    private LocationManager mGpsLocationManager;

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onProviderDisabled(String provider) {
        hideData();
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
                hidePermissionNotGrantedError();
                hideDataNotFoundError();
                hideLocationNotDetectedError();
                showGpsDisabledError();
                break;

            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                hideData();
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
    private void searchLayoutClick() {
        searchViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchRestaurantActivity.class);
                intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
                startActivity(intent);
            }
        });
//        seeAllCategories.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SearchRestaurantActivity.class);
//                intent.setAction(ACTION_HOME_CATEGORIES);
//                startActivity(intent);
//            }
//        });
    }

    private void currentLocationButtonClick() {
        mCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectLocationActivity.class);
                intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
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
    private String mAction;
    private City mCity;
    private int mUserId;
    private String mCityName;
    private double mCityLatitude;
    private double mCityLongitude;
    private boolean isNetworkOk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        findViewsById(rootView); // (1)

        setUpTablesRecyclerView(); // (2)
        //==========================================================================================

        //
        ViewTreeObserver viewTreeObserver = bonjourLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    bonjourLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int height = bonjourLayout.getHeight();
					float scale = getResources().getDisplayMetrics().density;
					int dpAsPixels = (int) (height*scale + 0.5f);
					mTablesRecyclerView.setPadding(0,height,0,0);
//                    mTablesRecyclerView.requestLayout();

                }
            });
        }
        //
        mAction = getActivity().getIntent().getStringExtra(SelectLocationActivity.KEY_SEARCH_BY);
        mUserId = SharedPreferencesHelper.getSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, -10);
        mCityName = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_SELECTED_CITY, null);
        mCityLatitude = Double.parseDouble(String.valueOf(SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_CITY_LATITUDE, "-10")));
        mCityLongitude = Double.parseDouble(String.valueOf(SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_CITY_LONGITUDE, "-10")));

        isNetworkOk = NetworkHelper.hasNetworkAccess(getContext());
        if (isNetworkOk) {
            if (mCityName != null) {
                mCurrentLocationButton.setText(mCityName);
            }

            if (mAction == null) {
                if (mCityLatitude != -10 && mCityLongitude != -10) {
                    showLoadingIndicator();
                    getTablesList(mCityLatitude, mCityLongitude);
                    mCurrentLocationButton.setText(mCityName);

                } else {
                    askForLocationPermissionAndGetData();
                    mCurrentLocationButton.setText(getResources().getString(R.string.btn_home_current_location_text));
                }
            } else {
                if (mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_CITY + "-" + ConstantsHelper.ACTION_HOME_FRAGMENT) ||
                        mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_CITY + "-" + ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                    mCurrentLocationButton.setText(mCityName);
                    showLoadingIndicator();
                    getTablesList(mCityLatitude, mCityLongitude);
                } else if (mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_LOCATION + "-" + ConstantsHelper.ACTION_HOME_FRAGMENT) ||
                        mAction.equals(SelectLocationActivity.VALUE_SEARCH_BY_LOCATION + "-" + ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                    askForLocationPermissionAndGetData();
                    mCurrentLocationButton.setText(getResources().getString(R.string.btn_home_current_location_text));
                }
            }
        }
        else {
            hideData();
            mCurrentLocationButton.setEnabled(false);
            mSearchTextView.setEnabled(false);
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mErrorMessageTextView.setText(getContext().getResources().getString(R.string.sign_in_activity_toast_message_no_internet_connection));
        }
        //==========================================================================================
        searchLayoutClick();

        currentLocationButtonClick();

        actionGrantPermissionClick();

        actionRetryClick();


        //==========================================================================================
        return rootView;
    }
    //==============================================================================================
    @Override
    public void onResume() {
        // handle permissions
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

    private void fillRestaurentHeader(final RestaurantOfTheWeek headerRestaurent){
        listHeaderRestaurentName.setText(headerRestaurent.getName());
        listHeaderRestaurentType.setText(headerRestaurent.getCuisineName());
        if (headerRestaurent.getPromotionAmount() > 0){
            listHeaderRestaurentDiscount.setText(headerRestaurent.getPromotionAmount() + "%");
        }else{
            discount.setVisibility(View.GONE);
        }

        Picasso.with(getContext())
                .load(headerRestaurent.getPicture())
                .into(listHeaderRestaurentImage);
        listHeaderRestaurentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RestaurantProfileActivity.class);
                intent.putExtra(RESTAURENT_KEY, headerRestaurent.getId());
                intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    private void addFooterList(ArrayList<Category> categories){
        categoriesList.clear();
        categoriesList.addAll(categories);
        categoriesAdapter.notifyDataSetChanged();
	}
}
