package com.example.alexander.halalappv1.activities;

import android.content.Intent;
import android.os.Parcelable;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.adapters.NumberOfPeopleAdapter;
import com.example.alexander.halalappv1.adapters.TimeAdapter;
import com.example.alexander.halalappv1.fragments.ReserveFragment;
import com.example.alexander.halalappv1.model.Gallery;
import com.example.alexander.halalappv1.model.Menu;
import com.example.alexander.halalappv1.model.MenuItem;
import com.example.alexander.halalappv1.model.Restaurant;
import com.example.alexander.halalappv1.model.WorkDay;
import com.example.alexander.halalappv1.reservation.UpComingReservation;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
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
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReservationActivity extends AppCompatActivity {

    private static final String TAG = "XXXXX";
    public static final String ACTION_EDIT_RESERVE = "ActionEditReserve";
    public static final String UPCOMING_RESERVATION_OBJECT_KEY = "UpcomingReservationObject";
    public static final String SELECTED_DATE_KEY = "SelectedDate";
    public static final String SELECTED_TIME_KEY = "SelectedTime";
    public static final String SELECTED_NUMBER_PEOPLE_KEY = "SelectedNumberOfPeople";

    private ImageView mRestaurantImageImageView;
    private ImageView mFavouriteIconImageView;
    private ImageView mArrowBackImageView;
    private ImageView mGalleryIconImageView;
    private TextView mRestaurantNameTextView;
    private RatingBar mRestaurantRateRatingBar;

    private ConstraintLayout mDateTimeLayout;
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
    private ArrayList<String> mTimeList = new ArrayList<>();
    boolean isListItemClickable;
    private NumberOfPeopleAdapter mNumberOfPeopleAdapter;
    private ArrayList<String> mNumberOfPeopleList = new ArrayList<>();

    private UpComingReservation mUpComingReservation;
    private Restaurant mRestaurant;
    private int mUserId;
    private String mSelectedDate;
    private String mSelectedTime;
    private String mSelectedNumberOfPeople;

    private int mTotalQuantity;
    //==============================================================================================
    private void findViewsById() {
        mRestaurantImageImageView = findViewById(R.id.iv_edit_reservation_activity_restaurant_image);
        mFavouriteIconImageView = findViewById(R.id.iv_edit_reservation_activity_favourite);
        mArrowBackImageView = findViewById(R.id.iv_edit_reservation_activity_arrow_back);
        mGalleryIconImageView = findViewById(R.id.iv_edit_reservation_activity_gallery_icon);
        mRestaurantNameTextView = findViewById(R.id.tv_edit_reservation_activity_restaurant_name);
        mRestaurantRateRatingBar = findViewById(R.id.rb_edit_reservation_activity_restaurant_rate);

        mDateTimeLayout = findViewById(R.id.edit_reservation_activity_date_time_number_of_people_layout);
        mDateTextView = findViewById(R.id.tv_date_text);
        mNumberOfPeopleTextView = findViewById(R.id.tv_number_of_people_text);
        mCalendarLayout = findViewById(R.id.edit_reservation_activity_calendar_layout);
        mMonthTextView = findViewById(R.id.tv_month_text);
        mCalendarView = findViewById(R.id.calendar_view);
        mTimeRecyclerView = findViewById(R.id.rv_calendar_time_list);
        mNumberOfPeopleRecyclerView = findViewById(R.id.rv_calendar_number_of_people_list);
        mRestaurantIsClosedTextView = findViewById(R.id.tv_calendar_layout_restaurant_is_closed_message);
        mReserveButton = findViewById(R.id.btn_calendar_layout_reserve);
        mPlaceAnOrderButton = findViewById(R.id.btn_calendar_layout_place_an_order);
        findViewById(R.id.edit_reservation_scroll_view).scrollTo(0, mCalendarLayout.getHeight());
    }

    private void updateMainViewsWithRestaurantData() {
        if (mRestaurant != null) {
            Picasso.with(this).load(mRestaurant.getImage()).into(mRestaurantImageImageView);
            if (mRestaurant.getFavourite().equals("true")) {
                mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_pink);
            } else {
                mFavouriteIconImageView.setImageResource(R.drawable.ic_favourite_empty);
            }
            mRestaurantNameTextView.setText(mRestaurant.getName());
            mRestaurantRateRatingBar.setCount(Integer.parseInt(mRestaurant.getRate()));
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
                mSelectedTime = mTimeList.get(position);
                mTimeAdapter.notifyDataSetChanged();
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
                mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople);
                mNumberOfPeopleAdapter.notifyDataSetChanged();
            }
        });

        mNumberOfPeopleList.add("1");
        mNumberOfPeopleList.add("2");
        mNumberOfPeopleList.add("3");
        mNumberOfPeopleList.add("4");
        mNumberOfPeopleList.add("5");
        mNumberOfPeopleList.add("6");
        mNumberOfPeopleList.add("7");
        mNumberOfPeopleList.add("9");
        mNumberOfPeopleList.add("10");
        mNumberOfPeopleList.add("11");
        mNumberOfPeopleList.add("12");
        mNumberOfPeopleList.add("13");
        mNumberOfPeopleList.add("14");
        mNumberOfPeopleList.add("15");
        mNumberOfPeopleAdapter.setNumberOfPeopleList(mNumberOfPeopleList);
        mNumberOfPeopleRecyclerView.setAdapter(mNumberOfPeopleAdapter);
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
            Date date = new Date();
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

                List<WorkDay> workDaysList = mRestaurant.getWorkDays();
                for (int i = 0; i < workDaysList.size(); i ++) {
                    if (workDaysList.get(i).getDayName().equalsIgnoreCase(selectedDayOfTheWeek)) {
                        WorkDay workDay = workDaysList.get(i);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(mUpComingReservation.getBookingData().getReservationDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String currentDayOfTheWeek = getSelectedDayOfTheWeek(outFormat.format(date));
        //==========================================================================================
        List<WorkDay> workDaysList = mRestaurant.getWorkDays();
        for (int i = 0; i < workDaysList.size(); i ++) {
            if (workDaysList.get(i).getDayName().equalsIgnoreCase(currentDayOfTheWeek)) {
                WorkDay workDay = workDaysList.get(i);
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

        mSelectedTime = mUpComingReservation.getBookingData().getReservationTime();
        for (int i = 0; i < mTimeList.size(); i ++) {
            if (mTimeList.get(i).equals(mSelectedTime)) {
                mTimeAdapter.setClickedItem(i);
                mSelectedTime = mTimeList.get(i);
                mTimeAdapter.notifyDataSetChanged();
                if (i > 1 && i < mTimeList.size() - 1) {
                    mTimeRecyclerView.scrollToPosition(i - 1);
                } else {
                    mTimeRecyclerView.scrollToPosition(i);
                }
            }
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
                if (mUpComingReservation != null) {
                    for (int i = 0; i < mUpComingReservation.getRestaurant().getMenus().size(); i ++) {
                        List<MenuItem> menuItemsList = mUpComingReservation.getRestaurant().getMenus().get(i).getMenuItems();
                        for (int j = 0; j < menuItemsList.size(); j ++) {
                            MenuItem menuItem = menuItemsList.get(j);
                            menuItem.setQuantity(0);
                        }
                    }
                }

                if (mSelectedDate == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_date), Toast.LENGTH_LONG).show();
                } else if (mSelectedTime == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_time), Toast.LENGTH_LONG).show();
                } else if (mSelectedNumberOfPeople == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_number_of_people), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(EditReservationActivity.this, SubmitReservationActivity.class);
                    intent.putExtra(UPCOMING_RESERVATION_OBJECT_KEY, mUpComingReservation);
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
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_date), Toast.LENGTH_LONG).show();
                } else if (mSelectedTime == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_time), Toast.LENGTH_LONG).show();
                } else if (mSelectedNumberOfPeople == null) {
                    Toast.makeText(EditReservationActivity.this, getResources().getString(R.string.toast_message_select_number_of_people), Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(EditReservationActivity.this, MenuActivity.class);
                    intent.putExtra(UPCOMING_RESERVATION_OBJECT_KEY, mUpComingReservation);
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
        mUpComingReservation = getIntent().getParcelableExtra(ReserveFragment.EDIT_RESERVATION_OBJECT_KEY);

        findViewsById();
        setUpTimeRecyclerView();
        setUpNumberOfPeopleRecyclerView();
        setUpCalendarView();

        if (mUpComingReservation != null) {
            mRestaurant = mUpComingReservation.getRestaurant();
            try {
                mSelectedDate = mUpComingReservation.getBookingData().getReservationDate();
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

            mSelectedNumberOfPeople = mUpComingReservation.getBookingData().getNumberOfPeople();
            mNumberOfPeopleTextView.setText(mSelectedNumberOfPeople);
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

            for (int i = 0; i < mUpComingReservation.getRestaurant().getMenus().size(); i ++) {
                List<MenuItem> menuItemsList = mUpComingReservation.getRestaurant().getMenus().get(i).getMenuItems();
                for (int j = 0; j < menuItemsList.size(); j ++) {
                    MenuItem menuItem = menuItemsList.get(j);
                    mTotalQuantity += menuItem.getQuantity();
                }
            }
            if (mTotalQuantity > 0) {
                mReserveButton.setVisibility(View.GONE);
            }
        }

        updateMainViewsWithRestaurantData();
        updateTimeRecyclerViewWithCurrentDayWorkingHours();
        //==========================================================================================
        favouriteIconClick();
        arrowBackClick();
        galleryIconClick();
        reserveButtonClick();
        placeAnOrderButtonClick();
        //==========================================================================================
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
}
