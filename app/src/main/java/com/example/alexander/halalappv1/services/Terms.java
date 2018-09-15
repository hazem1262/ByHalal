package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hazem on 9/15/2018.
 */

public interface Terms {
	// Get Terms
	@GET(ConstantsHelper.TERMS)
	Call<JsonObject> getTerms();
}
