package com.example.alexander.halalappv1.activities;

import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends AppCompatActivity {

    private ImageView mArrowBackImageView;
    private EditText mUserIssueEditText;
    private Button mSendButton;
    private ConstraintLayout mLoadingIndicator;
    private ConstraintLayout mSuccessfulLayout;
    private TextView mSuccessfulOkTextView;
    private boolean isNetworkOk;

    private void findViewsById() {
        mArrowBackImageView = findViewById(R.id.help_activity_arrow_back);
        mUserIssueEditText = findViewById(R.id.et_help_activity_user_issue);
        mSendButton = findViewById(R.id.btn_help_activity_send);
        mLoadingIndicator = findViewById(R.id.pb_help_activity_loading_indicator);
        mSuccessfulLayout = findViewById(R.id.message_submitted_successfully_layout);
        mSuccessfulOkTextView = findViewById(R.id.tv_message_submitted_successfully_ok);
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mSendButton.setEnabled(true);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
        mSendButton.setEnabled(false);
    }

    private void arrowBackClick() {
        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void successfulOkTextViewClick() {
        mSuccessfulOkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSuccessfulLayout.setVisibility(View.GONE);
                finish();
            }
        });
    }

    private void sendButtonClick() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = SharedPreferencesHelper.getSharedPreferenceInt(HelpActivity.this, ConstantsHelper.KEY_USER_ID, -10);
                String message = mUserIssueEditText.getText().toString();
                if (userId != -10 && ! TextUtils.isEmpty(message)) {
                    isNetworkOk = NetworkHelper.hasNetworkAccess(HelpActivity.this);
                    if (isNetworkOk) {
                        showLoadingIndicator();
                        contactUs(userId, message);
                    } else {
                        Toast.makeText(HelpActivity.this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void contactUs(int userId, String message) {
        RetrofitWebService webService = RetrofitWebService.retrofit.create(RetrofitWebService.class);
        Call<JsonObject> call = webService.contactUs(userId, message);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        String data = jsonObject.get("data").getAsString();
                        if (!TextUtils.isEmpty(data)) {
                            if (data.equals("success")) {
//                                Toast.makeText(HelpActivity.this, getResources().getString(R.string.toast_message_message_submitted_successfully), Toast.LENGTH_LONG).show();
                                mSuccessfulLayout.setVisibility(View.VISIBLE);
                                mUserIssueEditText.setText("");
                            }
                        }
                    }
                }

                else {
                    Toast.makeText(HelpActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_message), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoadingIndicator();
                Toast.makeText(HelpActivity.this, getResources().getString(R.string.toast_message_error_submitting_your_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        findViewsById();

        arrowBackClick();

        successfulOkTextViewClick();

        sendButtonClick();
    }
}
