package com.example.alexander.halalappv1.services;

import com.example.alexander.halalappv1.model.City;
import com.example.alexander.halalappv1.model.Cuisine;
import com.example.alexander.halalappv1.model.User;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.model.modifiedmodels.RestaurantsList1;
import com.example.alexander.halalappv1.reservation.ReservationObject;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitWebService {

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(interceptor);
    Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(ConstantsHelper.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

    // Sign In
    @POST(ConstantsHelper.SIGN_UP_END_POINT)
    @FormUrlEncoded
    Call<User> signUpNewUser(@Field("firstName") String firstName,
                             @Field("lastName") String lastName,
                             @Field("email") String email,
                             @Field("mobileNo") String mobileNo,
                             @Field("password") String password);

    // Sign Up
    @POST(ConstantsHelper.SIGN_IN_END_POINT)
    @FormUrlEncoded
    Call<User> signInExistingUser(@Field("email") String email,
                                  @Field("password") String password);


    // Facebook Sign In
    @POST(ConstantsHelper.FACEBOOK_SIGN_IN_END_POINT)
    @FormUrlEncoded
    Call<JsonObject> facebookSignIn(@Field("faceId") String facebookId,
                                    @Field("firstName") String firstName,
                                    @Field("lastName") String lastName,
                                    @Field("email") String email,
                                    @Field("mobileNo") String mobileNo);

    // Home Page
    @POST(ConstantsHelper.HOME_PAGE_FEED)
    @FormUrlEncoded
    Call<ArrayList<RestaurantsList1>> getTablesList(@Field("userId") int userId,
                                                    @Field("latitude") double latitude,
                                                    @Field("longitude") double longitude);

    // Search Page
    @POST(ConstantsHelper.SEARCH_PAGE_FEED)
    @FormUrlEncoded
    Call<ArrayList<Restaurant>> getRestaurants(@Field("userId") int userId,
                                               @Field("listId") int listId,
                                               @Field("latitude") double latitude,
                                               @Field("longitude") double longitude,
                                               @Field("keyword") String keyword,
                                               @Field("cuisineId") int cuisineId,
                                               @Field("price") int price,
                                               @Field("sortKey") String sortKey);

    // Mark As Favourite
    @POST(ConstantsHelper.MARK_AS_FAVOURITE_END_POINT)
    @FormUrlEncoded
    Call<Object> markAsFavourite(@Field("userId") int userId,
                                 @Field("restaurantId") int restaurantId);

    // Edit Account
    @POST(ConstantsHelper.EDIT_ACCOUNT_END_POINT)
    @FormUrlEncoded
    Call<User> editAccount(@Field("userId") int userId,
                           @Field("firstName") String firstName,
                           @Field("lastName") String lastName,
                           @Field("email") String email,
                           @Field("mobileNo") String mobileNo,
                           @Field("password") String password);

    // My Favourites
    @POST(ConstantsHelper.MY_FAVOURITES_FEED)
    @FormUrlEncoded
    Call<ArrayList<Restaurant>> getFavouriteRestaurants(@Field("userId") int userId);

    // List Restaurants
    @POST(ConstantsHelper.LIST_RESTAURANTS_FEED)
    @FormUrlEncoded
    Call<RestaurantsList1> getListRestaurants(@Field("userId") int userId,
                                             @Field("listId") int listId);

    // Get Cities
    @GET(ConstantsHelper.LOCATION_CITIES_FEED)
    Call<ArrayList<City>> getCities();

    // Get Cuisines
    @GET(ConstantsHelper.RESTAURANTS_CUISINES)
    Call<ArrayList<Cuisine>> getCuisines();

    // Get Capacity
    @POST(ConstantsHelper.RESTAURANT_CAPACITY)
    @FormUrlEncoded
    Call<JsonObject> getCapacity(@Field("restaurantId") int restaurantId,
                              @Field("reservationDate") String reservationDate,
                              @Field("reservationTime") String reservationTime);

    // Search By City
    @POST(ConstantsHelper.SEARCH_BY_CITY_END_POINT)
    @FormUrlEncoded
    Call<ArrayList<RestaurantsList1>> getRestaurantsListsByCity(@Field("userId") int userId,
                                                               @Field("cityId") int cityId);

    // Search By Keywords And City
    @POST(ConstantsHelper.SEARCH_BY_KEYWORDS_AND_CITY)
    @FormUrlEncoded
    Call<ArrayList<Restaurant>> getRestaurantsByKeywordAndCity(@Field("userId") int userId,
                                                               @Field("cityId") int cityId,
                                                               @Field("keyword") String keyword);

    // Make Reservation
    @POST(ConstantsHelper.MAKE_RESERVATION_END_POINT)
    @FormUrlEncoded
    Call<JsonObject> makeReservation(@Field("userId") int userId,
                                     @Field("restaurantId") int restaurantId,
                                     @Field("reservationDate") String reservationDate,
                                     @Field("reservationTime") String reservationTime,
                                     @Field("numberOfPeople") String numberOfPeople,
                                     @Field("firstName") String firstName,
                                     @Field("lastName") String lastName,
                                     @Field("email") String email,
                                     @Field("phone") String phone,
                                     @Field("notes") String notes,
                                     @Field("products") String productsList);

    // Edit Reservation
    @POST(ConstantsHelper.EDIT_RESERVATION)
    @FormUrlEncoded
    Call<JsonObject> editReservation(@Field("userId") int userId,
                                     @Field("restaurantId") int restaurantId,
                                     @Field("reservationDate") String reservationDate,
                                     @Field("reservationTime") String reservationTime,
                                     @Field("numberOfPeople") String numberOfPeople,
                                     @Field("firstName") String firstName,
                                     @Field("lastName") String lastName,
                                     @Field("email") String email,
                                     @Field("phone") String phone,
                                     @Field("notes") String notes,
                                     @Field("products") String productsList,
                                     @Field("reservationId") int reservationId);

    @POST(ConstantsHelper.MY_RESERVATIONS)
    @FormUrlEncoded
    Call<ReservationObject> getReservations(@Field("userId") int userId);

    // Cancel Reservation
    @POST(ConstantsHelper.CANCEL_RESERVATION)
    @FormUrlEncoded
    Call<JsonObject> cancelReservation(@Field("reservationId") int reservationId);

    @POST(ConstantsHelper.CONTACT_US)
    @FormUrlEncoded
    Call<JsonObject> contactUs(@Field("userId") int userId,
                               @Field("message") String message);

    // Get Polices
    @GET(ConstantsHelper.POLICIES)
    Call<JsonObject> getPolices();

    // Get Terms
    @GET(ConstantsHelper.TERMS)
    Call<JsonObject> getTerms();

    @POST(ConstantsHelper.GALLERY_IMAGES)
    @FormUrlEncoded
    Call<ArrayList<String>> getGalleryImages(@Field("restaurantId") int restaurantId);
}
