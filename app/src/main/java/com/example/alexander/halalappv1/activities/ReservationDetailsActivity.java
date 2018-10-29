package com.example.alexander.halalappv1.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.newModels.reservation.details.Product;
import com.example.alexander.halalappv1.model.newModels.reservation.details.ReservationDetails;
import com.example.alexander.halalappv1.model.newModels.reservation.details.ReservationDetailsAllResponse;
import com.example.alexander.halalappv1.services.ReservationDetailsResponse;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.activities.RestaurantProfileActivity.RESTAURANT_ID_KEY;
import static com.example.alexander.halalappv1.activities.RestaurantProfileActivity.RESTAURANT_NAME_KEY;
import static com.example.alexander.halalappv1.fragments.ReserveFragment.EDIT_RESERVATION_OBJECT_KEY;

public class ReservationDetailsActivity extends AppCompatActivity {

    private TextView restaurentName;
    private TextView restaurentAdress;
    private TextView reservationDate;
    private TextView reservationTime;
    private TextView reservationNumberOfPeople;
    private ImageView restaurantImage;
    private Button cancelButton;
    private Button editButton;
    private Button callButton;
    private TextView restaurantDescription;
    private TextView cuisineValue;
    private TextView chef;
    private TextView certficaionAlcohol;
    private LinearLayout menuLayout;

    private ReservationDetailsAllResponse mReservationDetailsResponse;
    private ArrayList<Product> mProducts;

    private ReservationDetails mReservationDetails;
    private com.example.alexander.halalappv1.model.newModels.reservation.details.Restaurant mRestaurantRes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);
        findViewsById();
        getData();
    }

    private void findViewsById(){
        restaurentName = findViewById(R.id.restaurant_name);
        restaurentAdress = findViewById(R.id.restaurantAdress);
        reservationDate = findViewById(R.id.tv_upcoming_reservation_date);
        reservationTime = findViewById(R.id.tv_upcoming_reservation_details);
        reservationNumberOfPeople = findViewById(R.id.reservation_persons);
        restaurantImage = findViewById(R.id.restaurant_image);
        cancelButton = findViewById(R.id.cancelBtn);
        callButton = findViewById(R.id.callBtn);
        editButton = findViewById(R.id.editBtn);
        restaurantDescription = findViewById(R.id.tv_restaurant_profile_description);
        cuisineValue = findViewById(R.id.tv_cuisine_value);
        chef = findViewById(R.id.tv_executive_chef_value);
        certficaionAlcohol = findViewById(R.id.tv_wine_value);
        menuLayout = findViewById(R.id.menu_layout);

    }

    private void getData(){
        int reservationId = getIntent().getIntExtra(EDIT_RESERVATION_OBJECT_KEY, 0);
        ReservationDetailsResponse webService = RetrofitWebService.retrofit.create(ReservationDetailsResponse.class);
        Call<ReservationDetailsAllResponse> menuResponse = webService.getReservationDetails(reservationId);
        menuResponse.enqueue(new Callback<ReservationDetailsAllResponse>() {
            @Override
            public void onResponse(Call<ReservationDetailsAllResponse> call, Response<ReservationDetailsAllResponse> response) {
                mReservationDetailsResponse = response.body();
                mProducts = mReservationDetailsResponse.getReservation().getProducts();
                mRestaurantRes = mReservationDetailsResponse.getRestaurant();
                mReservationDetails = mReservationDetailsResponse.getReservation();
                updateData();

            }

            @Override
            public void onFailure(Call<ReservationDetailsAllResponse> call, Throwable t) {

            }
        });
    }

    private void updateData(){
        restaurentName.setText(mRestaurantRes.getName());
        restaurentAdress.setText(mRestaurantRes.getAddress());
        reservationDate.setText(mReservationDetails.getDate());
        reservationTime.setText(mReservationDetails.getTime().replace("h",":"));
        reservationNumberOfPeople.setText(mReservationDetails.getGuests() + " Persons");
        Picasso.with(this).load(mRestaurantRes.getPicture()).into(restaurantImage);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reservationId = getIntent().getIntExtra(EDIT_RESERVATION_OBJECT_KEY, 0);
                Intent intent = new Intent(ReservationDetailsActivity.this, EditReservationActivity.class);
                intent.putExtra(EDIT_RESERVATION_OBJECT_KEY, reservationId); // use booking id to edit reservation
                startActivity(intent);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo update ui
                    showCallAlertDialog(mRestaurantRes.getPhone());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelAlertDialog(mReservationDetails.getId());
            }
        });
        restaurantDescription.setText(mRestaurantRes.getDescription());
        cuisineValue.setText(mRestaurantRes.getCuisineName());
        chef.setText(mRestaurantRes.getChef());
        certficaionAlcohol.setText(mRestaurantRes.getCertification() + "/" + mRestaurantRes.getAlcohol());
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantName = mRestaurantRes.getName();
                Intent intent = new Intent(ReservationDetailsActivity.this, MenuActivity.class);
                intent.putExtra(RESTAURANT_NAME_KEY, restaurantName);
                intent.putExtra(RESTAURANT_ID_KEY, mRestaurantRes.getId());
                startActivity(intent);
            }
        });
    }

    private void showCancelAlertDialog(final int upComingReservationId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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
//                showLoadingIndicator();

                cancelReservation(upComingReservationId);
            }
        });
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
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                hideLoadingIndicator();
                Log.d("XXXXX", t.getMessage());
            }
        });
    }

    private void showCallAlertDialog(final String phoneNumber) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ReservationDetailsActivity.this);
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
                String formattedPhoneNumber = String.format("tel: %s", phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(formattedPhoneNumber));
                if (intent.resolveActivity(ReservationDetailsActivity.this.getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }



}
