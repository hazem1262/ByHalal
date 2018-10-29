package com.example.alexander.halalappv1.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.EditReservationActivity;
import com.example.alexander.halalappv1.activities.ReservationDetailsActivity;
import com.example.alexander.halalappv1.activities.RestaurantProfileActivity;
import com.example.alexander.halalappv1.model.User;
import com.example.alexander.halalappv1.model.newModels.reservation.Reservation;
import com.example.alexander.halalappv1.reservation.PreviousReservation;
import com.example.alexander.halalappv1.reservation.PreviousReservationsAdapter;
import com.example.alexander.halalappv1.reservation.ReservationObject;
import com.example.alexander.halalappv1.reservation.UpComingReservation;
import com.example.alexander.halalappv1.reservation.UpcomingReservationsAdapter;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveFragment extends Fragment implements UpcomingReservationsAdapter.UpcomingListItemClickListener, PreviousReservationsAdapter.PreviousListItemClickListener {

    public ReserveFragment() {}

    public static final String EDIT_RESERVATION_OBJECT_KEY = "Edit_Reservation_Object";

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
    private ConstraintLayout mReservationsLayout;
    private ConstraintLayout mSignInSignUpLayout;
    private boolean isLoggedIn;

    private ConstraintLayout mSignInLayout;
    private Button mCreateNewAccountButton;
    private ConstraintLayout mSignUpLayout;
    private TextView mSignUpArrowBackImageView;

    private TextView mUpcomingReservationsTextView;
    private RecyclerView mUpcomingReservationsListRecyclerView;
    private TextView mPreviousReservationsTextView;
    private RecyclerView mPreviousReservationsListRecyclerView;
    private ConstraintLayout mLoadingIndicator;
    private TextView mErrorMessageTextView;

    private void findViewsById(View rootView) {
        mUpcomingReservationsTextView = rootView.findViewById(R.id.tv_reserve_fragment_up_coming_reservations_label);
        mUpcomingReservationsListRecyclerView = rootView.findViewById(R.id.rv_reserve_fragment_up_coming_reservations_list);
        mPreviousReservationsTextView = rootView.findViewById(R.id.tv_reserve_fragment_previous_reservations_label);
        mPreviousReservationsListRecyclerView = rootView.findViewById(R.id.rv_reserve_fragment_previous_reservations_list);
        mLoadingIndicator = rootView.findViewById(R.id.pb_reserve_fragment_loading_indicator);
        mErrorMessageTextView = rootView.findViewById(R.id.tv_reserve_fragment_error_message);
        mUpcomingReservationsTextView.setVisibility(View.GONE);
        mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
        mPreviousReservationsTextView.setVisibility(View.GONE);
        mPreviousReservationsListRecyclerView.setVisibility(View.GONE);
        mSignInEmailEditText = rootView.findViewById(R.id.et_sign_in_activity_email);
        mSignInPasswordEditText = rootView.findViewById(R.id.et_sign_in_activity_password);
        mSignInButton = rootView.findViewById(R.id.btn_sign_in_activity_sign_in);
        mSignUpFirstNameEditText = rootView.findViewById(R.id.et_sign_up_activity_first_name);
        mSignUpFamilyNameEditText = rootView.findViewById(R.id.et_sign_up_activity_family_name);
        mSignUpEmailEditText = rootView.findViewById(R.id.et_sign_up_activity_email);
        mSignUpMobileNumberEditText = rootView.findViewById(R.id.et_sign_up_activity_mobile_number);
        mSignUpPasswordEditText = rootView.findViewById(R.id.et_sign_up_activity_password);
        mSignUpRetypedPasswordEditText = rootView.findViewById(R.id.et_sign_up_activity_retype_password);
        mSignUpButton = rootView.findViewById(R.id.btn_sign_up_activity_sign_up);
        mReservationsLayout = rootView.findViewById(R.id.layout_reserve_fragment_reservations);
        mSignInSignUpLayout = rootView.findViewById(R.id.layout_reserve_fragment_sign_in_sign_up);

        mSignInLayout = rootView.findViewById(R.id.layout_sign_in);
        mCreateNewAccountButton = rootView.findViewById(R.id.btn_sign_in_activity_create_naw_account);
        mSignUpLayout = rootView.findViewById(R.id.layout_sign_up);
        mSignUpArrowBackImageView = rootView.findViewById(R.id.iv_sign_up_arrow_back);
    }
    //==============================================================================================
    private UpcomingReservationsAdapter mUpcomingReservationsAdapter;
    private ArrayList<Reservation> mUpComingReservationsList;
    private void setUpUpcomingReservationsRecyclerView() {
        LinearLayoutManager upcomingLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mUpcomingReservationsListRecyclerView.setLayoutManager(upcomingLayoutManager);
        mUpcomingReservationsAdapter = new UpcomingReservationsAdapter(getContext(), this);
    }
    //==============================================================================================
    private PreviousReservationsAdapter mPreviousReservationsAdapter;
    private ArrayList<Reservation> mPreviousReservationsList;
    private void setUPPreviousReservationsRecyclerView() {
        LinearLayoutManager previousLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mPreviousReservationsListRecyclerView.setLayoutManager(previousLayoutManager);
        mPreviousReservationsAdapter = new PreviousReservationsAdapter(getContext(), this);
    }
    //==============================================================================================
    private ReservationObject mReservations;
    private boolean isNetworkOk;

    private void getReservations() {
        int userId = SharedPreferencesHelper.getSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, -10);
        if (userId != -10) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<ReservationObject> reservationsCall = webService.getReservations(userId);
            reservationsCall.enqueue(new Callback<ReservationObject>() {
                @Override
                public void onResponse(Call<ReservationObject> call, Response<ReservationObject> response) {
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        mReservations = response.body();
                        if (mReservations != null) {
                            mUpComingReservationsList = (ArrayList<Reservation>) mReservations.getUpComingReservations();
                            if (mUpComingReservationsList != null && mUpComingReservationsList.size() > 0) {
                                mUpcomingReservationsTextView.setVisibility(View.VISIBLE);
                                mUpcomingReservationsListRecyclerView.setVisibility(View.VISIBLE);

                                mUpcomingReservationsAdapter.setUpComingReservationsList(mUpComingReservationsList);
                                mUpcomingReservationsListRecyclerView.setAdapter(mUpcomingReservationsAdapter);
                            } else {
                                mUpcomingReservationsTextView.setVisibility(View.GONE);
                                mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
                            }
                            //======================================================================
                            mPreviousReservationsList = (ArrayList<Reservation>) mReservations.getPreviousReservations();
                            if (mPreviousReservationsList != null && mPreviousReservationsList.size() > 0) {
                                mPreviousReservationsTextView.setVisibility(View.VISIBLE);
                                mPreviousReservationsListRecyclerView.setVisibility(View.VISIBLE);

                                mPreviousReservationsAdapter.setPreviousReservationsList(mPreviousReservationsList);
                                mPreviousReservationsListRecyclerView.setAdapter(mPreviousReservationsAdapter);
                            } else {
                                mPreviousReservationsTextView.setVisibility(View.GONE);
                                mPreviousReservationsListRecyclerView.setVisibility(View.GONE);
                            }
                            //======================================================================
                            if ((mUpComingReservationsList == null || mUpComingReservationsList.size() == 0)
                                    && (mPreviousReservationsList == null || mPreviousReservationsList.size() == 0)) {
                                mUpcomingReservationsTextView.setVisibility(View.GONE);
                                mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
                                mPreviousReservationsTextView.setVisibility(View.GONE);
                                mPreviousReservationsListRecyclerView.setVisibility(View.GONE);

                                mErrorMessageTextView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mUpcomingReservationsTextView.setVisibility(View.GONE);
                            mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
                            mPreviousReservationsTextView.setVisibility(View.GONE);
                            mPreviousReservationsListRecyclerView.setVisibility(View.GONE);

                            mErrorMessageTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mUpcomingReservationsTextView.setVisibility(View.GONE);
                        mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
                        mPreviousReservationsTextView.setVisibility(View.GONE);
                        mPreviousReservationsListRecyclerView.setVisibility(View.GONE);

                        mErrorMessageTextView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ReservationObject> call, Throwable t) {
                    hideLoadingIndicator();
                    mUpcomingReservationsTextView.setVisibility(View.GONE);
                    mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
                    mPreviousReservationsTextView.setVisibility(View.GONE);
                    mPreviousReservationsListRecyclerView.setVisibility(View.GONE);
                    mErrorMessageTextView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void cancelReservation(int reservationId) {
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<JsonObject> cancelReservationCall = webService.cancelReservation(reservationId);
        cancelReservationCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    String data = jsonObject.get("data").getAsString();
                    if (data != null) {
                        if (data.equals("success")) {
                            getReservations();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoadingIndicator();
                Log.d("XXXXX", t.getMessage());
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reserve, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViewsById(rootView); // (1)

        setUpUpcomingReservationsRecyclerView(); // (2)

        setUPPreviousReservationsRecyclerView(); // (3)

        isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            mReservationsLayout.setVisibility(View.VISIBLE);
            mSignInSignUpLayout.setVisibility(View.GONE);

            isNetworkOk = NetworkHelper.hasNetworkAccess(getContext());
            if (isNetworkOk) {
                showLoadingIndicator(); // (4)

                getReservations(); // (5)
            } else {
                mUpcomingReservationsTextView.setVisibility(View.GONE);
                mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
                mPreviousReservationsTextView.setVisibility(View.GONE);
                mPreviousReservationsListRecyclerView.setVisibility(View.GONE);

                mErrorMessageTextView.setVisibility(View.VISIBLE);
                mErrorMessageTextView.setText(getResources().getString(R.string.toast_message_no_internet_connection));
            }
        } else {
            mReservationsLayout.setVisibility(View.GONE);
            mSignInSignUpLayout.setVisibility(View.VISIBLE);
        }
        //==========================================================================================
        signInButtonClick();

        signUpButtonClick();

        createNewAccountButtonClick();

        signUpArrowBackImageViewClick();

        return rootView;
    }

    private void showOrderDialog(final Reservation previousReservation, final Reservation upComingReservation) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_reserve, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        //==========================================================================================
        ConstraintLayout dialogLayout = dialogView.findViewById(R.id.reserve_dialog_layout);
        View lineSeparator = dialogView.findViewById(R.id.reserve_dialog_line);
        ImageView restaurantImage = dialogView.findViewById(R.id.iv_reserve_alert_dialog_restaurant_image);
        TextView restaurantName = dialogView.findViewById(R.id.tv_reserve_alert_dialog_restaurant_name);
        TextView restaurantLocation = dialogView.findViewById(R.id.tv_reserve_alert_dialog_restaurant_location);
        TextView numberOfPeople = dialogView.findViewById(R.id.tv_reserve_alert_dialog_number_of_people);
        TextView reserveDate = dialogView.findViewById(R.id.tv_reserve_alert_dialog_date);
        TextView reserveTime = dialogView.findViewById(R.id.tv_reserve_alert_dialog_time);
        LinearLayout reserveActionsLayout = dialogView.findViewById(R.id.reserve_alert_dialog_actions_layout);
        Button cancelReservationButton = dialogView.findViewById(R.id.btn_reserve_alert_dialog_cancel);
        Button editReservationButton = dialogView.findViewById(R.id.btn_reserve_alert_dialog_edit);
        Button callButton = dialogView.findViewById(R.id.btn_reserve_alert_dialog_call);
        //==========================================================================================
        String language = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_SELECTED_LANGUAGE, null);

        if (previousReservation != null) {
            Picasso.with(getContext()).load(previousReservation.getPicture()).into(restaurantImage);
            restaurantName.setText(previousReservation.getRestaurantName());
//            restaurantLocation.setText(previousReservation.getRestaurant().getAddress() + ", " + previousReservation.getRestaurant().getCityNameEn());

            if (language != null) {
                if (language.equals("français")) {
                    numberOfPeople.setText(previousReservation.getGuests() + " personnes");
                    String[] dateParts = previousReservation.getDate().split("/");
                    String reservationDate = dateParts[2] + "/" + dateParts[1];
//                    reserveDate.setText(getDayNameFrench(previousReservation.getBookingData().getReservationDayName()) + ", " + reservationDate);
                    reserveDate.setText(previousReservation.getDate());
                    reserveTime.setText(previousReservation.getTime());
                } else {
                    numberOfPeople.setText(previousReservation.getGuests() + " people");
                    String[] dateParts = previousReservation.getDate().split("/");
                    String reservationDate = dateParts[2] + "/" + dateParts[1];
                    reserveDate.setText(previousReservation.getDate());
                    reserveTime.setText(previousReservation.getTime());
                }
            }

            reserveActionsLayout.setVisibility(View.GONE);
            lineSeparator.setVisibility(View.GONE);
            dialogLayout.setPadding(0, 0, 0, dpToPx(16));

            restaurantImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RestaurantProfileActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
//                    intent.putExtra(ConstantsHelper.RESTAURANT_OBJECT_KEY, previousReservation.getRestaurant());
//                    startActivity(intent);
                }
            });
        }

        if (upComingReservation != null) {
            Picasso.with(getContext()).load(upComingReservation.getPicture()).into(restaurantImage);
            restaurantName.setText(upComingReservation.getRestaurantName());
//            restaurantLocation.setText(upComingReservation.getRestaurant().getAddress() + ", " + upComingReservation.getRestaurant().getCityNameEn());

            if (language != null) {
                if (language.equals("français")) {
                    numberOfPeople.setText(upComingReservation.getGuests() + " personnes");
                    String[] dateParts = upComingReservation.getDate().split("/");
                    String reservationDate = dateParts[2] + "/" + dateParts[1];
                    reserveDate.setText(upComingReservation.getDate());
                    reserveTime.setText(upComingReservation.getTime());
                }
                else {
                    numberOfPeople.setText(upComingReservation.getGuests() + " people");
                    String[] dateParts = upComingReservation.getDate().split("/");
                    String reservationDate = dateParts[2] + "/" + dateParts[1];
                    reserveDate.setText(upComingReservation.getDate());
                    reserveTime.setText(upComingReservation.getTime());
                }
            }

            reserveActionsLayout.setVisibility(View.VISIBLE);
            //======================================================================================
            restaurantImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RestaurantProfileActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
                    // todo ask for restaurent id to make image clickable
//                    intent.putExtra(ConstantsHelper.RESTAURANT_OBJECT_KEY, upComingReservation.getRestaurant());
//                    startActivity(intent);
                }
            });
            //======================================================================================
            cancelReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCancelAlertDialog(alertDialog, upComingReservation);
                }
            });
            //======================================================================================
            editReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditReservationActivity.class);
                    intent.putExtra(EDIT_RESERVATION_OBJECT_KEY, upComingReservation.getBookingId()); // use booking id to edit reservation
                    startActivity(intent);
                }
            });
            //======================================================================================
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo update ui
//                    showCallAlertDialog(alertDialog, upComingReservation.getRestaurant().getPhone());
                }
            });
        }
    }

    private String getDayNameFrench(String dayNameEnglish) {
        if (dayNameEnglish.equalsIgnoreCase("Sat")) {
            return "sam";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Sun")) {
            return "dim";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Mon")) {
            return "lun";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Tue")) {
            return "mar";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Wed")) {
            return "mer";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Thu")) {
            return "jeu";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Fri")) {
            return "ven";
        }
        else {
            return "";
        }
    }

    private int dpToPx(float dp) {
        float density = getActivity().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void showCancelAlertDialog(final AlertDialog alertDialog, final Reservation upComingReservation) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_cancel_reservation, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog cancelAlertDialog = dialogBuilder.create();
        cancelAlertDialog.show();

        TextView cancelTextView = dialogView.findViewById(R.id.tv_cancel_reservation_alert_dialog_action_cancel);
        TextView okTextView = dialogView.findViewById(R.id.tv_cancel_reservation_alert_dialog_action_ok);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlertDialog.dismiss();
            }
        });

        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlertDialog.dismiss();
                alertDialog.dismiss();
                showLoadingIndicator();
                mUpcomingReservationsTextView.setVisibility(View.GONE);
                mUpcomingReservationsAdapter.setUpComingReservationsList(null);
                mUpcomingReservationsListRecyclerView.setAdapter(mUpcomingReservationsAdapter);

                mPreviousReservationsTextView.setVisibility(View.GONE);
                mPreviousReservationsAdapter.setPreviousReservationsList(null);
                mPreviousReservationsListRecyclerView.setAdapter(mPreviousReservationsAdapter);

                cancelReservation(upComingReservation.getBookingId());
            }
        });
    }

    private void showCallAlertDialog(final AlertDialog alertDialog, final String phoneNumber) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_make_call, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog callAlertDialog = dialogBuilder.create();
        callAlertDialog.show();

        TextView phoneNumberTextView = dialogView.findViewById(R.id.tv_call_alert_dialog_phone_number);
        TextView cancelTextView = dialogView.findViewById(R.id.tv_call_alert_dialog_action_cancel);
        TextView callTextView = dialogView.findViewById(R.id.tv_call_alert_dialog_action_call);

        phoneNumberTextView.setText(phoneNumber);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAlertDialog.dismiss();
            }
        });

        callTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAlertDialog.dismiss();
                alertDialog.dismiss();
                String formattedPhoneNumber = String.format("tel: %s", phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(formattedPhoneNumber));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onUpcomingItemClick(int position) {
        // todo replace dialog with activity
//        showOrderDialog(null, mUpComingReservationsList.get(position));
        Intent detailsIntent = new Intent(getContext(), ReservationDetailsActivity.class);
        detailsIntent.putExtra(EDIT_RESERVATION_OBJECT_KEY, mUpComingReservationsList.get(position).getBookingId()); // use booking id to edit reservation

        startActivity(detailsIntent);
    }

    @Override
    public void onPreviousItemClick(int position) {
        showOrderDialog(mPreviousReservationsList.get(position), null);
    }
    //==============================================================================================
    private void signInButtonClick() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(getContext());
                if (isNetworkOk) {
                    String email = mSignInEmailEditText.getText().toString();
                    String password = mSignInPasswordEditText.getText().toString();
                    boolean isDataValid = isSignInDataValid(email, password);
                    if (isDataValid) {
                        showLoadingIndicator();
                        signInExistingUser(email, password);
                    }
                } else {
                    Context context = getContext();
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
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(getContext());
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
                    Context context = getContext();
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
            Toast.makeText(getContext(), getResources().getString(R.string.sign_in_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_in_activity_toast_message_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_in_activity_toast_message_password_is_required), Toast.LENGTH_LONG).show();
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
                hideSoftKeyboard();
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
                            SharedPreferencesHelper.setSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            reloadReserveFragment();
                        } else {
                            Context context = getContext();
                            String message = getResources().getString(R.string.sign_in_activity_toast_message_wrong_email_or_wrong_password);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = getContext();
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_problem_sign_in_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = getContext();
                String message = getResources().getString(R.string.sign_in_activity_toast_message_problem_sign_in_try_again_later);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    private boolean isSignUpDataValid(String firstName, String familyName, String email, String mobileNumber, String password, String retypedPassword) {
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_first_name_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(familyName)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_family_name_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mobileNumber)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_mobile_number_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! Pattern.matches("[a-zA-Z]+", mobileNumber) && ! (mobileNumber.length() == 10)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_invalid_mobile_number), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_password_is_at_least_6_chars), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(retypedPassword)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_retyped_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! password.equals(retypedPassword)) {
            Toast.makeText(getContext(), getResources().getString(R.string.sign_up_activity_toast_message_password_and_retyped_password_do_not_match), Toast.LENGTH_LONG).show();
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
                hideSoftKeyboard();
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
                            SharedPreferencesHelper.setSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            reloadReserveFragment();
                        } else {
                            Context context = getContext();
                            String message = getResources().getString(R.string.sign_up_activity_toast_message_account_already_exists);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = getContext();
                    String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = getContext();
                String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    private void saveUserDataInSharedPreferences(String key, String value) {
        SharedPreferencesHelper.setSharedPreferenceString(getContext(), key, value);
    }

    private void reloadReserveFragment() {
        isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            mReservationsLayout.setVisibility(View.VISIBLE);
            mSignInSignUpLayout.setVisibility(View.GONE);

            isNetworkOk = NetworkHelper.hasNetworkAccess(getContext());
            if (isNetworkOk) {
                showLoadingIndicator(); // (4)

                getReservations(); // (5)
            } else {
                mUpcomingReservationsTextView.setVisibility(View.GONE);
                mUpcomingReservationsListRecyclerView.setVisibility(View.GONE);
                mPreviousReservationsTextView.setVisibility(View.GONE);
                mPreviousReservationsListRecyclerView.setVisibility(View.GONE);

                mErrorMessageTextView.setVisibility(View.VISIBLE);
                mErrorMessageTextView.setText(getResources().getString(R.string.toast_message_no_internet_connection));
            }
        } else {
            mReservationsLayout.setVisibility(View.GONE);
            mSignInSignUpLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
