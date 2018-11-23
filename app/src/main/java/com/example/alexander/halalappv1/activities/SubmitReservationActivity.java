package com.example.alexander.halalappv1.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.OrderAdapter;
import com.example.alexander.halalappv1.model.ReservationOrder;
import com.example.alexander.halalappv1.model.User;

import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.model.newModels.RestaurantProfile;
import com.example.alexander.halalappv1.model.newModels.reservation.details.Product;
import com.example.alexander.halalappv1.reservation.UpComingReservation;
import com.example.alexander.halalappv1.services.RestaurentProfileWebService;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.activities.EditReservationActivity.RESERVATION_ID;
import static com.example.alexander.halalappv1.fragments.HomeFragment.RESTAURENT_KEY;

public class SubmitReservationActivity extends AppCompatActivity {

    private EditText mSignInEmailEditText;
    private RecyclerView mMenuListView;
    private OrderAdapter mMenuAdapter;
    private EditText mSignInPasswordEditText;
    private Button mSignInButton;
    private EditText mSignUpFirstNameEditText;
    private EditText mSignUpFamilyNameEditText;
    private EditText mSignUpEmailEditText;
    private EditText mSignUpMobileNumberEditText;
    private EditText mSignUpPasswordEditText;
    private EditText mSignUpRetypedPasswordEditText;
    private Button mSignUpButton;
    private ConstraintLayout mReservationFormLayout;
    private ConstraintLayout mSignInSignUpLayout;
    private boolean isLoggedIn;

    private ConstraintLayout mSignInLayout;
    private Button mCreateNewAccountButton;
    private ConstraintLayout mSignUpLayout;
    private TextView mSignUpArrowBackImageView;
    private TextView backText;
    private ImageView mRestaurantImageImageView;
    private TextView mArrowBackImageView;
    private TextView mRestaurantNameTextView;
    private TextView mNumberOfPeopleTextView;
    private TextView mReserveDateTextView;
    private TextView mReserveTimeTextView;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mPhoneNumberEditText;
    private EditText mEmailEditText;
    private EditText mCommentEditText;
    private Button mSubmitButton;
    private ConstraintLayout mLoadingIndicator;

    private int mUserId;
    private UpComingReservation mUpComingReservation;
    private RestaurantProfile mRestaurant;
    private int mRestaurantId;
    private String mSelectedDate;
    private String mFormattedDate;
    private String mSelectedTime;
    private String mSelectedNumberOfPeople;
    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;
    private String mEmail;
    private String mComments;
    private LinearLayout promotionLayout;
    private TextView promotionText;
    private TextView reservationPromotionDay;
    private boolean isNetworkOk;
    private LinearLayout notLoggedUser;
    private ArrayList<ReservationOrder> mReservationOrdersList;
    private ConstraintLayout signInLayOut;
    private ConstraintLayout signUpLayout;

    private void findViewsById() {
        mMenuListView = findViewById(R.id.elv_menu_activity_menu_list);
        promotionLayout = findViewById(R.id.promotionLayout);
        promotionText = findViewById(R.id.promotionAmount);
        reservationPromotionDay = findViewById(R.id.reservationPromotionDay);
        signInLayOut = findViewById(R.id.signInLayOut);
        signUpLayout = findViewById(R.id.signUpLayout);
        notLoggedUser = findViewById(R.id.notLoggedUser);
        backText = findViewById(R.id.retourSignIn);
        mRestaurantImageImageView = findViewById(R.id.iv_submit_reservation_activity_restaurant_image);
        mArrowBackImageView = findViewById(R.id.iv_submit_reservation_activity_arrow_back);
        mRestaurantNameTextView = findViewById(R.id.tv_submit_reservation_activity_restaurant_name);
        mNumberOfPeopleTextView = findViewById(R.id.tv_submit_reservation_activity_number_of_people);
        mReserveDateTextView = findViewById(R.id.tv_submit_reservation_activity_reserve_date);
        mReserveTimeTextView = findViewById(R.id.tv_submit_reservation_activity_reserve_time);
        mFirstNameEditText = findViewById(R.id.et_submit_reservation_activity_first_name_value);
        mLastNameEditText = findViewById(R.id.et_submit_reservation_activity_last_name_value);
        mPhoneNumberEditText = findViewById(R.id.et_submit_reservation_activity_phone_number_value);
        mEmailEditText = findViewById(R.id.et_submit_reservation_activity_email_value);
        mCommentEditText = findViewById(R.id.et_submit_reservation_activity_write_comment);
        mSubmitButton = findViewById(R.id.btn_submit_reservation_activity_submit);
        mLoadingIndicator = findViewById(R.id.pb_submit_reservation_activity_loading_indicator);

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
        mReservationFormLayout = findViewById(R.id.layout_submit_reservation_activity_reservation_form);
        mSignInSignUpLayout = findViewById(R.id.layout_submit_reservation_activity_sign_in);

        mSignInLayout = findViewById(R.id.layout_sign_in);
        mCreateNewAccountButton = findViewById(R.id.btn_sign_in_activity_create_naw_account);
        mSignUpLayout = findViewById(R.id.layout_sign_up);
        mSignUpArrowBackImageView = findViewById(R.id.iv_sign_up_arrow_back);
    }

    private void updateRestaurantInformationViews() {
        String language = SharedPreferencesHelper.getSharedPreferenceString(SubmitReservationActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
        if (mUpComingReservation != null) {
            mRestaurantNameTextView.setText(mRestaurant.getName());
            if (language != null) {
                if (language.equals("français")) {
                    mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople + " personnes");
                } else {
                    mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople + " People");
                }
            }
            mReserveDateTextView.setText(mSelectedDate);
            mReserveTimeTextView.setText(mSelectedTime.replace(":","h"));
        } else {
            Picasso.with(this).load(mRestaurant.getPicture()).into(mRestaurantImageImageView);
            mRestaurantNameTextView.setText(mRestaurant.getName());
            if (language != null) {
                if (true) {
                    mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople + " personnes");
                } else {
                    mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople + " People");
                }
            }
            mReserveDateTextView.setText(mSelectedDate);
            mReserveTimeTextView.setText(mSelectedTime.replace(":","h"));
        }
    }

    private void updatePersonalInformationViews() {
        if (mUpComingReservation != null) {
            mFirstNameEditText.setText(mUpComingReservation.getUserData().getFirstName());
            mLastNameEditText.setText(mUpComingReservation.getUserData().getLastName());
            mEmailEditText.setText(mUpComingReservation.getUserData().getEmail());
            mPhoneNumberEditText.setText(mUpComingReservation.getUserData().getPhone());
        } else {
            String firstName = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_FIRST_NAME, null);
            if (firstName != null) {
                mFirstNameEditText.setText(firstName);
            }

            String lastName = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_FAMILY_NAME, null);
            if (lastName != null) {
                mLastNameEditText.setText(lastName);
            }

            String phoneNumber = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_MOBILE_NUMBER, null);
            if (phoneNumber != null) {
                mPhoneNumberEditText.setText(phoneNumber);
            }

            String email = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_EMAIL, null);
            if (email != null) {
                mEmailEditText.setText(email);
            }
        }
    }

    private String getFormattedDate(String date) {
        String[] dateParts = date.split("/");
        if (dateParts.length > 2) {
            Log.d("XXXXX_date", String.valueOf(dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0]));
            return dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
        } else {
            Log.d("XXXXX_date", String.valueOf(date));
            return date;
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

    private void submitButtonClick() {
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirstName = mFirstNameEditText.getText().toString();
                mLastName = mLastNameEditText.getText().toString();
                mPhoneNumber = mPhoneNumberEditText.getText().toString();
                mEmail = mEmailEditText.getText().toString();
                mComments = mCommentEditText.getText().toString();

                if (TextUtils.isEmpty(mFirstName)) {
                    Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_first_name_is_required), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mLastName)) {
                    Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_family_name_is_required), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mPhoneNumber)) {
                    Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_mobile_number_is_required), Toast.LENGTH_LONG).show();
                } else if (! Pattern.matches("[a-zA-Z]+", mPhoneNumber) && ! (mPhoneNumber.length() == 10)) {
                    Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_invalid_mobile_number), Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mEmail)) {
                    Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
                } else {
                    if (mUserId != -10) {
                        isNetworkOk = NetworkHelper.hasNetworkAccess(SubmitReservationActivity.this);
                        if (isNetworkOk) {
                            String productsList;
                            if (mReservationOrdersList == null) {
                                productsList = "";
                            } else {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Restaurant>>() {}.getType();
                                productsList = gson.toJson(mReservationOrdersList, type);
                            }

                            int reservationID = getIntent().getIntExtra(RESERVATION_ID, -10);
                            if (reservationID != -10) {
                                showLoadingIndicator();
                                RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
                                Call<JsonObject> reserveCall = webService.editReservation(
                                        mUserId,
                                        mRestaurantId,
                                        mFormattedDate,
                                        mSelectedTime,
                                        mSelectedNumberOfPeople,
                                        mFirstName,
                                        mLastName,
                                        mEmail,
                                        mPhoneNumber,
                                        mComments,
                                        productsList,
                                        reservationID);


                                reserveCall.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        hideLoadingIndicator();
                                        if (response.isSuccessful()) {
                                            JsonObject jsonObjectResponse = response.body();
                                            if (jsonObjectResponse != null) {
                                                String data = jsonObjectResponse.get("data").getAsString();
                                                if (data.equals("success")) {
                                                    Log.d("FFFFF_success", String.valueOf(response));
                                                    Log.d("FFFFF_success", String.valueOf(response.body()));
                                                    Intent intent = new Intent(SubmitReservationActivity.this, MainActivity.class);
                                                    intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Log.d("FFFFF_fail", String.valueOf(response));
                                            Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        hideLoadingIndicator();
                                        Log.d("FFFFF_error", String.valueOf(t.getMessage()));
                                        Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                showLoadingIndicator();
                                RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
                                Call<JsonObject> reserveCall = webService.makeReservation(
                                        mUserId,
                                        mRestaurantId,
                                        mFormattedDate,
                                        mSelectedTime,
                                        mSelectedNumberOfPeople,
                                        mFirstName,
                                        mLastName,
                                        mEmail,
                                        mPhoneNumber,
                                        mComments,
                                        productsList);
                                reserveCall.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        hideLoadingIndicator();
                                        if (response.isSuccessful()) {
                                            JsonObject jsonObjectResponse = response.body();
                                            if (jsonObjectResponse != null) {
                                                String data = jsonObjectResponse.get("data").getAsString();
                                                if (data.equals("success")) {
                                                    Log.d("FFFFF_success", String.valueOf(response));
                                                    Log.d("FFFFF_success", String.valueOf(response.body()));
                                                    Intent intent = new Intent(SubmitReservationActivity.this, MainActivity.class);
                                                    intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Log.d("FFFFF_fail", String.valueOf(response));
                                            Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        hideLoadingIndicator();
                                        Log.d("FFFFF_error", String.valueOf(t.getMessage()));
                                        Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void showSubmitReservationAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_submit_reservation, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        TextView cancelTextView = dialogView.findViewById(R.id.tv_submit_reservation_alert_dialog_action_cancel);
        TextView submitTextView = dialogView.findViewById(R.id.tv_submit_reservation_alert_dialog_action_submit);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        submitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productsList = "";
                if (mReservationOrdersList == null) {
                    productsList = "";
                } else {
                    Gson gson = new Gson();
//                    Type type = new TypeToken<List<Restaurant>>() {}.getType();
//                    productsList = gson.toJson(mReservationOrdersList, type);
                }

                if (mUpComingReservation != null) {
                    showLoadingIndicator();
                    RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
                    Call<JsonObject> reserveCall = webService.editReservation(
                            mUserId,
                            mUpComingReservation.getRestaurant().getId(),
                            mFormattedDate,
                            mSelectedTime,
                            mSelectedNumberOfPeople,
                            mFirstName,
                            mLastName,
                            mEmail,
                            mPhoneNumber,
                            mComments,
                            productsList,
                            mUpComingReservation.getBookingData().getReservationId());



                    reserveCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            hideLoadingIndicator();
                            if (response.isSuccessful()) {
                                JsonObject jsonObjectResponse = response.body();
                                if (jsonObjectResponse != null) {
                                    String data = jsonObjectResponse.get("data").getAsString();
                                    if (data.equals("success")) {
                                        Log.d("FFFFF_success", String.valueOf(response));
                                        Log.d("FFFFF_success", String.valueOf(response.body()));
                                        Intent intent = new Intent(SubmitReservationActivity.this, MainActivity.class);
                                        intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Log.d("FFFFF_fail", String.valueOf(response));
                                Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            hideLoadingIndicator();
                            Log.d("FFFFF_error", String.valueOf(t.getMessage()));
                            Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    showLoadingIndicator();
                    RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
                    Call<JsonObject> reserveCall = webService.makeReservation(
                            mUserId,
                            mRestaurantId,
                            mFormattedDate,
                            mSelectedTime,
                            mSelectedNumberOfPeople,
                            mFirstName,
                            mLastName,
                            mEmail,
                            mPhoneNumber,
                            mComments,
                            productsList);
                    reserveCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            hideLoadingIndicator();
                            if (response.isSuccessful()) {
                                JsonObject jsonObjectResponse = response.body();
                                if (jsonObjectResponse != null) {
                                    String data = jsonObjectResponse.get("data").getAsString();
                                    if (data.equals("success")) {
                                        Log.d("FFFFF_success", String.valueOf(response));
                                        Log.d("FFFFF_success", String.valueOf(response.body()));
                                        Intent intent = new Intent(SubmitReservationActivity.this, MainActivity.class);
                                        intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Log.d("FFFFF_fail", String.valueOf(response));
                                Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            hideLoadingIndicator();
                            Log.d("FFFFF_error", String.valueOf(t.getMessage()));
                            Toast.makeText(SubmitReservationActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_order), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_reservation);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        requestRestaurentData();
        findViewsById();

        mUpComingReservation = getIntent().getParcelableExtra(MenuActivity.UPCOMING_RESERVATION_OBJECT_KEY);
        mSelectedDate = getIntent().getStringExtra(RestaurantProfileActivity.SELECTED_DATE_KEY);
        mFormattedDate = getFormattedDate(mSelectedDate);
        mSelectedTime = getIntent().getStringExtra(RestaurantProfileActivity.SELECTED_TIME_KEY);
        mSelectedNumberOfPeople = getIntent().getStringExtra(RestaurantProfileActivity.SELECTED_NUMBER_PEOPLE_KEY);
        mReservationOrdersList = getIntent().getParcelableArrayListExtra(MenuActivity.RESERVATION_ORDERS_LIST_KEY);
        if (mReservationOrdersList != null){
            ArrayList<Product> mProducts = new ArrayList<>();
            for (int i = 0 ; i<mReservationOrdersList.size(); i++){
//                mProducts.add(Product(mReservationOrdersList.get(i).))
            }
        }


        isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(this, ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            mReservationFormLayout.setVisibility(View.VISIBLE);
//            mSignInSignUpLayout.setVisibility(View.GONE);
            notLoggedUser.setVisibility(View.GONE);

            mUserId = SharedPreferencesHelper.getSharedPreferenceInt(this, ConstantsHelper.KEY_USER_ID, -10);
            updatePersonalInformationViews();
        } else {
            mReservationFormLayout.setVisibility(View.GONE);
//            mSignInSignUpLayout.setVisibility(View.VISIBLE);
            notLoggedUser.setVisibility(View.VISIBLE);
            backText.setVisibility(View.INVISIBLE);
        }

        arrowBackClick();

        submitButtonClick();

        signInButtonClick();

        signUpButtonClick();

        createNewAccountButtonClick();

        signUpArrowBackImageViewClick();

        signInLayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInLayout();
            }
        });

        signUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpLayout();
            }
        });
    }

    private void showSignInLayout(){
        mSignInSignUpLayout.setVisibility(View.VISIBLE);
        mSignInLayout.setVisibility(View.VISIBLE);
        notLoggedUser.setVisibility(View.GONE);
    }

    private void showSignUpLayout(){
        mSignInSignUpLayout.setVisibility(View.VISIBLE);
        mSignUpLayout.setVisibility(View.VISIBLE);
        notLoggedUser.setVisibility(View.GONE);
        mSignUpArrowBackImageView.setVisibility(View.INVISIBLE);
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
            finish();
        }
    }

    //==============================================================================================
    private void signInButtonClick() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(SubmitReservationActivity.this);
                if (isNetworkOk) {
                    String email = mSignInEmailEditText.getText().toString();
                    String password = mSignInPasswordEditText.getText().toString();
                    boolean isDataValid = isSignInDataValid(email, password);
                    if (isDataValid) {
                        showLoadingIndicator();
                        signInExistingUser(email, password);
                    }
                } else {
                    Context context = SubmitReservationActivity.this;
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
                hideSoftKeyboard();
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(SubmitReservationActivity.this);
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
                    Context context = SubmitReservationActivity.this;
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
                            SharedPreferencesHelper.setSharedPreferenceInt(SubmitReservationActivity.this, ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(SubmitReservationActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            showAndUpdateReservationForm();
                        } else {
                            Context context = SubmitReservationActivity.this;
                            String message = getResources().getString(R.string.sign_in_activity_toast_message_wrong_email_or_wrong_password);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = SubmitReservationActivity.this;
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_problem_sign_in_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = SubmitReservationActivity.this;
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
                            SharedPreferencesHelper.setSharedPreferenceInt(SubmitReservationActivity.this, ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(SubmitReservationActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            showAndUpdateReservationForm();
                        } else {
                            Context context = SubmitReservationActivity.this;
                            String message = getResources().getString(R.string.sign_up_activity_toast_message_account_already_exists);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = SubmitReservationActivity.this;
                    String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = SubmitReservationActivity.this;
                String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    private void saveUserDataInSharedPreferences(String key, String value) {
        SharedPreferencesHelper.setSharedPreferenceString(SubmitReservationActivity.this, key, value);
    }

    private void showAndUpdateReservationForm() {
        mReservationFormLayout.setVisibility(View.VISIBLE);
        mSignInSignUpLayout.setVisibility(View.GONE);
        notLoggedUser.setVisibility(View.GONE);
        mUserId = SharedPreferencesHelper.getSharedPreferenceInt(this, ConstantsHelper.KEY_USER_ID, -10);
        updatePersonalInformationViews();
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
    private void requestRestaurentData(){
        int restaurentId = getIntent().getIntExtra(RESTAURENT_KEY, 0);
        RestaurentProfileWebService webService = RetrofitWebService.retrofit.create(RestaurentProfileWebService.class);
        int userId = SharedPreferencesHelper.getSharedPreferenceInt(SubmitReservationActivity.this, ConstantsHelper.KEY_USER_ID, -10);
        Call<RestaurantProfile> restaurentProfile = webService.getRestaurentProfile(restaurentId,userId);
        restaurentProfile.enqueue(new Callback<RestaurantProfile>() {
            @Override
            public void onResponse(Call<RestaurantProfile> call, Response<RestaurantProfile> response) {
                mRestaurant = response.body();
                mRestaurantId = mRestaurant.getId();
                updateRestaurantInformationViews();
                if (mRestaurant.getPromotionAmount() > 0){
                    promotionLayout.setVisibility(View.VISIBLE);
                    promotionText.setText( "-" + mRestaurant.getPromotionAmount() + "% seulement le");
                    reservationPromotionDay.setText(mRestaurant.getPromotionDay());
                }
            }

            @Override
            public void onFailure(Call<RestaurantProfile> call, Throwable t) {

            }
        });
    }
}
