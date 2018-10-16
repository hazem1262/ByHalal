package com.example.alexander.halalappv1.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.SearchRestaurantAdapter;
import com.example.alexander.halalappv1.fragments.SearchFragment;
import com.example.alexander.halalappv1.model.User;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteRestaurantsActivity extends AppCompatActivity {

    private static final String TAG = "xxxxx";
    public static final String RESTAURANT_OBJECT_KEY = "RestaurantObject";

    private EditText mSignInEmailEditText;
    private EditText mSignInPasswordEditText;
    private Button mSignInButton;
    private EditText mSignUpFirstNameEditText;
    private EditText mSignUpFamilyNameEditText;
    private EditText mSignUpEmailEditText;
    private EditText mSignUpMobileNumberEditText;
    private EditText mSignUpPasswordEditText;
    private EditText mSignUpRetypedPasswordEditText;
    private Button mSignUpButton;
    private ConstraintLayout mRestaurantsListLayout;
    private ConstraintLayout mSignInSignUpLayout;
    private boolean isLoggedIn;

    private ConstraintLayout mSignInLayout;
    private Button mCreateNewAccountButton;
    private ConstraintLayout mSignUpLayout;
    private ImageView mSignUpArrowBackImageView;

    private ImageView mArrowBackImageView;
    private TextView mErrorMessageTextView;
    private RecyclerView mFavouriteRestaurantsRecyclerView;
    private ConstraintLayout mLoadingIndicator;
    private SearchRestaurantAdapter mSearchRestaurantAdapter;
    private List<Restaurant> mRestaurantList;

    private int mUserId;
    private int mRestaurantId;
    private String mAction;
    private boolean isNetworkOk;
    //==============================================================================================
    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAction != null) {
                    if (mAction.equals(ConstantsHelper.ACTION_PROFILE_FRAGMENT)) {
                        Intent intent = new Intent(FavouriteRestaurantsActivity.this, MainActivity.class);
                        intent.setAction(ConstantsHelper.ACTION_PROFILE_FRAGMENT );
                        startActivity(intent);
                    } else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                        Intent intent = new Intent(FavouriteRestaurantsActivity.this, MainActivity.class);
                        intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(FavouriteRestaurantsActivity.this, SearchRestaurantActivity.class);
                        intent.setAction(mAction.split("-")[0]);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void showData() {
        mErrorMessageTextView.setVisibility(View.GONE);
        mFavouriteRestaurantsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mFavouriteRestaurantsRecyclerView.setVisibility(View.GONE);
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void setUpFavouriteRestaurantsRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mFavouriteRestaurantsRecyclerView.setLayoutManager(layoutManager);
        mSearchRestaurantAdapter = new SearchRestaurantAdapter(this, new SearchRestaurantAdapter.OnRestaurantListClickListener() {
            @Override
            public void onFavoriteButtonClick(int itemPosition) {
                isNetworkOk = NetworkHelper.hasNetworkAccess(FavouriteRestaurantsActivity.this);
                if (isNetworkOk) {
                    showLoadingIndicator();
                    markAsFavourite(mUserId, mRestaurantList.get(itemPosition).getId(), itemPosition);
                }
            }

            @Override
            public void onListItemClick(int itemPosition) {
                Restaurant restaurant = mRestaurantList.get(itemPosition);
                Intent intent = new Intent(FavouriteRestaurantsActivity.this, RestaurantProfileActivity.class);
                intent.putExtra(RESTAURANT_OBJECT_KEY, restaurant);
                intent.setAction(mAction + "-" + ConstantsHelper.ACTION_FAVOURITE_RESTAURANTS_ACTIVITY);
                startActivity(intent);
            }
        });
    }

    private void getFavouriteRestaurants(int userId) {
        if (userId != -10) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<ArrayList<Restaurant>> restaurantCall = webService.getFavouriteRestaurants(userId);
            restaurantCall.enqueue(new Callback<ArrayList<Restaurant>>() {
                @Override
                public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        mRestaurantList = response.body();
                        if (mRestaurantList != null && mRestaurantList.size() > 0) {
                            showData();
//                            mSearchRestaurantAdapter.setRestaurantList(mRestaurantList);
//                            mFavouriteRestaurantsRecyclerView.setAdapter(mSearchRestaurantAdapter);
                        } else {
                            showError();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {
                    Log.d(TAG, String.valueOf(t.getMessage()));
                }
            });
        }
    }

    private void markAsFavourite(final int userId, int restaurantId, final int itemPosition) {
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
                        mSearchRestaurantAdapter.setRestaurantList(null);
                        mFavouriteRestaurantsRecyclerView.setAdapter(mSearchRestaurantAdapter);
                        showLoadingIndicator();
                        getFavouriteRestaurants(userId);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_restaurants);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mArrowBackImageView = findViewById(R.id.favourite_restaurants_activity_arrow_back);
        mErrorMessageTextView = findViewById(R.id.tv_favourite_restaurants_activity_error_message);
        mFavouriteRestaurantsRecyclerView = findViewById(R.id.rv_favourite_restaurants_activity_restaurants_list);
        mLoadingIndicator = findViewById(R.id.pb_favourite_restaurants_activity_loading_indicator);

        mSignInEmailEditText = findViewById(R.id.et_sign_in_activity_email);
        mSignInPasswordEditText = findViewById(R.id.et_sign_in_activity_password);
        mSignInButton = findViewById(R.id.btn_sign_in_activity_sign_in);
        mSignUpFirstNameEditText = findViewById(R.id.et_sign_up_activity_first_name);
        mSignUpFamilyNameEditText = findViewById(R.id.et_sign_up_activity_family_name);
        mSignUpEmailEditText = findViewById(R.id.et_sign_up_activity_email);
        mSignUpMobileNumberEditText = findViewById(R.id.et_sign_up_activity_mobile_number);
        mSignUpPasswordEditText = findViewById(R.id.et_sign_up_activity_password);
        mSignUpRetypedPasswordEditText = findViewById(R.id.et_sign_up_activity_retype_password);
        mSignUpButton = findViewById(R.id.btn_sign_up_activity_sign_up);
        mRestaurantsListLayout = findViewById(R.id.layout_favourite_restaurants_activity_restaurants_list);
        mSignInSignUpLayout = findViewById(R.id.layout_favourite_restaurants_activity_sign_in_sign_up);

        mSignInLayout = findViewById(R.id.layout_sign_in);
        mCreateNewAccountButton = findViewById(R.id.btn_sign_in_activity_create_naw_account);
        mSignUpLayout = findViewById(R.id.layout_sign_up);
//        mSignUpArrowBackImageView = findViewById(R.id.iv_sign_up_arrow_back);

        mAction = getIntent().getAction();
        mRestaurantId = getIntent().getIntExtra(SearchFragment.RESTAURANT_ID_KEY, -10);
        setUpFavouriteRestaurantsRecyclerView();

        isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(FavouriteRestaurantsActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            mRestaurantsListLayout.setVisibility(View.VISIBLE);
            mSignInSignUpLayout.setVisibility(View.GONE);
            mUserId = SharedPreferencesHelper.getSharedPreferenceInt(this, ConstantsHelper.KEY_USER_ID, -10);
            getFavouriteRestaurants(mUserId);
        } else {
            mRestaurantsListLayout.setVisibility(View.GONE);
            mSignInSignUpLayout.setVisibility(View.VISIBLE);
        }

        arrowBackClick();

        signInButtonClick();

        signUpButtonClick();

        createNewAccountButtonClick();

        signUpArrowBackImageViewClick();
    }

    @Override
    public void onBackPressed() {
        if (mSignUpLayout.getVisibility() == View.VISIBLE) {
            mSignUpLayout.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mSignInLayout.animate()
                                    .translationY(0)
                                    .alpha(1.0f)
                                    .setDuration(300)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mSignUpLayout.setVisibility(View.GONE);
                                            mSignInLayout.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    });
        } else {
            if (mAction != null) {
                if (mAction.equals(ConstantsHelper.ACTION_PROFILE_FRAGMENT)) {
                    Intent intent = new Intent(FavouriteRestaurantsActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_PROFILE_FRAGMENT );
                    startActivity(intent);
                } else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                    Intent intent = new Intent(FavouriteRestaurantsActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FavouriteRestaurantsActivity.this, SearchRestaurantActivity.class);
                    intent.setAction(mAction.split("-")[0]);
                    startActivity(intent);
                }
            }
        }
    }
    //==============================================================================================
    private void signInButtonClick() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(FavouriteRestaurantsActivity.this);
                if (isNetworkOk) {
                    String email = mSignInEmailEditText.getText().toString();
                    String password = mSignInPasswordEditText.getText().toString();
                    boolean isDataValid = isSignInDataValid(email, password);
                    if (isDataValid) {
                        showLoadingIndicator();
                        signInExistingUser(email, password);
                    }
                } else {
                    Context context = FavouriteRestaurantsActivity.this;
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_no_internet_connection);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }
        });
    }

    private void signUpButtonClick() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(FavouriteRestaurantsActivity.this);
                if (isNetworkOk) {
                    String firstName = mSignUpFirstNameEditText.getText().toString();
                    String familyName = mSignUpFamilyNameEditText.getText().toString();
                    String email = mSignUpEmailEditText.getText().toString();
                    String mobileNumber = mSignUpMobileNumberEditText.getText().toString();
                    String password = mSignUpPasswordEditText.getText().toString();
                    String retypedPassword = mSignUpRetypedPasswordEditText.getText().toString();
                    boolean isValid = isSignUpDataValid(firstName, familyName, email, mobileNumber, password, retypedPassword);

                    if (isValid) {
                        showLoadingIndicator();
                        signUpNewUser(firstName, familyName, email, mobileNumber, password);
                    }
                } else {
                    Context context = FavouriteRestaurantsActivity.this;
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_no_internet_connection);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }
        });
    }

    private void createNewAccountButtonClick() {
        mCreateNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignInLayout.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mSignUpLayout.animate()
                                        .translationY(0)
                                        .alpha(1.0f)
                                        .setDuration(300)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                mSignInLayout.setVisibility(View.GONE);
                                                mSignUpLayout.setVisibility(View.VISIBLE);
                                            }
                                        });
                            }
                        });

//                mSignInLayout.setVisibility(View.GONE);
//                mSignUpLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void signUpArrowBackImageViewClick() {
        mSignUpArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignUpLayout.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mSignInLayout.animate()
                                        .translationY(0)
                                        .alpha(1.0f)
                                        .setDuration(300)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                mSignUpLayout.setVisibility(View.GONE);
                                                mSignInLayout.setVisibility(View.VISIBLE);
                                            }
                                        });
                            }
                        });

//                mSignUpLayout.setVisibility(View.GONE);
//                mSignInLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean isSignInDataValid(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.sign_in_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(this, getResources().getString(R.string.sign_in_activity_toast_message_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.sign_in_activity_toast_message_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void signInExistingUser(String email, String password) {
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<User> call = webService.signInExistingUser(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        int userId = user.getUserId();
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        String email = user.getEmail();
                        String mobileNumber = user.getMobileNo();
                        String password = user.getPassword();

                        if (firstName != null && lastName != null && email != null && mobileNumber != null && password != null) {
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FIRST_NAME, firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FAMILY_NAME, lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_EMAIL, email);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_MOBILE_NUMBER, mobileNumber);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_PASSWORD, password);
                            SharedPreferencesHelper.setSharedPreferenceInt(FavouriteRestaurantsActivity.this, ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(FavouriteRestaurantsActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            reloadFavouriteRestaurantsActivity();
                        } else {
                            Context context = FavouriteRestaurantsActivity.this;
                            String message = getResources().getString(R.string.sign_in_activity_toast_message_wrong_email_or_wrong_password);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = FavouriteRestaurantsActivity.this;
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_problem_sign_in_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = FavouriteRestaurantsActivity.this;
                String message = getResources().getString(R.string.sign_in_activity_toast_message_problem_sign_in_try_again_later);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    private boolean isSignUpDataValid(String firstName, String familyName, String email, String mobileNumber, String password, String retypedPassword) {
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_first_name_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(familyName)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_family_name_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mobileNumber)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_mobile_number_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! Pattern.matches("[a-zA-Z]+", mobileNumber) && ! (mobileNumber.length() == 10)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_invalid_mobile_number), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_password_is_at_least_6_chars), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(retypedPassword)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_retyped_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! password.equals(retypedPassword)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_password_and_retyped_password_do_not_match), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void signUpNewUser(String firstName, String familyName, String email, String mobileNumber, String password) {
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<User> call = webService.signUpNewUser(firstName, familyName, email, mobileNumber, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        int userId = user.getUserId();
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        String email = user.getEmail();
                        String mobileNumber = user.getMobileNo();
                        String password = user.getPassword();

                        if (firstName != null && lastName != null && email != null && mobileNumber != null && password != null) {
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FIRST_NAME, firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FAMILY_NAME, lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_EMAIL, email);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_MOBILE_NUMBER, mobileNumber);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_PASSWORD, password);
                            SharedPreferencesHelper.setSharedPreferenceInt(FavouriteRestaurantsActivity.this, ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(FavouriteRestaurantsActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            reloadFavouriteRestaurantsActivity();
                        } else {
                            Context context = FavouriteRestaurantsActivity.this;
                            String message = getResources().getString(R.string.sign_up_activity_toast_message_account_already_exists);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = FavouriteRestaurantsActivity.this;
                    String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = FavouriteRestaurantsActivity.this;
                String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    private void saveUserDataInSharedPreferences(String key, String value) {
        SharedPreferencesHelper.setSharedPreferenceString(FavouriteRestaurantsActivity.this, key, value);
    }

    private void reloadFavouriteRestaurantsActivity() {
        if (mRestaurantId != -10) {
            if (mAction != null) {
                if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                    Intent intent = new Intent(FavouriteRestaurantsActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                    startActivity(intent);
                }
            } else {
                finish();
            }

        } else {
            Intent intent = new Intent(FavouriteRestaurantsActivity.this, FavouriteRestaurantsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(SearchFragment.RESTAURANT_ID_KEY, mRestaurantId);
            intent.setAction(mAction);
            startActivity(intent);
        }
    }
}
