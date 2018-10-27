package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.NumberOfPeopleAdapter;
import com.example.alexander.halalappv1.adapters.TimeAdapter;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.model.modifiedmodels.WorkDay;
import com.example.alexander.halalappv1.model.newModels.menues.MenuItem;
import com.example.alexander.halalappv1.model.newModels.reservation.details.Product;
import com.example.alexander.halalappv1.model.newModels.reservation.details.ReservationDetails;
import com.example.alexander.halalappv1.model.newModels.reservation.details.ReservationDetailsAllResponse;
import com.example.alexander.halalappv1.model.newModels.workdays.Period;
import com.example.alexander.halalappv1.model.newModels.workdays.WorkingDay;
import com.example.alexander.halalappv1.model.newModels.workdays.WorkingHoursResponse;
import com.example.alexander.halalappv1.services.ReservationDetailsResponse;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.services.WorkingHours;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.squareup.picasso.Picasso;
import com.whinc.widget.ratingbar.RatingBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.fragments.HomeFragment.RESTAURENT_KEY;
import static com.example.alexander.halalappv1.fragments.ReserveFragment.EDIT_RESERVATION_OBJECT_KEY;

public class EditReservationActivity extends AppCompatActivity {

    private static final String TAG = "XXXXX";
    public static final String ACTION_EDIT_RESERVE = "ActionEditReserve";
    public static final String UPCOMING_RESERVATION_OBJECT_KEY = "UpcomingReservationObject";
    public static final String SELECTED_DATE_KEY = "SelectedDate";
    public static final String SELECTED_TIME_KEY = "SelectedTime";
    public static final String SELECTED_NUMBER_PEOPLE_KEY = "SelectedNumberOfPeople";
    public static final String RESERVATION_ID = "selectedReservationId";
    private WorkingHoursResponse mRestaurentWorkingHours;
    private boolean isNetworkOk;

    private ConstraintLayout mReserveLayout;

    private TextView mNumberOfPeopleLabelTextView;
    private TextView mNoCapacityTextView;
    private ProgressBar mCalendarLoadingIndicator;
    private ImageView mRestaurantImageImageView;
    private ImageView mFavouriteIconImageView;
    private ImageView mArrowBackImageView;
    private ImageView mGalleryIconImageView;
    private TextView mRestaurantNameTextView;
    private RatingBar mRestaurantRateRatingBar;

    private LinearLayout mDateTimeLayout;
    private TextView mDateTextView;
    private TextView mNumberOfPeopleTextView;
    private ConstraintLayout mCalendarLayout;
    private TextView mMonthTextView;
    private MaterialCalendarView mCalendarView;
    private RecyclerView mTimeRecyclerView;
    private RecyclerView mNumberOfPeopleRecyclerView;
    private TextView mRestaurantIsClosedTextView;
    private Button mReserveButton;
    private Button mPlaceAnOrderButton;

    private TimeAdapter mTimeAdapter;
    private ArrayList<Period> mTimeList = new ArrayList<>();
    boolean isListItemClickable;
    private NumberOfPeopleAdapter mNumberOfPeopleAdapter;
    private ArrayList<String> mNumberOfPeopleList = new ArrayList<>();

    private ScrollView mScrollView;
    private Restaurant mRestaurant;
    private int mUserId;
    private String mSelectedDate;
    private String mSelectedTime;
    private String mSelectedNumberOfPeople;
    private ArrayList<Product> mProducts;
    private ReservationDetailsAllResponse mReservationDetailsResponse;
    private ReservationDetails mReservationDetails;
    private com.example.alexander.halalappv1.model.newModels.reservation.details.Restaurant mRestaurantRes;
    private int mTotalQuantity;
    //==============================================================================================
    private void findViewsById() {
        mRestaurantImageImageView = findViewById(R.id.iv_edit_reservation_activity_restaurant_image);
        mFavouriteIconImageView = findViewById(R.id.iv_edit_reservation_activity_favourite);
//        mArrowBackImageView = findViewById(R.id.iv_edit_reservation_activity_arrow_back);
        mRestaurantNameTextView = findViewById(R.id.tv_edit_reservation_activity_restaurant_name);
        mNumberOfPeopleLabelTextView = findViewById(R.id.tv_calendar_layout_number_of_people_label);
        mNoCapacityTextView = findViewById(R.id.tv_calendar_layout_no_capacity_message);

        mCalendarLoadingIndicator = findViewById(R.id.pb_calendar_layout_loading_indicator);

        mDateTimeLayout = findViewById(R.id.date_time_number_of_people_layout);
        mDateTextView = findViewById(R.id.tv_date_text);
        mScrollView = findViewById(R.id.restaurant_profile_scroll_view);

        mReserveLayout = findViewById(R.id.layout_reserve_place_an_order);

//        mNumberOfPeopleTextView = findViewById(R.id.tv_number_of_people_text);
        mCalendarLayout = findViewById(R.id.edit_reservation_activity_calendar_layout);
        mMonthTextView = findViewById(R.id.tv_month_text);
        mCalendarView = findViewById(R.id.calendar_view);
        mTimeRecyclerView = findViewById(R.id.rv_calendar_time_list);
        mNumberOfPeopleRecyclerView = findViewById(R.id.rv_calendar_number_of_people_list);
        mRestaurantIsClosedTextView = findViewById(R.id.tv_calendar_layout_restaurant_is_closed_message);
        mReserveButton = findViewById(R.id.btn_calendar_layout_reserve);
        mPlaceAnOrderButton = findViewById(R.id.btn_calendar_layout_place_an_order);
        findViewById(R.id.restaurant_profile_scroll_view).scrollTo(0, mCalendarLayout.getHeight());
    }

    private void updateMainViewsWithRestaurantData() {
        Picasso.with(this).load(mRestaurantRes.getPicture()).into(mRestaurantImageImageView);
        if (mRestaurant != null) {

            if (mRestaurant.getFavourite().equals("true")) {
                mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_pink);
            } else {
                mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_empty);
            }
            mRestaurantNameTextView.setText(mRestaurant.getName());
            mRestaurantRateRatingBar.setCount((mRestaurant.getRate()));
        }
    }

    private void favouriteIconClick() {
        mFavouriteIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsFavourite(mUserId, mRestaurant.getId());
                if (mRestaurant.getFavourite().equals("false")) {
                    mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_pink);
                    mRestaurant.setFavourite("true");
                } else {
                    mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_empty);
                    mRestaurant.setFavourite("false");
                }
            }
        });
    }

    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditReservationActivity.this, MainActivity.class);
                intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    private void galleryIconClick() {
        mGalleryIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditReservationActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
    }
    //==============================================================================================
    private void setUpTimeRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTimeRecyclerView.setLayoutManager(layoutManager);
        mTimeAdapter = new TimeAdapter(EditReservationActivity.this, new TimeAdapter.OnTimeClickListener() {
            @Override
            public void onTimeSelected(int position) {
                mTimeAdapter.setClickedItem(position);
                mSelectedTime = mTimeList.get(position).toString();
                mTimeAdapter.notifyDataSetChanged();
                isNetworkOk = NetworkHelper.hasNetworkAccess(EditReservationActivity.this);
                if (isNetworkOk) {
                    mCalendarLoadingIndicator.setVisibility(View.VISIBLE);
                    mNumberOfPeopleList.clear();
                    mNumberOfPeopleAdapter.setNumberOfPeopleList(null);
                    mNumberOfPeopleAdapter.setClickedItem(-10);
                    mSelectedNumberOfPeople = null;
                    mNumberOfPeopleLabelTextView.setVisibility(View.GONE);
                    mNumberOfPeopleRecyclerView.setVisibility(View.GONE);
                    mNoCapacityTextView.setVisibility(View.GONE);

                    int restaurantId = mRestaurantRes.getId();
                    String reservationDate = mSelectedDate.split("-")[2] + "-" + mSelectedDate.split("-")[1] + "-" + mSelectedDate.split("-")[0];
                    String reservationTime = mSelectedTime;
                    getCapacity(restaurantId, reservationDate, reservationTime);
                } else {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setUpNumberOfPeopleRecyclerView() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mNumberOfPeopleRecyclerView.setLayoutManager(layoutManager1);
        mNumberOfPeopleAdapter = new NumberOfPeopleAdapter(EditReservationActivity.this, new NumberOfPeopleAdapter.OnNumberClickListener() {
            @Override
            public void onNumberSelected(int position) {
                mNumberOfPeopleAdapter.setClickedItem(position);
                mSelectedNumberOfPeople = mNumberOfPeopleList.get(position);
//                mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople);
                mNumberOfPeopleAdapter.notifyDataSetChanged();
            }
        });

        for (int i =1; i<= mReservationDetails.getGuests(); i++){
            mNumberOfPeopleList.add(String.valueOf(i));
        }
        mNumberOfPeopleAdapter.setNumberOfPeopleList(mNumberOfPeopleList);
        mNumberOfPeopleRecyclerView.setAdapter(mNumberOfPeopleAdapter);
        mNumberOfPeopleAdapter.setClickedItem(mNumberOfPeopleList.size() - 1);
        mNumberOfPeopleAdapter.notifyDataSetChanged();
    }

    private void setUpCalendarView() {
        mCalendarView.setTopbarVisible(false);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 90);

        CharSequence[] charSequences = new CharSequence[7];
        String language = SharedPreferencesHelper.getSharedPreferenceString(EditReservationActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
        if (language != null) {
            if (language.equals("français")) {
                charSequences[0] = "D";
                charSequences[1] = "L";
                charSequences[2] = "M";
                charSequences[3] = "M";
                charSequences[4] = "J";
                charSequences[5] = "V";
                charSequences[6] = "S";
            } else {
                charSequences[0] = "S";
                charSequences[1] = "M";
                charSequences[2] = "T";
                charSequences[3] = "W";
                charSequences[4] = "T";
                charSequences[5] = "F";
                charSequences[6] = "S";
            }
        } else {
            charSequences[0] = "S";
            charSequences[1] = "M";
            charSequences[2] = "T";
            charSequences[3] = "W";
            charSequences[4] = "T";
            charSequences[5] = "F";
            charSequences[6] = "S";
        }

        mCalendarView.setWeekDayLabels(charSequences);
        mCalendarView.setWeekDayTextAppearance(R.style.CustomTextAppearance);
        mCalendarView.state().edit()
                .setFirstDayOfWeek(2)
                .setMinimumDate(Calendar.getInstance())
                .setMaximumDate(calendar)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = simpleDateFormat1.parse(mReservationDetails.getDate());
            mSelectedDate = simpleDateFormat.format(date);
            Date date1 = simpleDateFormat.parse(mSelectedDate);
            mCalendarView.setSelectedDate(date1);
            mCalendarView.setSelectionColor(getResources().getColor(R.color.pink));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendar1 = Calendar.getInstance();
        mMonthTextView.setText(getMonthText(calendar1.get(Calendar.MONTH)) + " " + calendar1.get(Calendar.YEAR));
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                setSelectedDate(date);
            }
        });

        //Listener on swipe
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mMonthTextView.setText(getMonthText(date.getMonth()) + " " + date.getYear());
            }
        });

        setUpTimeRecyclerView();

    }

    private void updateTimeRecyclerViewWithCurrentDayWorkingHours() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(mReservationDetails.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String currentDayOfTheWeek = getSelectedDayOfTheWeek(outFormat.format(date));
        //==========================================================================================
        List<WorkingDay> workDaysList = mRestaurentWorkingHours.getWorkingDays();
        for (int i = 0; i < workDaysList.size(); i ++) {
            if (workDaysList.get(i).getDayName().equalsIgnoreCase(currentDayOfTheWeek)) {
                WorkingDay workDay = workDaysList.get(i);
                if (workDay.getPeriods() != null && workDay.getPeriods().size() > 0) {

                    mRestaurantIsClosedTextView.setVisibility(View.INVISIBLE);
                    mTimeRecyclerView.setVisibility(View.VISIBLE);
                    mTimeList.addAll(workDay.getPeriods());
                    mTimeAdapter.setTimeList(mTimeList);
                    mTimeRecyclerView.setAdapter(mTimeAdapter);
                } else {
                    mRestaurantIsClosedTextView.setVisibility(View.VISIBLE);
                    mTimeRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        }

        mSelectedTime = mReservationDetails.getTime();
        // todo set the init time
        boolean isTimeAvailable = false;
        for (int i = 0; i < mTimeList.size(); i ++) {
            if (mTimeList.get(i).getHour().equals(mSelectedTime.replace("h",":"))) {
                isTimeAvailable = true;
                mTimeAdapter.setClickedItem(i);
                mSelectedTime = mTimeList.get(i).getHour();
                mTimeAdapter.notifyDataSetChanged();
                if (i > 1 && i < mTimeList.size() - 1) {
                    mTimeRecyclerView.scrollToPosition(i - 1);
                } else {
                    mTimeRecyclerView.scrollToPosition(i);
                }
            }
        }
        if (!isTimeAvailable){
            Period p = new Period();
            p.setHour(mReservationDetails.getTime().replace("h",":"));
            mTimeList.add(p);
            mTimeAdapter.setClickedItem(mTimeList.size() - 1);
            mTimeAdapter.notifyDataSetChanged();
        }
    }

    private String getSelectedDayOfTheWeek(String day) {
        if (day.equals("Sunday") || day.equals("dimanche")) {
            return "Sunday";
        }
        else if (day.equals("Monday") || day.equals("lundi")) {
            return "Monday";
        }
        else if (day.equals("Tuesday") || day.equals("mardi")) {
            return "Tuesday";
        }
        else if (day.equals("Wednesday") || day.equals("mercredi")) {
            return "Wednesday";
        }
        else if (day.equals("Thursday") || day.equals("jeudi")) {
            return "Thursday";
        }
        else if (day.equals("Friday") || day.equals("vendredi")) {
            return "Friday";
        }
        else if (day.equals("Saturday") || day.equals("samedi")) {
            return "Saturday";
        }
        else {
            return "";
        }
    }

    private String getMonthText(int monthNumber) {
        String language = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
        if (language != null) {
            if (language.equals("français")) {
                if (monthNumber == 0) {
                    return "Janvier";
                }
                else if (monthNumber == 1) {
                    return "Février";
                }
                else if (monthNumber == 2) {
                    return "Mars";
                }
                else if (monthNumber == 3) {
                    return "Avril";
                }
                else if (monthNumber == 4) {
                    return "Mai";
                }
                else if (monthNumber == 5) {
                    return "Juin";
                }
                else if (monthNumber == 6) {
                    return "Juillet";
                }
                else if (monthNumber == 7) {
                    return "Août";
                }
                else if (monthNumber == 8) {
                    return "Septembre";
                }
                else if (monthNumber == 9) {
                    return "Octobre";
                }
                else if (monthNumber == 10) {
                    return "Novembre";
                }
                else if (monthNumber == 11) {
                    return "Décembre";
                }
                else {
                    return "No Match";
                }
            }

            else {
                if (monthNumber == 0) {
                    return "January";
                }
                else if (monthNumber == 1) {
                    return "February";
                }
                else if (monthNumber == 2) {
                    return "March";
                }
                else if (monthNumber == 3) {
                    return "April";
                }
                else if (monthNumber == 4) {
                    return "May";
                }
                else if (monthNumber == 5) {
                    return "June";
                }
                else if (monthNumber == 6) {
                    return "July";
                }
                else if (monthNumber == 7) {
                    return "August";
                }
                else if (monthNumber == 8) {
                    return "September";
                }
                else if (monthNumber == 9) {
                    return "October";
                }
                else if (monthNumber == 10) {
                    return "November";
                }
                else if (monthNumber == 11) {
                    return "December";
                }
                else {
                    return "No Match";
                }
            }
        }

        else {
            return "No Match";
        }
    }

    private void reserveButtonClick() {
        mReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo
                /*for (int i = 0; i < mUpComingReservation.getRestaurant().getMenus().size(); i ++) {
                    List<MenuItem> menuItemsList = mUpComingReservation.getRestaurant().getMenus().get(i).getMenuItems();
                    for (int j = 0; j < menuItemsList.size(); j ++) {
                        MenuItem menuItem = menuItemsList.get(j);
                        menuItem.setQuantity(0);
                    }
                }*/

                if (mSelectedDate == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_date), Toast.LENGTH_LONG).show();
                } else if (mSelectedTime == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_time), Toast.LENGTH_LONG).show();
                } else if (mSelectedNumberOfPeople == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_number_of_people), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(EditReservationActivity.this, SubmitReservationActivity.class);
                    // todo
                    intent.putExtra(RESERVATION_ID, mReservationDetails.getId());
                    intent.putExtra(RESTAURENT_KEY, mRestaurantRes.getId());
                    intent.putExtra(SELECTED_DATE_KEY, mSelectedDate);
                    intent.putExtra(SELECTED_TIME_KEY, mSelectedTime.replace("h",":"));
                    intent.putExtra(SELECTED_NUMBER_PEOPLE_KEY, mSelectedNumberOfPeople);
                    startActivity(intent);
                }
            }
        });
    }

    private void placeAnOrderButtonClick() {
        mPlaceAnOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedDate == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_date), Toast.LENGTH_LONG).show();
                } else if (mSelectedTime == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_time), Toast.LENGTH_LONG).show();
                } else if (mSelectedNumberOfPeople == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_number_of_people), Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(EditReservationActivity.this, MenuActivity.class);
                    //todo invest how to send details to menu
//                    intent.putExtra(UPCOMING_RESERVATION_OBJECT_KEY, mUpComingReservation);
                    intent.putExtra(SELECTED_DATE_KEY, mSelectedDate);
                    intent.putExtra(SELECTED_TIME_KEY, mSelectedTime);
                    intent.putExtra(SELECTED_NUMBER_PEOPLE_KEY, mSelectedNumberOfPeople);
                    intent.setAction(ACTION_EDIT_RESERVE);
                    startActivity(intent);
                }
            }
        });
    }
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        mUserId = SharedPreferencesHelper.getSharedPreferenceInt(this, ConstantsHelper.KEY_USER_ID, -10);
        findViewsById();
        getData();
        //==========================================================================================
        favouriteIconClick();
//        arrowBackClick();
//        galleryIconClick();
        reserveButtonClick();
        placeAnOrderButtonClick();
        //==========================================================================================
    }

    public void updateData(){
        try {
            mSelectedDate = mReservationDetails.getDate2();
            String[] dateParts = mSelectedDate.split("-");
            mDateTextView.setText(dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0]);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(mSelectedDate);
            mCalendarView.setSelectedDate(date);
            mCalendarView.setSelectionColor(getResources().getColor(R.color.pink));
            mCalendarView.setCurrentDate(date);
            mSelectedDate = dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
        } catch (ParseException e) {
            e.printStackTrace();
        }


        mSelectedNumberOfPeople = mReservationDetails.getGuests().toString();
//        mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople);
        for (int i = 0; i < mNumberOfPeopleList.size(); i ++) {
            if (mNumberOfPeopleList.get(i).equals(mSelectedNumberOfPeople)) {
                mNumberOfPeopleAdapter.setClickedItem(i);
                mSelectedNumberOfPeople = mNumberOfPeopleList.get(i);
                mNumberOfPeopleAdapter.notifyDataSetChanged();
                if (i > 1 && i < mNumberOfPeopleList.size() - 1) {
                    mNumberOfPeopleRecyclerView.scrollToPosition(i - 1);
                } else {
                    mNumberOfPeopleRecyclerView.scrollToPosition(i);
                }
            }
        }

        for (int i = 0; i < mProducts.size(); i ++) {
            mTotalQuantity += mProducts.get(i).getQuantity();
        }
        if (mTotalQuantity > 0) {
            mReserveButton.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditReservationActivity.this, MainActivity.class);
        intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
        startActivity(intent);
    }

    private void markAsFavourite(int userId, int restaurantId) {
        if (userId != -10 && restaurantId >= 0) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<Object> resultCall = webService.markAsFavourite(userId, restaurantId);
            resultCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, String.valueOf(response.body()));
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        }
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
                setUpCalendarView();
                getWorkingHours(mRestaurantRes.getId());

                setUpNumberOfPeopleRecyclerView();
                updateMainViewsWithRestaurantData();
//                updateTimeRecyclerViewWithCurrentDayWorkingHours();
            }

            @Override
            public void onFailure(Call<ReservationDetailsAllResponse> call, Throwable t) {

            }
        });
    }

    private void getWorkingHours(int restaurentId){
        WorkingHours workingHours = RetrofitWebService.retrofit.create(WorkingHours.class);
        Call<WorkingHoursResponse> restaurentWorkingHours = workingHours.getRestaurentWorkingHours(restaurentId);
        restaurentWorkingHours.enqueue(new Callback<WorkingHoursResponse>() {
            @Override
            public void onResponse(Call<WorkingHoursResponse> call, Response<WorkingHoursResponse> response) {
                mRestaurentWorkingHours = response.body();
                updateTimeRecyclerViewWithCurrentDayWorkingHours();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = simpleDateFormat.parse(mReservationDetails.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                setSelectedDate(CalendarDay.from(date));
            }

            @Override
            public void onFailure(Call<WorkingHoursResponse> call, Throwable t) {

            }
        });
    }

    private void setSelectedDate(CalendarDay date){
        int day = date.getCalendar().get(Calendar.DAY_OF_MONTH);
        int month = date.getCalendar().get(Calendar.MONTH) + 1;
        int year = date.getYear();
        String dayString = String.valueOf(day);
        String monthString = String.valueOf(month);
        if (day < 10) {
            dayString = String.valueOf("0" + day);
        }
        if (month < 10) {
            monthString = String.valueOf("0" + month);
        }

        mSelectedDate = dayString + "/" + monthString + "/" + year;
        mDateTextView.setText(mSelectedDate);
        //==================================================================================
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String selectedDayOfTheWeek = getSelectedDayOfTheWeek(simpleDateFormat.format(date.getDate()));

        mTimeList.clear();
        mTimeAdapter.setTimeList(null);
        mTimeAdapter.setClickedItem(-10);
        mSelectedTime = null;
        List<WorkingDay> workDaysList = mRestaurentWorkingHours.getWorkingDays();
        for (int i = 0; i < workDaysList.size(); i ++) {
            if (workDaysList.get(i).getDayName().equalsIgnoreCase(selectedDayOfTheWeek)) {
                WorkingDay workDay = workDaysList.get(i);
                if (workDay.getPeriods() != null && workDay.getPeriods().size() > 0) {
                    mRestaurantIsClosedTextView.setVisibility(View.INVISIBLE);
                    mTimeRecyclerView.setVisibility(View.VISIBLE);
                    mTimeList.addAll(workDay.getPeriods());
                    mTimeAdapter.setTimeList(mTimeList);
                    mTimeRecyclerView.setAdapter(mTimeAdapter);
                } else {
                    mRestaurantIsClosedTextView.setVisibility(View.VISIBLE);
                    mTimeRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
    private void getCapacity(int restaurantId, String reservationDate, String reservationTime) {
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<JsonObject> call = webService.getCapacity(restaurantId, reservationDate, reservationTime);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mCalendarLoadingIndicator.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    int capacity = (response.body().get("seats").getAsInt());
                    if (capacity > 0) {
                        mNumberOfPeopleLabelTextView.setVisibility(View.VISIBLE);
                        mNumberOfPeopleRecyclerView.setVisibility(View.VISIBLE);
                        mNoCapacityTextView.setVisibility(View.GONE);
                        for (int i = 1; i <= capacity; i ++) {
                            mNumberOfPeopleList.add(String.valueOf(i));
                        }
                        mNumberOfPeopleAdapter.setNumberOfPeopleList(mNumberOfPeopleList);
                        mNumberOfPeopleRecyclerView.setAdapter(mNumberOfPeopleAdapter);
                    } else {
                        mNumberOfPeopleLabelTextView.setVisibility(View.GONE);
                        mNumberOfPeopleRecyclerView.setVisibility(View.GONE);
                        mNoCapacityTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    mNumberOfPeopleLabelTextView.setVisibility(View.GONE);
                    mNumberOfPeopleRecyclerView.setVisibility(View.GONE);
                    mNoCapacityTextView.setVisibility(View.VISIBLE);
                }

                mScrollView.scrollTo(0, mReserveLayout.getBottom());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCalendarLoadingIndicator.setVisibility(View.GONE);
                mNumberOfPeopleLabelTextView.setVisibility(View.GONE);
                mNumberOfPeopleRecyclerView.setVisibility(View.GONE);
                mNoCapacityTextView.setVisibility(View.VISIBLE);
                mScrollView.scrollTo(0, mReserveLayout.getBottom());
            }
        });
    }

}
