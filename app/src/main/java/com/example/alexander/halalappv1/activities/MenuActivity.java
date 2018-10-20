package com.example.alexander.halalappv1.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.RestaurantMenuAdapter;
import com.example.alexander.halalappv1.model.ReservationOrder;
	import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.model.newModels.RestaurantProfile;
import com.example.alexander.halalappv1.model.newModels.menues.MenuItem;
import com.example.alexander.halalappv1.model.newModels.menues.MenuResponse;
import com.example.alexander.halalappv1.reservation.UpComingReservation;
import com.example.alexander.halalappv1.services.MenuResponseWebService;
import com.example.alexander.halalappv1.services.RetrofitWebService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.fragments.HomeFragment.RESTAURENT_KEY;

public class MenuActivity extends AppCompatActivity {

    public static final String UPCOMING_RESERVATION_OBJECT_KEY = "UpcomingReservationObject";
    public static final String RESERVATION_ORDERS_LIST_KEY = "ReservationOrdersList";

    private TextView mRestaurantNameTextView;
    private ImageView mArrowBackImageView;
    private ExpandableListView mMenuListView;

    private ConstraintLayout mCheckoutOrderLayout;
    private TextView mTotalQuantityTextView;
    private TextView mTotalPriceTextView;

    private RestaurantMenuAdapter mMenuAdapter;
    private List<String> mListDataHeader;
    private HashMap<String, List<MenuItem>> mListDataChild;
    private int mLastExpandedPosition = -1;

    private UpComingReservation mUpComingReservation;
    private Restaurant mRestaurant;
    private String mRestaurantName;
    private String mSelectedDate;
    private String mSelectedTime;
    private String mSelectedNumberOfPeople;
    private List<MenuResponse> mMenuList;

    private int mTotalQuantity = 0;
    private double mTotalPrice = 0.00;
    private String mAction;

    private ArrayList<ReservationOrder> mReservationOrdersList = new ArrayList<>();
    //==============================================================================================
    private void updateRestaurantNameView() {
        mRestaurantNameTextView.setText(mRestaurantName);
    }

    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpMenuListView() {
        for (int i = 0; i < mMenuList.size(); i ++) {
            mListDataHeader.add(mMenuList.get(i).getName());
            mListDataChild.put(mMenuList.get(i).getName(), mMenuList.get(i).getMenuItems());
        }

        mMenuAdapter = new RestaurantMenuAdapter(this, mListDataHeader, mListDataChild);
        mMenuListView.setAdapter(mMenuAdapter);

        for (int i = 0; i < mMenuList.size(); i ++){
            mMenuListView.expandGroup(i);
        }
        mMenuListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (mLastExpandedPosition != -1 && groupPosition != mLastExpandedPosition) {
                    mMenuListView.collapseGroup(mLastExpandedPosition);
                }
                mLastExpandedPosition = groupPosition;
            }
        });

        if (mAction != null) {
            if (mAction.equals(RestaurantProfileActivity.ACTION_RESERVE)) {
                mMenuListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        MenuItem menuItem = mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition);
                        showOrderDialog(menuItem, mListDataHeader.get(groupPosition));
                        return false;
                    }
                });
            }

            else if (mAction.equals(EditReservationActivity.ACTION_EDIT_RESERVE)) {
                mMenuListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        MenuItem menuItem = mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition);
                        showOrderDialog(menuItem, mListDataHeader.get(groupPosition));
                        return false;
                    }
                });
            }
        }
    }

    private void showOrderDialog(final MenuItem menuItem, final String menuHeader) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_menu_order, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        //==========================================================================================
        TextView dishNameTextView = dialogView.findViewById(R.id.tv_dialog_dish_name);
        final TextView dishPriceTextView = dialogView.findViewById(R.id.tv_dialog_dish_price);
        final TextView quantityTextView = dialogView.findViewById(R.id.tv_dialog_quantity_value);
        ImageView plusSignImageView = dialogView.findViewById(R.id.iv_dialog_plus_sign);
        ImageView minusSignImageView = dialogView.findViewById(R.id.iv_dialog_minus_sign);
        TextView addTextView = dialogView.findViewById(R.id.tv_dialog_add);
        //==========================================================================================
        dishNameTextView.setText(menuItem.getName());
        dishPriceTextView.setText(String.valueOf(menuItem.getPrice()) + " €");
//        quantityTextView.setText(String.valueOf(menuItem.getQuantity()));
        //==========================================================================================
        plusSignImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(quantityTextView.getText().toString());
                quantity ++;
                if (quantity > 0) {
//                    menuItem.setQuantity(quantity);
                    quantityTextView.setText(String.valueOf(quantity));
                    dishPriceTextView.setText(String.valueOf(quantity * Double.parseDouble(menuItem.getPrice())) + " €");

                    int x = 0;
                    x ++;

                    mTotalQuantity += 1;
                    mTotalPrice += x * Double.parseDouble(menuItem.getPrice());
                }
            }
        });
        //==========================================================================================
        minusSignImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(quantityTextView.getText().toString());
                quantity --;
                if (quantity <= 0) {
                    quantity = 0;
                }
                if (quantity > 0) {
//                    menuItem.setQuantity(quantity);
                    quantityTextView.setText(String.valueOf(quantity));
                    dishPriceTextView.setText(String.valueOf(quantity * Double.parseDouble(menuItem.getPrice())) + " €");

                    int x = 0;
                    x ++;

                    mTotalQuantity -= 1;
                    mTotalPrice -= x * Double.parseDouble(menuItem.getPrice());
                }
            }
        });
        //==========================================================================================
        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuAdapter.notifyDataSetChanged();
                if (Integer.parseInt(quantityTextView.getText().toString()) > 0) {
                    boolean isTagged = isItemTagged(menuItem);
                    if (isTagged) {
                        for (int i = 0; i < mReservationOrdersList.size(); i ++) {
                            if (mReservationOrdersList.get(i).getId() == menuItem.getId()) {
                                ReservationOrder reservationOrder = mReservationOrdersList.get(i);
                                reservationOrder.setId(menuItem.getId());
                                reservationOrder.setQuantity(Integer.parseInt(quantityTextView.getText().toString()));
                            }
                        }
                    } else {
                        ReservationOrder reservationOrder = new ReservationOrder();
                        reservationOrder.setId(menuItem.getId());
                        reservationOrder.setQuantity(Integer.parseInt(quantityTextView.getText().toString()));
                        mReservationOrdersList.add(reservationOrder);
                    }

                    mTotalQuantityTextView.setText(String.valueOf("x" + mTotalQuantity));
                    mTotalPriceTextView.setText(String.valueOf(mTotalPrice + " €"));
                }

                alertDialog.dismiss();
            }
        });
    }

    private boolean isItemTagged(MenuItem menuItem) {
        for (int i = 0; i < mReservationOrdersList.size(); i ++) {
            if (mReservationOrdersList.get(i).getId() == menuItem.getId()) {
                return true;
            }
        }
        return false;
    }

    private void checkoutOrderLayoutClick() {
        mCheckoutOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SubmitReservationActivity.class);
                intent.putExtra(UPCOMING_RESERVATION_OBJECT_KEY, mUpComingReservation);
                intent.putExtra(RestaurantProfileActivity.RESTAURANT_OBJECT_KEY, mRestaurant);
                intent.putExtra(RestaurantProfileActivity.SELECTED_DATE_KEY, mSelectedDate);
                intent.putExtra(RestaurantProfileActivity.SELECTED_TIME_KEY, mSelectedTime);
                intent.putExtra(RestaurantProfileActivity.SELECTED_NUMBER_PEOPLE_KEY, mSelectedNumberOfPeople);
                intent.putExtra(RESERVATION_ORDERS_LIST_KEY, mReservationOrdersList);
                startActivity(intent);
            }
        });
    }
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mRestaurantNameTextView = findViewById(R.id.tv_menu_activity_restaurant_name);
        mArrowBackImageView = findViewById(R.id.iv_menu_activity_arrow_back);
        mCheckoutOrderLayout = findViewById(R.id.checkout_order_layout);
        mTotalQuantityTextView = findViewById(R.id.tv_checkout_order_total_quantity);
        mTotalPriceTextView = findViewById(R.id.tv_checkout_order_total_price);

        mMenuListView = findViewById(R.id.elv_menu_activity_menu_list);
        mMenuListView.setChildDivider(getResources().getDrawable(R.color.modifiedPrimaryColor));
        mListDataHeader = new ArrayList<>();
        mListDataChild = new HashMap<>();


        mAction = getIntent().getAction();
        if (mAction != null) {

            if (mAction.equals(RestaurantProfileActivity.ACTION_RESERVE)) {
                mCheckoutOrderLayout.setVisibility(View.VISIBLE);
                mRestaurant = getIntent().getParcelableExtra(RestaurantProfileActivity.RESTAURANT_OBJECT_KEY);
                mRestaurantName = getIntent().getStringExtra(RestaurantProfileActivity.RESTAURANT_NAME_KEY);
                mSelectedDate = getIntent().getStringExtra(RestaurantProfileActivity.SELECTED_DATE_KEY);
                mSelectedTime = getIntent().getStringExtra(RestaurantProfileActivity.SELECTED_TIME_KEY);
                mSelectedNumberOfPeople = getIntent().getStringExtra(RestaurantProfileActivity.SELECTED_NUMBER_PEOPLE_KEY);
//                mMenuList = getIntent().getParcelableArrayListExtra(RestaurantProfileActivity.MENU_LIST_KEY);
            }

            else if (mAction.equals(EditReservationActivity.ACTION_EDIT_RESERVE)) {
                mCheckoutOrderLayout.setVisibility(View.VISIBLE);
                mUpComingReservation = getIntent().getParcelableExtra(EditReservationActivity.UPCOMING_RESERVATION_OBJECT_KEY);
                if (mUpComingReservation != null) {
                    mRestaurant = mUpComingReservation.getRestaurant();
                }
                if (mRestaurant != null) {
                    mRestaurantName = mRestaurant.getName();
//                    mMenuList = mRestaurant.getMenus();

                    for (int i = 0; i < mMenuList.size(); i ++) {
                        List<MenuItem> menuItemsList = mMenuList.get(i).getMenuItems();
                        for (int j = 0; j < menuItemsList.size(); j ++) {
                            MenuItem menuItem = menuItemsList.get(j);
//                            if (menuItem.getQuantity() > 0) {
//                                ReservationOrder reservationOrder = new ReservationOrder();
//                                reservationOrder.setId(menuItem.getId());
//                                reservationOrder.setQuantity(menuItem.getQuantity());
//                                mReservationOrdersList.add(reservationOrder);
//                            }
                        }
                    }

                    for (int i = 0; i < mMenuList.size(); i ++) {
                        List<MenuItem> menuItemsList = mMenuList.get(i).getMenuItems();
                        for (int j = 0; j < menuItemsList.size(); j ++) {
                            MenuItem menuItem = menuItemsList.get(j);
//                            mTotalQuantity += menuItem.getQuantity();
//                            mTotalPrice += menuItem.getQuantity() * Double.parseDouble(menuItem.getPrice());
                        }
                    }

                    mTotalQuantityTextView.setText(String.valueOf("x" + mTotalQuantity));
                    mTotalPriceTextView.setText(String.valueOf(mTotalPrice + " €"));
                }

                mSelectedDate = getIntent().getStringExtra(EditReservationActivity.SELECTED_DATE_KEY);
                mSelectedTime = getIntent().getStringExtra(EditReservationActivity.SELECTED_TIME_KEY);
                mSelectedNumberOfPeople = getIntent().getStringExtra(EditReservationActivity.SELECTED_NUMBER_PEOPLE_KEY);
            }

            else {
                mCheckoutOrderLayout.setVisibility(View.GONE);
                mRestaurantName = getIntent().getStringExtra(RestaurantProfileActivity.RESTAURANT_NAME_KEY);
//                mMenuList = getIntent().getParcelableArrayListExtra(RestaurantProfileActivity.MENU_LIST_KEY);
            }
        }

        else {
            mCheckoutOrderLayout.setVisibility(View.GONE);
            mRestaurantName = getIntent().getStringExtra(RestaurantProfileActivity.RESTAURANT_NAME_KEY);
//            mMenuList = getIntent().getParcelableArrayListExtra(RestaurantProfileActivity.MENU_LIST_KEY);
        }

        updateRestaurantNameView();

        setUpMenuListView();

        arrowBackClick();

        checkoutOrderLayoutClick();
    }

    private void requestMenueData(){
        int restaurentId = getIntent().getIntExtra(RESTAURENT_KEY, 0);
        MenuResponseWebService webService = RetrofitWebService.retrofit.create(MenuResponseWebService.class);
        Call<MenuResponse> menuResponse = webService.getRestaurantMenu(restaurentId);
        menuResponse.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                MenuResponse menuResponse = response.body();

            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {

            }
        });
    }
}
