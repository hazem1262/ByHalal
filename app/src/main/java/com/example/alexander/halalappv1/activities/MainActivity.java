package com.example.alexander.halalappv1.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.fragments.HomeFragment;
import com.example.alexander.halalappv1.fragments.ProfileFragment;
import com.example.alexander.halalappv1.fragments.ReserveFragment;
import com.example.alexander.halalappv1.fragments.SearchFragment;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ConstraintLayout mLoadingIndicator;
    //==============================================================================================
    private FragmentManager mFragmentManager;
    private HomeFragment mHomeFragment;
    private SearchFragment mSearchFragment;
    private ReserveFragment mReserveFragment;
    private ProfileFragment mProfileFragment;
    //==============================================================================================
    private boolean isNetworkOk;
    private String mAction;
    private String mLanguage;
    //==============================================================================================
    private void findViewsById() {
        mTabLayout = findViewById(R.id.tab_layout);
        mLoadingIndicator = findViewById(R.id.pb_main_activity_loading_indicator);
    }

    private void getLocaleAndUpdateSharedPreferences() {
        mLanguage = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
        if (mLanguage != null) {
            if (mLanguage.equals("français")) {
                String languageToLoad  = "fr";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

                SharedPreferencesHelper.setSharedPreferenceString(MainActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE, "français");
            }
            else {
                String languageToLoad  = "en";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

                SharedPreferencesHelper.setSharedPreferenceString(MainActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE, "English");
            }
        }
        else {
            mLanguage = Locale.getDefault().getDisplayLanguage();
            saveUserDataInSharedPreferences(ConstantsHelper.KEY_SELECTED_LANGUAGE, mLanguage);
        }
    }

    private void instantiateFragments() {
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mSearchFragment = new SearchFragment();
        mReserveFragment = new ReserveFragment();
        mProfileFragment = new ProfileFragment();
    }

    private void getActionAndUpdateMainContainerWithTheRightFragment() {
        mAction = getIntent().getAction();
        if (mAction == null || mAction.length() <= 0) {
            mFragmentManager.beginTransaction()
                    .add(R.id.fragments_main_container, mHomeFragment)
                    .commit();
            mTabLayout.getTabAt(0).select();
        }
        else if (mAction.equals(ConstantsHelper.ACTION_HOME_FRAGMENT)) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragments_main_container, mHomeFragment)
                    .commit();
            mTabLayout.getTabAt(0).select();
        }
        else if (mAction.equals(ConstantsHelper.ACTION_SEARCH_FRAGMENT)) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragments_main_container, mSearchFragment)
                    .commit();
            mTabLayout.getTabAt(1).select();
        }
        else if (mAction.equals(ConstantsHelper.ACTION_RESERVE_FRAGMENT)) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragments_main_container, mReserveFragment)
                    .commit();
            mTabLayout.getTabAt(1).select();
        }
        else if (mAction.equals(ConstantsHelper.ACTION_PROFILE_FRAGMENT)) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragments_main_container, mProfileFragment)
                    .commit();
            mTabLayout.getTabAt(2).select();
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(mTabLayout.getSelectedTabPosition() == 0){
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragments_main_container, mHomeFragment)
                            .commit();
                } else if(mTabLayout.getSelectedTabPosition() == 10){
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragments_main_container, mSearchFragment)
                            .commit();
                } else if(mTabLayout.getSelectedTabPosition() == 1){
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragments_main_container, mReserveFragment)
                            .commit();
                } else if(mTabLayout.getSelectedTabPosition() == 2){
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragments_main_container, mProfileFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void checkForConnectionAndUpdateLayout() {
        isNetworkOk = NetworkHelper.hasNetworkAccess(this);
        if (isNetworkOk) {
            hideLoadingIndicator();
            instantiateFragments();
            getActionAndUpdateMainContainerWithTheRightFragment();
        } else {
            showNoConnectionAlertDialog();
        }
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void enableTabIconsAndSetHomeIconSelected() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_tab_home);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_tab_search);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_tab_profile);
        mTabLayout.getTabAt(3).setIcon(R.drawable.ic_tab_reserve);

        mTabLayout.getTabAt(0).select();
    }

    private void disableTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_search_black);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_reserve_black);
        mTabLayout.getTabAt(3).setIcon(R.drawable.ic_profile_black);
    }
    //==============================================================================================
    private void showNoConnectionAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_no_connection, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        TextView okTextView = dialogView.findViewById(R.id.tv_layout_no_connection_action_ok);
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showLoadingIndicator();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkForConnectionAndUpdateLayout();
                    }
                }, 100);
            }
        });
    }
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewsById(); // (1)

        getLocaleAndUpdateSharedPreferences(); // (2)

        checkForConnectionAndUpdateLayout(); // (3)
    }
    //==============================================================================================
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saveUserDataInSharedPreferences(String key, String value) {
        SharedPreferencesHelper.setSharedPreferenceString(MainActivity.this, key, value);
    }
}
