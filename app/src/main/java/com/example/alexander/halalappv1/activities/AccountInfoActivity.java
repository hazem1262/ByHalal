package com.example.alexander.halalappv1.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.User;
import com.example.alexander.halalappv1.services.Account;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.alexander.halalappv1.utils.ConstantsHelper.SIGNUP;

public class AccountInfoActivity extends AppCompatActivity {

    private EditText mSignInEmailEditText;
    private EditText mSignInPasswordEditText;
    private Button mSignInButton;
    private EditText mSignUpFirstNameEditText;
    private EditText mSignUpFamilyNameEditText;
    private EditText mSignUpEmailEditText;
    private EditText mSignUpMobileNumberEditText;
    private EditText mSignUpPasswordEditText;
    private EditText mSignUpRetypedPasswordEditText;
    private Button mSignUpButton;
    private ConstraintLayout mEditProfileLayout;
    private ConstraintLayout mSignInSignUpLayout;
    private boolean isLoggedIn;

    private ConstraintLayout mSignInLayout;
    private Button mCreateNewAccountButton;
    private ConstraintLayout mSignUpLayout;
    private TextView mSignUpArrowBackImageView;
    private TextView retourSignIn;

    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mEmailTextView;
    private TextView mPhoneNumberTextView;
    private TextView mPasswordTextView;
    private TextView mConfirmPasswordTextView;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mEmailEditText;
    private EditText mPhoneNumberEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private ImageView mArrowBackImageView;
    private TextView mEditTextView;
    private ConstraintLayout mLoadingIndicator;

    private void findViewsById() {
        mFirstNameTextView = findViewById(R.id.tv_profile_acc_info_first_name);
        mLastNameTextView = findViewById(R.id.tv_profile_acc_info_last_name);
        mEmailTextView = findViewById(R.id.tv_profile_acc_info_email);
        mPhoneNumberTextView = findViewById(R.id.tv_profile_acc_info_phone_number);
        mPasswordTextView = findViewById(R.id.tv_profile_acc_info_password);
        mConfirmPasswordTextView = findViewById(R.id.tv_profile_acc_info_confirm_password);
        mFirstNameEditText = findViewById(R.id.et_profile_acc_info_first_name);
        mLastNameEditText = findViewById(R.id.et_profile_acc_info_last_name);
        mEmailEditText = findViewById(R.id.et_profile_acc_info_email);
        mPhoneNumberEditText = findViewById(R.id.et_profile_acc_info_phone_number);
        mPasswordEditText = findViewById(R.id.et_profile_acc_info_password);
        mConfirmPasswordEditText = findViewById(R.id.et_profile_acc_info_confirm_password);
        mArrowBackImageView = findViewById(R.id.profile_acc_info_arrow_back);
        mEditTextView = findViewById(R.id.tv_profile_acc_info_edit);
        mLoadingIndicator = findViewById(R.id.pb_profile_acc_info_loading_indicator);

        mSignInEmailEditText = findViewById(R.id.et_sign_in_activity_email);
        mSignInPasswordEditText = findViewById(R.id.et_sign_in_activity_password);
        mSignInButton = findViewById(R.id.btn_sign_in_activity_sign_in);
        mSignUpFirstNameEditText = findViewById(R.id.et_sign_up_activity_first_name);
        mSignUpFamilyNameEditText = findViewById(R.id.et_sign_up_activity_family_name);
        mSignUpEmailEditText = findViewById(R.id.et_sign_up_activity_email);
        mSignUpMobileNumberEditText = findViewById(R.id.et_sign_up_activity_mobile_number);
        mSignUpPasswordEditText = findViewById(R.id.et_sign_up_activity_password);
        mSignUpRetypedPasswordEditText = findViewById(R.id.et_sign_up_activity_retype_password);
        mSignUpButton = findViewById(R.id.btn_sign_up_activity_sign_up);
        mEditProfileLayout = findViewById(R.id.layout_account_info_activity_edit_profile);
        mSignInSignUpLayout = findViewById(R.id.layout_account_info_activity_sign_in_sign_up);

        mSignInLayout = findViewById(R.id.layout_sign_in);
        mCreateNewAccountButton = findViewById(R.id.btn_sign_in_activity_create_naw_account);
        mSignUpLayout = findViewById(R.id.layout_sign_up);
        mSignUpArrowBackImageView = findViewById(R.id.iv_sign_up_arrow_back);
        retourSignIn = findViewById(R.id.retourSignIn);
    }

    private void updateTextViewsWithUserData() {
        String firstName = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_FIRST_NAME, "");
        String familyName = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_FAMILY_NAME, "");
        String email = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_EMAIL, "");
        String phoneNumber = SharedPreferencesHelper.getSharedPreferenceString(this, ConstantsHelper.KEY_MOBILE_NUMBER, "");

        mFirstNameTextView.setText(firstName);
        mLastNameTextView.setText(familyName);
        mEmailTextView.setText(email);
        mPhoneNumberTextView.setText(phoneNumber);
    }

    private void hideTextViews() {
//        mFirstNameTextView.setVisibility(View.GONE);
//        mLastNameTextView.setVisibility(View.GONE);
        mEmailTextView.setVisibility(View.GONE);
        mPhoneNumberTextView.setVisibility(View.GONE);
        mPasswordTextView.setVisibility(View.GONE);
        mConfirmPasswordTextView.setVisibility(View.GONE);
    }

    private void showAndUpdateEditTexts() {
        mFirstNameEditText.setVisibility(View.VISIBLE);
        mLastNameEditText.setVisibility(View.VISIBLE);
        mEmailEditText.setVisibility(View.VISIBLE);
        mPhoneNumberEditText.setVisibility(View.VISIBLE);
        mPasswordEditText.setVisibility(View.VISIBLE);
        mConfirmPasswordEditText.setVisibility(View.VISIBLE);

        mFirstNameEditText.setText(mFirstNameTextView.getText());
        mLastNameEditText.setText(mLastNameTextView.getText());
        mEmailEditText.setText(mEmailTextView.getText());
        mPhoneNumberEditText.setText(mPhoneNumberTextView.getText());
        mPasswordEditText.setText("");
        mConfirmPasswordEditText.setText("");
    }

    private boolean isUserDataValid(String firstName, String familyName, String email, String mobileNumber, String password, String confirmPassword) {

//        if (TextUtils.isEmpty(firstName)) {
//            Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_first_name_is_required), Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if (TextUtils.isEmpty(familyName)) {
//            Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_family_name_is_required), Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (! email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(mobileNumber)) {
            Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_mobile_number_is_required), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (! Pattern.matches("[a-zA-Z]+", mobileNumber) && ! (mobileNumber.length() == 10)) {
            Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_invalid_mobile_number), Toast.LENGTH_LONG).show();
            return false;
        }
        else if (! TextUtils.isEmpty(password)) {
            if (password.length() < 6) {
                Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_password_is_at_least_6_chars), Toast.LENGTH_LONG).show();
                return false;
            }
            else if (! password.equals(confirmPassword)) {
                Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.sign_up_activity_toast_message_password_and_retyped_password_do_not_match), Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }

    private void editAccount(int userId, String firstName, String lastName, String email, String phoneNumber, String password) {
        Account webService = RetrofitWebService.retrofit.create(Account.class);
        Call<User> userCall = webService.editAccount(userId, firstName, lastName, email, phoneNumber, password);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        int userId = user.getUserId();
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        String email = user.getEmail();
                        String mobileNumber = user.getMobileNo();
                        String password = user.getPassword();
                        if (firstName != null && lastName != null && email != null && mobileNumber != null && password != null) {
                            hideEditTexts();
                            showAndUpdateTextViews();

                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FIRST_NAME, firstName);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FAMILY_NAME, lastName);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_EMAIL, email);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_MOBILE_NUMBER, mobileNumber);
                            SharedPreferencesHelper.setSharedPreferenceInt(AccountInfoActivity.this, ConstantsHelper.KEY_USER_ID, userId);
                        } else {
                            Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.toast_message_error_updating_your_data), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.toast_message_error_updating_your_data), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Toast.makeText(AccountInfoActivity.this, getResources().getString(R.string.toast_message_error_updating_your_data), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hideEditTexts() {
        mFirstNameEditText.setVisibility(View.GONE);
        mLastNameEditText.setVisibility(View.GONE);
        mEmailEditText.setVisibility(View.GONE);
        mPhoneNumberEditText.setVisibility(View.GONE);
        mPasswordEditText.setVisibility(View.GONE);
        mConfirmPasswordEditText.setVisibility(View.GONE);
    }

    private void showAndUpdateTextViews() {
        mFirstNameTextView.setVisibility(View.VISIBLE);
        mLastNameTextView.setVisibility(View.VISIBLE);
        mEmailTextView.setVisibility(View.VISIBLE);
        mPhoneNumberTextView.setVisibility(View.VISIBLE);
        mPasswordTextView.setVisibility(View.VISIBLE);
        mConfirmPasswordTextView.setVisibility(View.VISIBLE);

        mFirstNameTextView.setText(mFirstNameEditText.getText().toString());
        mLastNameTextView.setText(mLastNameEditText.getText().toString());
        mEmailTextView.setText(mEmailEditText.getText().toString());
        mPhoneNumberTextView.setText(mPhoneNumberEditText.getText().toString());
        mPasswordTextView.setText("*********");
        mConfirmPasswordTextView.setText("*********");
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }
    //==============================================================================================
    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (mSignUpLayout.getVisibility() == View.VISIBLE)
        if (false)
        {
            mSignUpLayout.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mSignInLayout.animate()
                                    .translationY(0)
                                    .alpha(1.0f)
                                    .setDuration(300)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mSignUpLayout.setVisibility(View.GONE);
                                            mSignInLayout.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    });
        } else {
            finish();
        }
    }

    private void editTextViewClick() {
        mEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextView.getText().equals(getResources().getString(R.string.tv_profile_acc_info_edit_text))) {
                    hideTextViews();
                    showAndUpdateEditTexts();
                    mEditTextView.setText(getResources().getString(R.string.tv_profile_acc_info_save_text));
                }
                else if (mEditTextView.getText().equals(getResources().getString(R.string.tv_profile_acc_info_save_text))) {

                    String firstName = mFirstNameEditText.getText().toString();
                    String lastName = mLastNameEditText.getText().toString();
                    String email = mEmailEditText.getText().toString();
                    String phoneNumber = mPhoneNumberEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();
                    String confirmPassword = mConfirmPasswordEditText.getText().toString();

                    boolean isValid = isUserDataValid(firstName, lastName, email, phoneNumber, password, confirmPassword);
                    boolean isNetworkOk = NetworkHelper.hasNetworkAccess(AccountInfoActivity.this);

                    if (isValid) {
                        if (isNetworkOk) {
                            showLoadingIndicator();
                            int userId = SharedPreferencesHelper.getSharedPreferenceInt(AccountInfoActivity.this, ConstantsHelper.KEY_USER_ID, -10);
                            if (userId != -10) {
                                editAccount(userId, firstName, lastName, email, phoneNumber, password);
                                mEditTextView.setText(getResources().getString(R.string.tv_profile_acc_info_edit_text));
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViewsById(); // (1)

        isLoggedIn = SharedPreferencesHelper.getSharedPreferenceBoolean(AccountInfoActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            mEditProfileLayout.setVisibility(View.VISIBLE);
            mSignInSignUpLayout.setVisibility(View.GONE);
            updateTextViewsWithUserData(); // (2)
        } else {
			mEditProfileLayout.setVisibility(View.GONE);
			mSignInSignUpLayout.setVisibility(View.VISIBLE);
			if (getIntent().getBooleanExtra(SIGNUP,false)){
				showSignUpLayout();
			}else{

			}


        }

        arrowBackClick();

        editTextViewClick();

        signInButtonClick();

        signUpButtonClick();

        createNewAccountButtonClick();

        signUpArrowBackImageViewClick();
        if (getIntent().getBooleanExtra(SIGNUP,false)){
            showSignUpLayout();
        }
        retourSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    //==============================================================================================
    private void signInButtonClick() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(AccountInfoActivity.this);
                if (isNetworkOk) {
                    String email = mSignInEmailEditText.getText().toString();
                    String password = mSignInPasswordEditText.getText().toString();
                    boolean isDataValid = isSignInDataValid(email, password);
                    if (isDataValid) {
                        showLoadingIndicator();
                        signInExistingUser(email, password);
                    }
                } else {
                    Context context = AccountInfoActivity.this;
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_no_internet_connection);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }
        });
    }

    private void signUpButtonClick() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetworkOk = NetworkHelper.hasNetworkAccess(AccountInfoActivity.this);
                if (isNetworkOk) {
                    String firstName = mSignUpFirstNameEditText.getText().toString();
                    String familyName = mSignUpFamilyNameEditText.getText().toString();
                    String email = mSignUpEmailEditText.getText().toString();
                    String mobileNumber = mSignUpMobileNumberEditText.getText().toString();
                    String password = mSignUpPasswordEditText.getText().toString();
                    String retypedPassword = mSignUpRetypedPasswordEditText.getText().toString();
                    boolean isValid = isSignUpDataValid(firstName, familyName, email, mobileNumber, password, retypedPassword);

                    if (isValid) {
                        showLoadingIndicator();
                        signUpNewUser(firstName, familyName, email, mobileNumber, password);
                    }
                } else {
                    Context context = AccountInfoActivity.this;
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_no_internet_connection);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }
        });
    }

    private void createNewAccountButtonClick() {
        mCreateNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpLayout();
//                mSignInLayout.setVisibility(View.GONE);
//                mSignUpLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showSignUpLayout(){

		mSignInLayout.setVisibility(View.GONE);
		mSignUpLayout.setVisibility(View.VISIBLE);
        /*mSignInLayout.animate()
                .translationY(0)
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mSignUpLayout.animate()
                                .translationY(0)
                                .alpha(1.0f)
                                .setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        mSignInLayout.setVisibility(View.GONE);
                                        mSignUpLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                });*/
    }
    private void signUpArrowBackImageViewClick() {
        mSignUpArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                /*mSignUpLayout.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mSignInLayout.animate()
                                        .translationY(0)
                                        .alpha(1.0f)
                                        .setDuration(300)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                mSignUpLayout.setVisibility(View.GONE);
                                                mSignInLayout.setVisibility(View.VISIBLE);
                                            }
                                        });
                            }
                        });
*/
//                mSignUpLayout.setVisibility(View.GONE);
//                mSignInLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean isSignInDataValid(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.sign_in_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(this, getResources().getString(R.string.sign_in_activity_toast_message_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.sign_in_activity_toast_message_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void signInExistingUser(String email, String password) {
        Account webService = RetrofitWebService.retrofit.create(Account.class);
        Call<User> call = webService.signInExistingUser(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        int userId = user.getUserId();
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        String email = user.getEmail();
                        String mobileNumber = user.getMobileNo();
                        String password = user.getPassword();

                        if (firstName != null && lastName != null && email != null && mobileNumber != null && password != null) {
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FIRST_NAME, firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FAMILY_NAME, lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_EMAIL, email);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_MOBILE_NUMBER, mobileNumber);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_PASSWORD, password);
                            SharedPreferencesHelper.setSharedPreferenceInt(AccountInfoActivity.this, ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(AccountInfoActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            reloadAccountInfoActivity();
                        } else {
                            Context context = AccountInfoActivity.this;
                            String message = getResources().getString(R.string.sign_in_activity_toast_message_wrong_email_or_wrong_password);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = AccountInfoActivity.this;
                    String message = getResources().getString(R.string.sign_in_activity_toast_message_problem_sign_in_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = AccountInfoActivity.this;
                String message = getResources().getString(R.string.sign_in_activity_toast_message_problem_sign_in_try_again_later);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    private boolean isSignUpDataValid(String firstName, String familyName, String email, String mobileNumber, String password, String retypedPassword) {
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_first_name_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(familyName)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_family_name_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_email_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(mobileNumber)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_mobile_number_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! Pattern.matches("[a-zA-Z]+", mobileNumber) && ! (mobileNumber.length() == 10)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_invalid_mobile_number), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_password_is_at_least_6_chars), Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(retypedPassword)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_retyped_password_is_required), Toast.LENGTH_LONG).show();
            return false;
        } else if (! password.equals(retypedPassword)) {
            Toast.makeText(this, getResources().getString(R.string.sign_up_activity_toast_message_password_and_retyped_password_do_not_match), Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void signUpNewUser(String firstName, String familyName, String email, String mobileNumber, String password) {
        Account webService = RetrofitWebService.retrofit.create(Account.class);
        Call<User> call = webService.signUpNewUser(firstName, familyName, email, mobileNumber, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        int userId = user.getUserId();
                        String firstName = user.getFirstName();
                        String lastName = user.getLastName();
                        String email = user.getEmail();
                        String mobileNumber = user.getMobileNo();
                        String password = user.getPassword();

                        if (firstName != null && lastName != null && email != null && mobileNumber != null && password != null) {
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FIRST_NAME, firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_FAMILY_NAME, lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_EMAIL, email);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_MOBILE_NUMBER, mobileNumber);
                            saveUserDataInSharedPreferences(ConstantsHelper.KEY_PASSWORD, password);
                            SharedPreferencesHelper.setSharedPreferenceInt(AccountInfoActivity.this, ConstantsHelper.KEY_USER_ID, userId);
                            SharedPreferencesHelper.setSharedPreferenceBoolean(AccountInfoActivity.this, ConstantsHelper.KEY_IS_LOGGED_IN, true);

                            hideLoadingIndicator();
                            reloadAccountInfoActivity();
                        } else {
                            Context context = AccountInfoActivity.this;
                            String message = getResources().getString(R.string.sign_up_activity_toast_message_account_already_exists);
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, message, duration).show();
                        }
                    }
                } else {
                    Context context = AccountInfoActivity.this;
                    String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, message, duration).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideLoadingIndicator();
                Context context = AccountInfoActivity.this;
                String message = getResources().getString(R.string.sign_up_activity_toast_message_problem_sign_up_try_again_later);
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, message, duration).show();
            }
        });
    }

    private void saveUserDataInSharedPreferences(String key, String value) {
        SharedPreferencesHelper.setSharedPreferenceString(AccountInfoActivity.this, key, value);
    }

    private void reloadAccountInfoActivity() {
        Intent intent = new Intent(AccountInfoActivity.this, AccountInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
