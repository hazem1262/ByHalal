package com.example.alexander.halalappv1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.FavouriteRestaurantsActivity;
import com.example.alexander.halalappv1.activities.ProfileSettingsActivity;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {}

    private TextView mUserNameTextView;
    private ConstraintLayout mUserNameLayout;
    private ConstraintLayout mFavouriteRestaurantsLayout;
    //==============================================================================================
    private void updateUserNameView() {
        boolean isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(getContext(), ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            String firstName = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_FIRST_NAME, null);
            String familyName = SharedPreferencesHelper.getSharedPreferenceString(getContext(), ConstantsHelper.KEY_FAMILY_NAME, null);
            if (firstName != null && familyName != null) {
                mUserNameTextView.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + familyName.substring(0, 1).toUpperCase() + familyName.substring(1));
            }
        } else {
            mUserNameTextView.setText(getResources().getString(R.string.tv_profile_fragment_user_name_text));
        }
    }

    private void userNameLayoutClick() {
        mUserNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
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
        mUserNameLayout = rootView.findViewById(R.id.profile_fragment_user_name_layout);
        mFavouriteRestaurantsLayout = rootView.findViewById(R.id.profile_fragment_favourite_restaurants_layout);

        updateUserNameView();

        userNameLayoutClick();

        favouriteRestaurantsLayoutClick();

        return rootView;
    }
}
