package com.example.alexander.halalappv1.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.Locale;

public class ProfileSettingsActivity extends AppCompatActivity {

    private ImageView mArrowBackImageView;
    private ConstraintLayout mAccountInfoLayout;
    private ConstraintLayout mAboutHalalLayout;
    private TextView mSignOutTextView;
    private ConstraintLayout mLanguageLayout;
    private ImageView mLanguageIconImageView;
    private ConstraintLayout mSelectLanguageLayout;
    private ConstraintLayout mLanguageEnglishLayout;
    private ImageView mEnglishSelectedIconImageView;
    private ConstraintLayout mLanguageFrenchLayout;
    private ImageView mFrenchSelectedIconImageView;

    private String mLanguage;
    private boolean isSelectLanguageLayoutVisible = false;
    private boolean isLoggedIn;

    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, MainActivity.class);
                intent.setAction(ConstantsHelper.ACTION_PROFILE_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    private void accountInfoLayoutClick() {
        mAccountInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AccountInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void aboutHalalLayoutClick() {
        mAboutHalalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileSettingsActivity.this, AboutHalalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signOutClick() {
        mSignOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutAlertDialog();
            }
        });
    }

    private void showSignOutAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_sign_out, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

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
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_USER_ID);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_USER_FACEBOOK_ID);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_FIRST_NAME);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_FAMILY_NAME);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_EMAIL);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_MOBILE_NUMBER);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_PASSWORD);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_SELECTED_CITY);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_CITY_LATITUDE);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_CITY_LONGITUDE);
                SharedPreferencesHelper.removeFromSharedPreference(ProfileSettingsActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN);

                Intent intent = new Intent(ProfileSettingsActivity.this, MainActivity.class);
                intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        mArrowBackImageView = findViewById(R.id.profile_settings_arrow_back);
        mAccountInfoLayout = findViewById(R.id.profile_settings_account_info_layout);
        mAboutHalalLayout = findViewById(R.id.profile_settings_about_by_hallal_layout);
        mSignOutTextView = findViewById(R.id.tv_profile_settings_sign_out);
        mLanguageLayout = findViewById(R.id.profile_settings_language_layout);
        mLanguageIconImageView = findViewById(R.id.iv_profile_settings_language_icon);
        mSelectLanguageLayout = findViewById(R.id.profile_settings_select_language_layout);
        mLanguageEnglishLayout = findViewById(R.id.profile_settings_language_en);
        mEnglishSelectedIconImageView = findViewById(R.id.iv_en_checked_icon);
        mLanguageFrenchLayout = findViewById(R.id.profile_settings_language_fr);
        mFrenchSelectedIconImageView = findViewById(R.id.iv_fr_checked_icon);

        isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(this, ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            mSignOutTextView.setVisibility(View.VISIBLE);
        } else {
            mSignOutTextView.setVisibility(View.GONE);
        }

        arrowBackClick();
        accountInfoLayoutClick();
        aboutHalalLayoutClick();
        signOutClick();

        mLanguage = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
        if (mLanguage != null) {
            if (mLanguage.equals("français")) {
                mLanguageIconImageView.setImageResource(R.drawable.ic_lang_fr_pink);
                mLanguageFrenchLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
                mLanguageEnglishLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mEnglishSelectedIconImageView.setVisibility(View.INVISIBLE);
            } else {
                mLanguageIconImageView.setImageResource(R.drawable.ic_lang_en_pink);
                mLanguageEnglishLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
                mLanguageFrenchLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mFrenchSelectedIconImageView.setVisibility(View.INVISIBLE);
            }
        }

        mLanguageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectLanguageLayoutVisible) {
                    isSelectLanguageLayoutVisible = false;
                    mSelectLanguageLayout.setVisibility(View.GONE);
                } else {
                    isSelectLanguageLayoutVisible = true;
                    mSelectLanguageLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mLanguageEnglishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguageIconImageView.setImageResource(R.drawable.ic_lang_en_pink);
                mEnglishSelectedIconImageView.setVisibility(View.VISIBLE);
                mLanguageEnglishLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
                mFrenchSelectedIconImageView.setVisibility(View.INVISIBLE);
                mLanguageFrenchLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

                String languageToLoad  = "en";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

                SharedPreferencesHelper.setSharedPreferenceString(ProfileSettingsActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE, "English");

                Intent intent = new Intent(ProfileSettingsActivity.this, ProfileSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mLanguageFrenchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguageIconImageView.setImageResource(R.drawable.ic_lang_fr_pink);
                mFrenchSelectedIconImageView.setVisibility(View.VISIBLE);
                mLanguageFrenchLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
                mEnglishSelectedIconImageView.setVisibility(View.INVISIBLE);
                mLanguageEnglishLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

                String languageToLoad  = "fr";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

                SharedPreferencesHelper.setSharedPreferenceString(ProfileSettingsActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE, "français");

                Intent intent = new Intent(ProfileSettingsActivity.this, ProfileSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileSettingsActivity.this, MainActivity.class);
        intent.setAction(ConstantsHelper.ACTION_PROFILE_FRAGMENT);
        startActivity(intent);
    }
}
