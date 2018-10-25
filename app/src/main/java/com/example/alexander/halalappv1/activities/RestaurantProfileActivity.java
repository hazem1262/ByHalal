package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import java.text.DecimalFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.fragments.HomeFragment;
import com.example.alexander.halalappv1.adapters.NumberOfPeopleAdapter;
import com.example.alexander.halalappv1.adapters.TimeAdapter;

import com.example.alexander.halalappv1.model.modifiedmodels.RestaurantsList1;
import com.example.alexander.halalappv1.model.newModels.RestaurantProfile;
import com.example.alexander.halalappv1.model.newModels.workdays.Period;
import com.example.alexander.halalappv1.model.newModels.workdays.WorkingDay;
import com.example.alexander.halalappv1.model.newModels.workdays.WorkingHoursResponse;
import com.example.alexander.halalappv1.services.RestaurentProfileWebService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.fragments.HomeFragment.RESTAURENT_KEY;

public class RestaurantProfileActivity extends AppCompatActivity {

    private static final String TAG = "xxxxx";
    public static final String MENU_LIST_KEY = "MenuList";
    public static final String RESTAURANT_NAME_KEY = "RestaurantName";
    public static final String RESTAURANT_ID_KEY = "RestaurantId";
    public static final String ACTION_RESERVE = "ActionReserve";
    public static final String RESTAURANT_OBJECT_KEY = "RestaurantObject";
    public static final String SELECTED_DATE_KEY = "SelectedDate";
    public static final String SELECTED_TIME_KEY = "SelectedTime";
    public static final String SELECTED_NUMBER_PEOPLE_KEY = "SelectedNumberOfPeople";

    private ScrollView mScrollView;
    private ImageView mRestaurantImageImageView;
    private ImageView mFavouriteIconImageView;
    private ImageView mArrowBackImageView;
    private ImageView mGalleryIconImageView;
    private ConstraintLayout mLoadingIndicator;
    private TextView mRestaurantNameTextView;
//    private RatingBar mRestaurantRateRatingBar;

    // Restaurant Information Layout
    private LinearLayout mInformationLayout;
    private TextView mRestaurantDescriptionTextView;
    private TextView mCuisineTextView;
    private TextView mExecutiveChefTextView;
    private ConstraintLayout mCertificateLayout;
//    private TextView mCertificateTextView;
    private TextView mWineTextView;
    private TextView mVisitorsTextView;
//    private TextView mNeighbourhoodTextView;
    private TextView mRestaurantAddressTextView;
//    private TextView mRestaurantCityTextView;
//    private TextView mRestaurantPhoneNumberTextView;
//    private TextView mRestaurantNotesTextView;

    private ConstraintLayout mLocationLayout;
    private ConstraintLayout mPhoneNumberLayout;
    private LinearLayout mMenuLayout;
    private LinearLayout mWebsiteLayout;
    private ConstraintLayout mWorkingHoursLayout;

    private RestaurantsList1 mTable;
    private RestaurantProfile mRestaurant;
    private String mLanguage;
    private int mUserId;
    private String mAction;
    //==============================================================================================
    private LinearLayout mDateTimeLayout;
    private TextView mDateTextView;
//    private TextView mNumberOfPeopleTextView;
    private ConstraintLayout mCalendarLayout;
    private TextView mMonthTextView;
    private MaterialCalendarView mCalendarView;
    private RecyclerView mTimeRecyclerView;
    private TextView mNumberOfPeopleLabelTextView;
    private RecyclerView mNumberOfPeopleRecyclerView;
    private TextView mRestaurantIsClosedTextView;
    private TextView mNoCapacityTextView;
    private ConstraintLayout mReserveLayout;
    private Button mReserveButton;
    private Button mPlaceAnOrderButton;
    private ProgressBar mCalendarLoadingIndicator;

    private TimeAdapter mTimeAdapter;
    private ArrayList<Period> mTimeList = new ArrayList<>();
    private NumberOfPeopleAdapter mNumberOfPeopleAdapter;
    private ArrayList<String> mNumberOfPeopleList = new ArrayList<>();

    private boolean isCalendarVisible = false;
    private boolean isNetworkOk;
    private boolean isLoggedIn;
    private String mSelectedDate;
    private String mSelectedTime;
    private String mSelectedNumberOfPeople;
    private WorkingHoursResponse mRestaurentWorkingHours;
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile_modified);
        mAction = getIntent().getAction();

        findViewsById();
        requestRestaurentData();
        getWorkingHours();
        //==========================================================================================
        favouriteIconClick();
//        arrowBackClick();
        restaurantImageClick();
        //==========================================================================================
//        phoneNumberLayoutClick();
        menuLayoutClick();
        websiteLayoutClick();
//        workingHoursLayoutClick();
        //==========================================================================================
        updateDateViewWithCurrentDate();
        dateTimeLayoutClick();
        setUpTimeRecyclerView();
        setUpNumberOfPeopleRecyclerView();
        setUpCalendarView();
        reserveButtonClick();
        placeAnOrderButtonClick();
    }

    //==============================================================================================

    //get restaurentProfilData

    private void updateDateViewWithCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate = simpleDateFormat.format(date);
        mDateTextView.setText(currentDate);
    }

    private void dateTimeLayoutClick() {
        mDateTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCalendarVisible) {
                    mCalendarLayout.setVisibility(View.GONE);
                    mInformationLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.restaurant_profile_scroll_view).scrollTo(0, 0);
                    isCalendarVisible = false;
                } else {
                    setUpCalendarView();
                    mCalendarLayout.setVisibility(View.VISIBLE);
                    mInformationLayout.setVisibility(View.GONE);
                    mScrollView.scrollTo(0, mInformationLayout.getHeight());
                    isCalendarVisible = true;
                }
            }
        });
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

    private void setUpTimeRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTimeRecyclerView.setLayoutManager(layoutManager);
        mTimeAdapter = new TimeAdapter(RestaurantProfileActivity.this, new TimeAdapter.OnTimeClickListener() {
            @Override
            public void onTimeSelected(int position) {
                mTimeAdapter.setClickedItem(position);
                mSelectedTime = mTimeList.get(position).getHour();
                mTimeAdapter.notifyDataSetChanged();

                isNetworkOk = NetworkHelper.hasNetworkAccess(RestaurantProfileActivity.this);
                if (isNetworkOk) {
                    mCalendarLoadingIndicator.setVisibility(View.VISIBLE);
                    mNumberOfPeopleList.clear();
                    mNumberOfPeopleAdapter.setNumberOfPeopleList(null);
                    mNumberOfPeopleAdapter.setClickedItem(-10);
                    mSelectedNumberOfPeople = null;
                    mNumberOfPeopleLabelTextView.setVisibility(View.GONE);
                    mNumberOfPeopleRecyclerView.setVisibility(View.GONE);
                    mNoCapacityTextView.setVisibility(View.GONE);

                    int restaurantId = mRestaurant.getId();
                    String reservationDate = mSelectedDate.split("/")[2] + "-" + mSelectedDate.split("/")[1] + "-" + mSelectedDate.split("/")[0];
                    String reservationTime = mSelectedTime;
                    getCapacity(restaurantId, reservationDate, reservationTime);
                } else {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setUpNumberOfPeopleRecyclerView() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mNumberOfPeopleRecyclerView.setLayoutManager(layoutManager1);
        mNumberOfPeopleAdapter = new NumberOfPeopleAdapter(RestaurantProfileActivity.this, new NumberOfPeopleAdapter.OnNumberClickListener() {
            @Override
            public void onNumberSelected(int position) {
                mNumberOfPeopleAdapter.setClickedItem(position);
                mSelectedNumberOfPeople = mNumberOfPeopleList.get(position);
//                mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople);
                mNumberOfPeopleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpCalendarView() {
        mCalendarView.setTopbarVisible(false);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 90);

        CharSequence[] charSequences = new CharSequence[7];
        if (mLanguage != null) {
            if (mLanguage.equals("français")) {
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
            Date date = new Date();
            mSelectedDate = simpleDateFormat.format(date);
            Date date1 = simpleDateFormat.parse(mSelectedDate);
            mCalendarView.setSelectedDate(date1);
            mCalendarView.setSelectionColor(getResources().getColor(R.color.pink));
            String[] dateParts = mSelectedDate.split("-");
            mSelectedDate = dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar calendar1 = Calendar.getInstance();
        mMonthTextView.setText(getMonthText(calendar1.get(Calendar.MONTH)) + " " + calendar1.get(Calendar.YEAR));
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mNumberOfPeopleLabelTextView.setVisibility(View.GONE);
                mNumberOfPeopleRecyclerView.setVisibility(View.GONE);
                mNoCapacityTextView.setVisibility(View.GONE);
                mNumberOfPeopleList.clear();
                mNumberOfPeopleAdapter.setNumberOfPeopleList(null);
                mNumberOfPeopleAdapter.setClickedItem(-10);
                mSelectedNumberOfPeople = null;

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

                //todo
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
        });

        //Listener on swipe
        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                mMonthTextView.setText(getMonthText(date.getMonth()) + " " + date.getYear());
            }
        });
    }

    private void updateTimeRecyclerViewWithCurrentDayWorkingHours() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String currentDayOfTheWeek = getSelectedDayOfTheWeek(simpleDateFormat.format(date));
        List<WorkingDay> workDaysList = mRestaurentWorkingHours.getWorkingDays();
        WorkingDay workDay = new WorkingDay();
        for (int i = 0; i < workDaysList.size(); i ++) {
            if (workDaysList.get(i).getDayName().equalsIgnoreCase(currentDayOfTheWeek)) {
                workDay = workDaysList.get(i);
            }
        }
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

    private void getWorkingHours(){
        int restaurentId = getIntent().getIntExtra(RESTAURENT_KEY, 0);
        WorkingHours workingHours = RetrofitWebService.retrofit.create(WorkingHours.class);
        Call<WorkingHoursResponse> restaurentWorkingHours = workingHours.getRestaurentWorkingHours(restaurentId);
        restaurentWorkingHours.enqueue(new Callback<WorkingHoursResponse>() {
            @Override
            public void onResponse(Call<WorkingHoursResponse> call, Response<WorkingHoursResponse> response) {
                mRestaurentWorkingHours = response.body();
                updateTimeRecyclerViewWithCurrentDayWorkingHours();
            }

            @Override
            public void onFailure(Call<WorkingHoursResponse> call, Throwable t) {

            }
        });
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
                if (mSelectedDate == null) {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_select_date), Toast.LENGTH_LONG).show();
                } else if (mSelectedTime == null) {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_select_time), Toast.LENGTH_LONG).show();
                } else if (mSelectedNumberOfPeople == null) {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_select_number_of_people), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(RestaurantProfileActivity.this, SubmitReservationActivity.class);
//                    intent.putExtra(RESTAURANT_OBJECT_KEY, mRestaurant);
                    int restaurentId = getIntent().getIntExtra(RESTAURENT_KEY, 0);
                    intent.putExtra(RESTAURENT_KEY, restaurentId);
                    intent.putExtra(SELECTED_DATE_KEY, mSelectedDate);
                    intent.putExtra(SELECTED_TIME_KEY, mSelectedTime);
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
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_select_date), Toast.LENGTH_LONG).show();
                } else if (mSelectedTime == null) {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_select_time), Toast.LENGTH_LONG).show();
                } else if (mSelectedNumberOfPeople == null) {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_select_number_of_people), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(RestaurantProfileActivity.this, MenuActivity.class);
                    String restaurantName = mRestaurant.getName();
                    intent.putExtra(RESTAURANT_NAME_KEY, restaurantName);
                    intent.putExtra(RESTAURANT_ID_KEY, mRestaurant.getId());
                    intent.putExtra(SELECTED_DATE_KEY, mSelectedDate);
                    intent.putExtra(SELECTED_TIME_KEY, mSelectedTime);
                    intent.putExtra(SELECTED_NUMBER_PEOPLE_KEY, mSelectedNumberOfPeople);
                    intent.setAction(ACTION_RESERVE);
                    startActivity(intent);
                }
            }
        });
    }
    //==============================================================================================
    private void findViewsById() {
        mScrollView = findViewById(R.id.restaurant_profile_scroll_view);
        mRestaurantImageImageView = findViewById(R.id.iv_restaurant_profile_restaurant_image); //
        mFavouriteIconImageView = findViewById(R.id.iv_restaurant_profile_favourite); //
//        mArrowBackImageView = findViewById(R.id.iv_restaurant_profile_arrow_back); //
        mGalleryIconImageView = findViewById(R.id.iv_restaurant_profile_instagram_icon); //
        mLoadingIndicator = findViewById(R.id.pb_restaurant_profile_loading_indicator); //
        mRestaurantNameTextView = findViewById(R.id.tv_restaurant_profile_restaurant_name); //
//        mRestaurantRateRatingBar = findViewById(R.id.rb_restaurant_profile_restaurant_rate); //

        // Restaurant Information Layout Views
        mInformationLayout = findViewById(R.id.restaurant_profile_information_layout);
        mRestaurantDescriptionTextView = findViewById(R.id.tv_restaurant_profile_description); //
        mCuisineTextView = findViewById(R.id.tv_cuisine_value); //
        mExecutiveChefTextView = findViewById(R.id.tv_executive_chef_value); //
//        mCertificateTextView = findViewById(R.id.tv_certificate_value); //
//        mCertificateLayout = findViewById(R.id.restaurant_profile_certificate_layout); //
        mWineTextView = findViewById(R.id.tv_wine_value); //
        mVisitorsTextView = findViewById(R.id.tv_visitors_value); //
//        mNeighbourhoodTextView = findViewById(R.id.tv_neighbourhood_value); //
        mRestaurantAddressTextView = findViewById(R.id.tv_restaurant_profile_location_address); //
//        mRestaurantCityTextView = findViewById(R.id.tv_restaurant_profile_location_city); //
//        mRestaurantPhoneNumberTextView = findViewById(R.id.tv_restaurant_profile_phone_number); //
//        mRestaurantNotesTextView = findViewById(R.id.tv_restaurant_profile_notes); //
        //==========================================================================================
        mLocationLayout = findViewById(R.id.restaurant_profile_location_layout);
        mPhoneNumberLayout = findViewById(R.id.restaurant_profile_phone_number_layout);
        mMenuLayout = findViewById(R.id.restaurant_profile_menu_layout);
        mWebsiteLayout = findViewById(R.id.restaurant_profile_website_layout);
        mWorkingHoursLayout = findViewById(R.id.restaurant_profile_working_hours_layout);
        //==========================================================================================
        mDateTimeLayout = findViewById(R.id.date_time_number_of_people_layout);
        mDateTextView = findViewById(R.id.tv_date_text);
//        mNumberOfPeopleTextView = findViewById(R.id.tv_number_of_people_text);
        mCalendarLayout = findViewById(R.id.restaurant_profile_calendar_layout);
        mMonthTextView = findViewById(R.id.tv_month_text);
        mCalendarView = findViewById(R.id.calendar_view);
        mTimeRecyclerView = findViewById(R.id.rv_calendar_time_list);
        mNumberOfPeopleLabelTextView = findViewById(R.id.tv_calendar_layout_number_of_people_label);
        mNumberOfPeopleRecyclerView = findViewById(R.id.rv_calendar_number_of_people_list);
        mRestaurantIsClosedTextView = findViewById(R.id.tv_calendar_layout_restaurant_is_closed_message);
        mNoCapacityTextView = findViewById(R.id.tv_calendar_layout_no_capacity_message);
        mReserveLayout = findViewById(R.id.layout_reserve_place_an_order);
        mReserveButton = findViewById(R.id.btn_calendar_layout_reserve);
        mPlaceAnOrderButton = findViewById(R.id.btn_calendar_layout_place_an_order);
        mCalendarLoadingIndicator = findViewById(R.id.pb_calendar_layout_loading_indicator);


        mNumberOfPeopleLabelTextView.setVisibility(View.GONE);
        mNumberOfPeopleRecyclerView.setVisibility(View.GONE);
        mNoCapacityTextView.setVisibility(View.GONE);
    }

    private void updateViewsWithRestaurantData() {
        mLanguage = Locale.getDefault().getDisplayLanguage();
        mUserId = SharedPreferencesHelper.getSharedPreferenceInt(this, ConstantsHelper.KEY_USER_ID, -10);

        Picasso.with(this).load(mRestaurant.getPicture()).into(mRestaurantImageImageView);
        if (mRestaurant.getFavourite().equals("true")) {
            mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_pink);
        } else {
            mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_empty);
        }
        mRestaurantNameTextView.setText(String.valueOf(mRestaurant.getName()));
//        if (!TextUtils.isEmpty(mRestaurant.getRate().toString())) {
////            mRestaurantRateRatingBar.setCount(Integer.parseInt(mRestaurant.getRate().toString()));
//        }
        mRestaurantDescriptionTextView.setText(String.valueOf(mRestaurant.getDescription()));
        if (mLanguage.equals("français")) {
            mCuisineTextView.setText(String.valueOf(mRestaurant.getCuisineName()));
            mExecutiveChefTextView.setText(String.valueOf(mRestaurant.getChef()));
            if (TextUtils.isEmpty(String.valueOf(mRestaurant.getCertification()))) {
//                mCertificateLayout.setVisibility(View.GONE);
            } else {
//                mCertificateTextView.setText(mRestaurant.getCertification());
            }
            mWineTextView.setText(mRestaurant.getCertification() + "/" + String.valueOf(mRestaurant.getAlcohol()));
            if ((mRestaurant.getVisitors()) > 1000){
                String formattedVistors = new DecimalFormat("##.#").format((mRestaurant.getVisitors()) / 1000 );
                mVisitorsTextView.setText(formattedVistors + " K");
            }else{
                mVisitorsTextView.setText(String.valueOf(mRestaurant.getVisitors()));
            }

//            mNeighbourhoodTextView.setText(String.valueOf(mRestaurant.getCityNameFr()));
            mRestaurantAddressTextView.setText(String.valueOf(mRestaurant.getAddress()));
//            mRestaurantCityTextView.setText(String.valueOf(mRestaurant.getCityNameFr()));
//            mRestaurantPhoneNumberTextView.setText(String.valueOf(mRestaurant.getPhone()));
//            mRestaurantNotesTextView.setText(String.valueOf(mRestaurant.getNotes()));
        } else {
            mCuisineTextView.setText(String.valueOf(mRestaurant.getCuisineName()));
            mExecutiveChefTextView.setText(String.valueOf(mRestaurant.getChef()));
            if (TextUtils.isEmpty(String.valueOf(mRestaurant.getCertification()))) {
//                mCertificateLayout.setVisibility(View.GONE);
            } else {
//                mCertificateTextView.setText(mRestaurant.getCertification());
            }
            mWineTextView.setText(mRestaurant.getCertification() + "/" + String.valueOf(mRestaurant.getAlcohol()));
            if ((mRestaurant.getVisitors()) > 1000){
                String formattedVistors = new DecimalFormat("##.#").format((mRestaurant.getVisitors()) / 1000 );
                mVisitorsTextView.setText(formattedVistors + " K");
            }else{
                mVisitorsTextView.setText(String.valueOf(mRestaurant.getVisitors()));
            }
//            mNeighbourhoodTextView.setText(String.valueOf(mRestaurant.getCityNameEn()));
            mRestaurantAddressTextView.setText(String.valueOf(mRestaurant.getAddress()));
//            mRestaurantCityTextView.setText(String.valueOf(mRestaurant.getCityNameEn()));
//            mRestaurantPhoneNumberTextView.setText(String.valueOf(mRestaurant.getPhone()));
//            mRestaurantNotesTextView.setText(String.valueOf(mRestaurant.getNotes()));
        }
    }

    private void requestRestaurentData(){
        int restaurentId = getIntent().getIntExtra(RESTAURENT_KEY, 0);
        RestaurentProfileWebService webService = RetrofitWebService.retrofit.create(RestaurentProfileWebService.class);
        int userId = SharedPreferencesHelper.getSharedPreferenceInt(RestaurantProfileActivity.this, ConstantsHelper.KEY_USER_ID, -10);
        Call<RestaurantProfile> restaurentProfile = webService.getRestaurentProfile(restaurentId,userId);
        restaurentProfile.enqueue(new Callback<RestaurantProfile>() {
            @Override
            public void onResponse(Call<RestaurantProfile> call, Response<RestaurantProfile> response) {
                mRestaurant = response.body();
                updateViewsWithRestaurantData();
            }

            @Override
            public void onFailure(Call<RestaurantProfile> call, Throwable t) {

            }
        });
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void restaurantImageClick() {
        mRestaurantImageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(RestaurantProfileActivity.this);
                if (isNetworkOk) {
                    Intent intent = new Intent(RestaurantProfileActivity.this, GalleryActivity.class);
                    intent.putExtra(RESTAURANT_NAME_KEY, mRestaurant.getName());
                    intent.putExtra(RESTAURANT_ID_KEY, mRestaurant.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void favouriteIconClick() {
        mFavouriteIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(RestaurantProfileActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, false);
                isNetworkOk = NetworkHelper.hasNetworkAccess(RestaurantProfileActivity.this);
                if (isNetworkOk) {
                    if (isLoggedIn) {
                        int userId = SharedPreferencesHelper.getSharedPreferenceInt(RestaurantProfileActivity.this, ConstantsHelper.KEY_USER_ID, -10);
                        showLoadingIndicator();
                        markAsFavourite(userId, mRestaurant.getId());
                    } else {
                        Intent intent = new Intent(RestaurantProfileActivity.this, FavouriteRestaurantsActivity.class);
                        intent.putExtra(RESTAURANT_ID_KEY, mRestaurant.getId());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    private void arrowBackClick() {
//        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mAction != null) {
//                    // When comes from HomeFragment
//                    if (mAction.equals(ConstantsHelper.ACTION_HOME_FRAGMENT)) {
//                        Intent intent = new Intent(RestaurantProfileActivity.this, MainActivity.class);
//                        intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
//                        startActivity(intent);
//                    }
//                    // When comes from TableRestaurantsActivity
//                    else if (mAction.equals(ConstantsHelper.ACTION_TABLE_RESTAURANT_ACTIVITY)) {
//                        Intent intent = new Intent(RestaurantProfileActivity.this, TableRestaurantsActivity.class);
//                        intent.putExtra(HomeFragment.TABLE_ID_KEY, getIntent().getIntExtra(HomeFragment.TABLE_ID_KEY, -10));
//                        intent.putExtra(HomeFragment.TABLE_OBJECT_KEY, mTable);
//                        startActivity(intent);
//                    }
//                    // When comes from SearchFragment
//                    else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
//                        Intent intent = new Intent(RestaurantProfileActivity.this, MainActivity.class);
//                        intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
//                        startActivity(intent);
//                    }
//                    else if (mAction.split("-").length > 1) {
//                        // When comes from FavouriteRestaurantsActivity
//                        if (mAction.equals(mAction.split("-")[0] + "-" + mAction.split("-")[1] + "-" + ConstantsHelper.ACTION_FAVOURITE_RESTAURANTS_ACTIVITY)) {
//                            Intent intent = new Intent(RestaurantProfileActivity.this, FavouriteRestaurantsActivity.class);
//                            intent.setAction(mAction.split("-")[0] + "-" + mAction.split("-")[1]);
//                            startActivity(intent);
//                        }
//                        else if (mAction.equals(mAction.split("-")[0] + "-" + ConstantsHelper.ACTION_FAVOURITE_RESTAURANTS_ACTIVITY)) {
//                            Intent intent = new Intent(RestaurantProfileActivity.this, FavouriteRestaurantsActivity.class);
//                            intent.setAction(mAction.split("-")[0]);
//                            startActivity(intent);
//                        }
//                    }
//                    else if (mAction.equals(ConstantsHelper.ACTION_RESERVE_FRAGMENT)) {
//                        Intent intent = new Intent(RestaurantProfileActivity.this, MainActivity.class);
//                        intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
//    }

    private void galleryIconClick() {
        mGalleryIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantProfileActivity.this, GalleryActivity.class);
                intent.putExtra(RESTAURANT_NAME_KEY, mRestaurant.getName());
                startActivity(intent);
            }
        });
    }

//    private void phoneNumberLayoutClick() {
//        mPhoneNumberLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String phoneNumber = String.format("tel: %s", mRestaurant.getPhone());
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse(phoneNumber));
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                }
//            }
//        });
//    }

    private void menuLayoutClick() {
        mMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantName = mRestaurant.getName();
                Intent intent = new Intent(RestaurantProfileActivity.this, MenuActivity.class);
                intent.putExtra(RESTAURANT_NAME_KEY, restaurantName);
                intent.putExtra(RESTAURANT_ID_KEY, mRestaurant.getId());
                startActivity(intent);
            }
        });
    }

    private void websiteLayoutClick() {
        mWebsiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(mRestaurant.getWebsite())));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

//    private void workingHoursLayoutClick() {
//        mWorkingHoursLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String restaurantName = mRestaurant.getName();
//                List<WorkDay> workDaysList = mRestaurant.getWorkDays();
//                Intent intent = new Intent(RestaurantProfileActivity.this, WorkingHoursActivity.class);
//                intent.putExtra("RestaurantName", restaurantName);
//                intent.putParcelableArrayListExtra("workDaysList", (ArrayList<? extends Parcelable>) workDaysList);
//                startActivity(intent);
//            }
//        });
//    }
    //==============================================================================================


    @Override
    public void onBackPressed() {
        if (isCalendarVisible) {
            mCalendarLayout.setVisibility(View.GONE);
            mInformationLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.restaurant_profile_scroll_view).scrollTo(0, 0);
            isCalendarVisible = false;
        } else {
            if (mAction != null) {

                // When comes from HomeFragment
                if (mAction.equals(ConstantsHelper.ACTION_HOME_FRAGMENT)) {
                    Intent intent = new Intent(RestaurantProfileActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
                    startActivity(intent);
                }
                // When comes from TableRestaurantsActivity
                else if (mAction.equals(ConstantsHelper.ACTION_TABLE_RESTAURANT_ACTIVITY)) {
                    Intent intent = new Intent(RestaurantProfileActivity.this, TableRestaurantsActivity.class);
                    intent.putExtra(HomeFragment.TABLE_ID_KEY, getIntent().getIntExtra(HomeFragment.TABLE_ID_KEY, -10));
                    intent.putExtra(HomeFragment.TABLE_OBJECT_KEY, mTable);
                    startActivity(intent);
                }
                // When comes from SearchFragment
                else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
                    Intent intent = new Intent(RestaurantProfileActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_SEARCH_FRAGMENT);
                    startActivity(intent);
                }
                else if (mAction.split("-").length > 1) {
                    // When comes from FavouriteRestaurantsActivity
                    if (mAction.equals(mAction.split("-")[0] + "-" + mAction.split("-")[1] + "-" + ConstantsHelper.ACTION_FAVOURITE_RESTAURANTS_ACTIVITY)) {
                        Intent intent = new Intent(RestaurantProfileActivity.this, FavouriteRestaurantsActivity.class);
                        intent.setAction(mAction.split("-")[0] + "-" + mAction.split("-")[1]);
                        startActivity(intent);
                    }
                    else if (mAction.equals(mAction.split("-")[0] + "-" + ConstantsHelper.ACTION_FAVOURITE_RESTAURANTS_ACTIVITY)) {
                        Intent intent = new Intent(RestaurantProfileActivity.this, FavouriteRestaurantsActivity.class);
                        intent.setAction(mAction.split("-")[0]);
                        startActivity(intent);
                    }
                }
                else if (mAction.equals(ConstantsHelper.ACTION_RESERVE_FRAGMENT)) {
                    Intent intent = new Intent(RestaurantProfileActivity.this, MainActivity.class);
                    intent.setAction(ConstantsHelper.ACTION_RESERVE_FRAGMENT);
                    startActivity(intent);
                }
            }
        }
    }

    private void markAsFavourite(int userId, int restaurantId) {
        if (userId != -10 && restaurantId >= 0) {
            RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
            Call<Object> resultCall = webService.markAsFavourite(userId, restaurantId);
            resultCall.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    hideLoadingIndicator();
                    if (response.isSuccessful()) {
                        if (mRestaurant.getFavourite().equals("false")) {
                            mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_pink);
                            mRestaurant.setFavourite("true");

                            if (mTable != null) {
                                for (int i = 0; i < mTable.getRestaurants().size(); i ++) {
//                                    Restaurant restaurant = mTable.getRestaurants().get(i);
//                                    if (restaurant.getId() == mRestaurant.getId()) {
//                                        restaurant.setFavourite("true");
//                                    }
                                }
                            }

                        } else {
                            mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_empty);
                            mRestaurant.setFavourite("false");

                            if (mTable != null) {
                                for (int i = 0; i < mTable.getRestaurants().size(); i ++) {
//                                    Restaurant restaurant = mTable.getRestaurants().get(i);
//                                    if (restaurant.getId() == mRestaurant.getId()) {
//                                        restaurant.setFavourite("false");
//                                    }
                                }
                            }
                        }
                    }

                    else {
                        Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_an_error_has_occurred), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    hideLoadingIndicator();
                    Toast.makeText(RestaurantProfileActivity.this, getResources().getString(R.string.toast_message_an_error_has_occurred), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Failed to get results");
                    Log.d(TAG, t.getMessage());
                }
            });
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
