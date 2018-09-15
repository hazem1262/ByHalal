package com.example.alexander.halalappv1.activities;

import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.services.Polices;
import com.example.alexander.halalappv1.services.RetrofitWebService;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.NetworkHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoliciesActivity extends AppCompatActivity {

    private ImageView mArrowBackImageView;
    private TextView mPoliciesTextView;
    private ConstraintLayout mLoadingIndicator;
    private boolean isNetworkOk;

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policies);

        mArrowBackImageView = findViewById(R.id.policies_activity_arrow_back);
        mPoliciesTextView = findViewById(R.id.tv_policies_activity_policies);
        mLoadingIndicator = findViewById(R.id.pb_policies_activity_loading_indicator);

        mArrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        isNetworkOk = NetworkHelper.hasNetworkAccess(this);
        if (isNetworkOk) {
            showLoadingIndicator();
            getPolices();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.toast_message_no_internet_connection), Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    private void getPolices() {
        Polices webService = RetrofitWebService.retrofit.create(Polices.class);
        Call<JsonObject> call = webService.getPolices();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hideLoadingIndicator();
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        String language = SharedPreferencesHelper.getSharedPreferenceString(PoliciesActivity.this, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
                        if (language != null) {
                            if (language.equals("français")) {
                                String html = jsonObject.get("policiesFr").getAsString();
                                if (!TextUtils.isEmpty(html)) {
                                    mPoliciesTextView.setText(Html.fromHtml(html));
                                }
                            } else {
                                String html = jsonObject.get("policiesEn").getAsString();
                                if (!TextUtils.isEmpty(html)) {
                                    mPoliciesTextView.setText(Html.fromHtml(html));
                                }
                            }
                        } else {
                            String html = jsonObject.get("policiesEn").getAsString();
                            if (!TextUtils.isEmpty(html)) {
                                mPoliciesTextView.setText(Html.fromHtml(html));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoadingIndicator();
            }
        });
    }
}
