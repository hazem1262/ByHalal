package com.example.alexander.halalappv1.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.alexander.halalappv1.activities.RestaurantProfileActivity;
import com.example.alexander.halalappv1.adapters.FavouriteRestaurentAdapter;
import com.example.alexander.halalappv1.adapters.HomeRestaurantAdapter;
import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.services.FavouriteRestaurents;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.fragments.HomeFragment.RESTAURENT_KEY;
import static com.example.alexander.halalappv1.fragments.SearchFragment.RESTAURANT_OBJECT_KEY;
import static com.example.alexander.halalappv1.utils.ConstantsHelper.SIGNUP;

public class ProfileFragment extends Fragment implements HomeRestaurantAdapter.OnRestaurantClickListener{

    public ProfileFragment() {}

    private TextView mUserNameTextView;
    private ConstraintLayout mUserNameLayout;
    private ConstraintLayout conditionLayout;
    private ConstraintLayout politiqueLayout;
    private ConstraintLayout noFavLayout;
    private ConstraintLayout mFavouriteRestaurantsLayout;
    private RecyclerView mFavouriteRecyclerView;
    private ArrayList<Restaurant> mFavResList;
    private FavouriteRestaurentAdapter mRestaurantAdapter;
    private TextView loggedSignOut;
    private TextView unLoggedSignIn;
    private TextView unLoggedSignUp;
    private ConstraintLayout mLoadingIndicator;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mUserNameTextView = rootView.findViewById(R.id.tv_profile_fragment_user_name);
        mUserNameLayout = rootView.findViewById(R.id.myInformationLayout);
        conditionLayout = rootView.findViewById(R.id.conditionsLayout);
        politiqueLayout = rootView.findViewById(R.id.politiqueLayout);
        noFavLayout = rootView.findViewById(R.id.noFavouritesLayout);
        loggedSignOut = rootView.findViewById(R.id.tv_profile_settings_sign_out);
        unLoggedSignIn = rootView.findViewById(R.id.tv_profile_settings_sign_in);
        unLoggedSignUp = rootView.findViewById(R.id.tv_profile_settings_sign_up);
        mFavouriteRestaurantsLayout = rootView.findViewById(R.id.profile_fragment_favourite_restaurants_layout);
        mFavouriteRecyclerView = rootView.findViewById(R.id.myFavRecyclerView);
        mLoadingIndicator = rootView.findViewById(R.id.pb_table_restaurants_activity_loading_indicator);

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

                showSignOutAlertDialog();
            }
        });
        unLoggedSignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
                startActivity(intent);
			}
		});
        unLoggedSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
                intent.putExtra(SIGNUP,true);
                startActivity(intent);
			}
		});
//		loggedSignOut.setVisibility(View.VISIBLE);
        return rootView;
    }

    //==============================================================================================
    private void showSignOutAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_sign_out, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView cancelTextView = dialogView.findViewById(R.id.tv_sign_out_alert_dialog_action_cancel);
        TextView signOutTextView = dialogView.findViewById(R.id.tv_sign_out_alert_dialog_action_sign_out);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        signOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_USER_ID);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_USER_FACEBOOK_ID);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_FIRST_NAME);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_FAMILY_NAME);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_EMAIL);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_MOBILE_NUMBER);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_PASSWORD);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_SELECTED_LANGUAGE);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_SELECTED_CITY);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_CITY_LATITUDE);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_CITY_LONGITUDE);
                SharedPreferencesHelper.removeFromSharedPreference(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN);

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
                startActivity(intent);
            }
        });
    }

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

    private void getFavRestaurents(){
        showLoadingIndicator();
        int userId = SharedPreferencesHelper.getSharedPreferenceInt(getContext(), ConstantsHelper.KEY_USER_ID, 0);
		FavouriteRestaurents webService = RetrofitWebService.retrofit.create(FavouriteRestaurents.class);
        Call<ArrayList<Restaurant>> favRestaurents = webService.getFavouriteRestaurants(userId);
        favRestaurents.enqueue(new Callback<ArrayList<Restaurant>>() {
            @Override
            public void onResponse(Call<ArrayList<Restaurant>> call, Response<ArrayList<Restaurant>> response) {
                hideLoadingIndicator();
                mFavResList = response.body();
                mFavouriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
                mRestaurantAdapter = new FavouriteRestaurentAdapter(getContext(), ProfileFragment.this);
                mRestaurantAdapter.setRestaurantList(mFavResList);
                mFavouriteRecyclerView.setAdapter(mRestaurantAdapter);
                if (mFavResList == null || mFavResList.isEmpty()){
					noFavLayout.setVisibility(View.VISIBLE);
				}
            }
            @Override
            public void onFailure(Call<ArrayList<Restaurant>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRestaurantClick(int parentPosition, int childPosition) {
		Restaurant restaurant = mFavResList.get(childPosition);
		Intent intent = new Intent(getActivity(), RestaurantProfileActivity.class);
		intent.putExtra(RESTAURENT_KEY, restaurant.getId());
		intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
		startActivity(intent);
		Log.i("restaurent", "parentPosition: " + parentPosition + "childPosition: " + childPosition);
	}
    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }
}
