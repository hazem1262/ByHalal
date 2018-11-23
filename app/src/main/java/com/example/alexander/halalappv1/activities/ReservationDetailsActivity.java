package com.example.alexander.halalappv1.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.RestaurantMenuAdapter;
import com.example.alexander.halalappv1.model.newModels.menues.MenuItem;
import com.example.alexander.halalappv1.model.newModels.reservation.details.Product;
import com.example.alexander.halalappv1.model.newModels.reservation.details.ReservationDetails;
import com.example.alexander.halalappv1.model.newModels.reservation.details.ReservationDetailsAllResponse;
import com.example.alexander.halalappv1.services.ReservationDetailsResponse;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private ExpandableListView mMenuListView;
    private RestaurantMenuAdapter mMenuAdapter;
    private LinearLayout containner;
    private ScrollView scrollLayout;
    private LinearLayout btnLayout;
    private TextView backText;
    private ReservationDetailsAllResponse mReservationDetailsResponse;
    private ArrayList<Product> mProducts;
    private ConstraintLayout loadingLayout;
    private ReservationDetails mReservationDetails;
    private com.example.alexander.halalappv1.model.newModels.reservation.details.Restaurant mRestaurantRes;
    FrameLayout headerLayout;
    LinearLayout restaurantInfoLayout;
    RelativeLayout orderAmount;
    private List<String> mListDataHeader;
    private HashMap<String, List<MenuItem>> mListDataChild;
    TextView totalCost;
    private LinearLayout discountPromotionLayout;
    private TextView reservationDetailsAmount;
    private TextView restaurentPhone;
    private LinearLayout mWebsiteLayout;
    private LinearLayout mPhoneNumberLayout;
    double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);

        findViewsById();
        getData();
        //set menu list
        mMenuListView = findViewById(R.id.elv_menu_activity_menu_list);
        mMenuListView.setChildDivider(getResources().getDrawable(R.color.modifiedPrimaryColor));
        mListDataHeader = new ArrayList<>();
        mListDataChild = new HashMap<>();
        if (getIntent().getAction() != null){
            if (getIntent().getAction().equals("PreviousReservation")){
                btnLayout.setVisibility(View.GONE);
            }
        }
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPhoneNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = String.format("tel: %s", mRestaurantRes.getPhone());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(phoneNumber));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        mWebsiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(mRestaurantRes.getWebsite())));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    // setup the menu list
    private void setUpMenuListView(){
        String header = "Ce que j’ai commandé";
        if (mReservationDetails.getProducts().size() > 0){
            mListDataHeader.add(header);
        }
        ArrayList<MenuItem> menuItemsList = new ArrayList<>();
        for (int i = 0; i< mReservationDetails.getProducts().size(); i++ ){
            Product p = mReservationDetails.getProducts().get(i);
            MenuItem menuItem = new MenuItem(p.getId(), p.getName(), p.getQuantity().toString(), p.getPrice());
            menuItem.setQuantity(p.getQuantity());
            menuItemsList.add(menuItem);
            total += (menuItem.getQuantity() * Double.parseDouble(menuItem.getPrice()));
        }
        mListDataChild.put(header, menuItemsList);
        mMenuAdapter = new RestaurantMenuAdapter(this, mListDataHeader, mListDataChild, false);
        mMenuListView.setAdapter(mMenuAdapter);
        if (menuItemsList.size()>0){
            mMenuListView.expandGroup(0);
            totalCost.setText(total + " €");
        }else{
            orderAmount.setVisibility(View.INVISIBLE);
        }


    }
    private void findViewsById(){
        discountPromotionLayout = findViewById(R.id.discountPromotionLayout);
        reservationDetailsAmount = findViewById(R.id.reservationDetailsAmount);
        restaurentPhone = findViewById(R.id.tv_restaurant_call);
        mPhoneNumberLayout = findViewById(R.id.restaurant_profile_phone);
        mWebsiteLayout = findViewById(R.id.restaurant_profile_website_layout);
        totalCost = findViewById(R.id.totalCost);
        orderAmount = findViewById(R.id.orderAmount);
        headerLayout = findViewById(R.id.upcoming_image_date_layout);
        loadingLayout = findViewById(R.id.pb_reserve_fragment_loading_indicator);
        backText = findViewById(R.id.backText);
        btnLayout = findViewById(R.id.btnLayout);
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
        containner = findViewById(R.id.containnerLayout);
        scrollLayout = findViewById(R.id.scrollLayout);
        restaurantInfoLayout = findViewById(R.id.restaurant_profile_information_layout);
    }

    private void getData(){
        showLoading();
        int reservationId = getIntent().getIntExtra(EDIT_RESERVATION_OBJECT_KEY, 0);
        ReservationDetailsResponse webService = RetrofitWebService.retrofit.create(ReservationDetailsResponse.class);
        Call<ReservationDetailsAllResponse> menuResponse = webService.getReservationDetails(reservationId);
        menuResponse.enqueue(new Callback<ReservationDetailsAllResponse>() {
            @Override
            public void onResponse(Call<ReservationDetailsAllResponse> call, Response<ReservationDetailsAllResponse> response) {
                hideLoading();
                mReservationDetailsResponse = response.body();
                mProducts = mReservationDetailsResponse.getReservation().getProducts();
                mRestaurantRes = mReservationDetailsResponse.getRestaurant();
                mReservationDetails = mReservationDetailsResponse.getReservation();
                updateData();
                if (!mReservationDetails.getPromotionText().isEmpty()){
                    discountPromotionLayout.setVisibility(View.VISIBLE);
                    reservationDetailsAmount.setText(mReservationDetails.getPromotionText());
                }

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
        reservationTime.setText(mReservationDetails.getTime());
        reservationNumberOfPeople.setText(mReservationDetails.getGuests() + " Personnes");
        restaurentPhone.setText(mRestaurantRes.getPhone());
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

        // setup the list view
        setUpMenuListView();
        containner.invalidate();
        // handle the change in height to linear layout
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(containner.getWidth(), containner.getHeight() + (200 + mReservationDetails.getProducts().size() * 200));
        containner.setLayoutParams(parms);
        restaurantImage.requestFocus();
        scrollLayout.smoothScrollTo(0,0);
    }

    private void showCancelAlertDialog(final int upComingReservationId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_cancel_reservation, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog cancelAlertDialog = dialogBuilder.create();
        cancelAlertDialog.show();
        //todo
        cancelAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

                            showConfirmDeletionDialog();
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
        callAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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


    private void showConfirmDeletionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ReservationDetailsActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.confirm_cancellation, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog cancelAlertDialog = dialogBuilder.create();
        cancelAlertDialog.show();
        cancelAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView okTextView = dialogView.findViewById(R.id.tv_cancel_reservation_alert_dialog_action_ok);



        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlertDialog.dismiss();
                finish();
            }
        });
    }

    private void showLoading(){
        loadingLayout.setVisibility(View.VISIBLE);
        headerLayout.setVisibility(View.GONE);
        btnLayout.setVisibility(View.GONE);
        orderAmount.setVisibility(View.INVISIBLE);
        restaurantInfoLayout.setVisibility(View.GONE);
    }

    private void hideLoading(){
        loadingLayout.setVisibility(View.GONE);
        headerLayout.setVisibility(View.VISIBLE);
        btnLayout.setVisibility(View.VISIBLE);
        orderAmount.setVisibility(View.VISIBLE);
        restaurantInfoLayout.setVisibility(View.VISIBLE);
    }
}
