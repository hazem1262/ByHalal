package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.User;
import com.example.alexander.halalappv1.utils.ConstantsHelper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Hazem on 9/15/2018.
 */

public interface Account {
	// Edit Account
	@POST(ConstantsHelper.EDIT_ACCOUNT_END_POINT)
	@FormUrlEncoded
	Call<User> editAccount(@Field("userId") int userId,
						   @Field("firstName") String firstName,
						   @Field("lastName") String lastName,
						   @Field("email") String email,
						   @Field("mobileNo") String mobileNo,
						   @Field("password") String password);

	// Sign Up
	@POST(ConstantsHelper.SIGN_IN_END_POINT)
	@FormUrlEncoded
	Call<User> signInExistingUser(@Field("email") String email,
								  @Field("password") String password);

	// Sign In
	@POST(ConstantsHelper.SIGN_UP_END_POINT)
	@FormUrlEncoded
	Call<User> signUpNewUser(@Field("firstName") String firstName,
							 @Field("lastName") String lastName,
							 @Field("email") String email,
							 @Field("mobileNo") String mobileNo,
							 @Field("password") String password);
}
