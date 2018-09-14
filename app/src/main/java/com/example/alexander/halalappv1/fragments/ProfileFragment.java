package com.example.alexander.halalappv1.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.AccountInfoActivity;
import com.example.alexander.halalappv1.activities.ConditionsActivity;
import com.example.alexander.halalappv1.activities.FavouriteRestaurantsActivity;
import com.example.alexander.halalappv1.activities.MainActivity;
import com.example.alexander.halalappv1.activities.PoliciesActivity;
import com.example.alexander.halalappv1.activities.ProfileSettingsActivity;
import com.example.alexander.halalappv1.adapters.HomeRestaurantAdapter;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements HomeRestaurantAdapter.OnRestaurantClickListener{

    public ProfileFragment() {}

    private TextView mUserNameTextView;
    private ConstraintLayout mUserNameLayout;
    private ConstraintLayout conditionLayout;
    private ConstraintLayout politiqueLayout;
    private ConstraintLayout mFavouriteRestaurantsLayout;
    private RecyclerView mFavouriteRecyclerView;
    private ArrayList<Restaurant> mFavResList;
    private HomeRestaurantAdapter mRestaurantAdapter;
    private TextView loggedSignOut;
    private TextView unLoggedSignIn;
    private TextView unLoggedSignUp;

    //==============================================================================================
    private void updateUserNameView() {
        boolean isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            loggedSignOut.setVisibility(View.VISIBLE);
            String firstName = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_FIRST_NAME, null);
            String familyName = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_FAMILY_NAME, null);
            if (firstName != null && familyName != null) {
                mUserNameTextView.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + familyName.substring(0, 1).toUpperCase() + familyName.substring(1));
            }
        } else {
            unLoggedSignIn.setVisibility(View.VISIBLE);
            unLoggedSignUp.setVisibility(View.VISIBLE);
            mUserNameTextView.setText(getResources().getString(R.string.tv_profile_fragment_user_name_text));
        }
    }

    private void userNameLayoutClick() {
        mUserNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void favouriteRestaurantsLayoutClick() {
        mFavouriteRestaurantsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavouriteRestaurantsActivity.class);
                intent.setAction(ConstantsHelper.ACTION_PROFILE_FRAGMENT);
                startActivity(intent);
            }
        });
    }
    //==============================================================================================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mUserNameTextView = rootView.findViewById(R.id.tv_profile_fragment_user_name);
        mUserNameLayout = rootView.findViewById(R.id.myInformationLayout);
        conditionLayout = rootView.findViewById(R.id.conditionsLayout);
        politiqueLayout = rootView.findViewById(R.id.politiqueLayout);
        loggedSignOut = rootView.findViewById(R.id.tv_profile_settings_sign_out);
        unLoggedSignIn = rootView.findViewById(R.id.tv_profile_settings_sign_in);
        unLoggedSignUp = rootView.findViewById(R.id.tv_profile_settings_sign_up);
        mFavouriteRestaurantsLayout = rootView.findViewById(R.id.profile_fragment_favourite_restaurants_layout);
        mFavouriteRecyclerView = rootView.findViewById(R.id.myFavRecyclerView);

        updateUserNameView();

        userNameLayoutClick();

        favouriteRestaurantsLayoutClick();

        getFavRestaurents();

        conditionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConditionsActivity.class);
                startActivity(intent);
            }
        });
        politiqueLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PoliciesActivity.class);
                startActivity(intent);
            }
        });

        loggedSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				SharedPreferencesHelper.setSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, false);
				SharedPreferencesHelper.setSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, 0);
				SharedPreferencesHelper.setSharedPreferenceString(getContext(), ConstantsHelper.KEY_FIRST_NAME, null);
            	Intent intent = new Intent(getActivity(), MainActivity.class);
            	startActivity(intent);
            }
        });
        unLoggedSignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
        unLoggedSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
//		loggedSignOut.setVisibility(View.VISIBLE);
        return rootView;
    }

    private void getFavRestaurents(){
        int userId = SharedPreferencesHelper.getSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, 0);
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<ArrayList<Restaurant>> favRestaurents = webService.getFavouriteRestaurants(userId);
        favRestaurents.enqueue(new Callback<ArrayList<Restaurant>>() {
            @Override
            public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                mFavResList = response.body();
                mFavouriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
                mRestaurantAdapter = new HomeRestaurantAdapter(getContext(), ProfileFragment.this);
                mRestaurantAdapter.setRestaurantList(mFavResList);
                mRestaurantAdapter.setTablePosition(0);
                mFavouriteRecyclerView.setAdapter(mRestaurantAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {

            }
        });



    }

    @Override
    public void onRestaurantClick(int parentPosition, int childPosition) {

    }
}
