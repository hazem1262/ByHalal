package com.example.alexander.halalappv1.utils;

public class ConstantsHelper {

    // shared preferences keys
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_FACEBOOK_ID = "user_facebook_id";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_FAMILY_NAME = "family_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_SELECTED_LANGUAGE = "selected_language";
    public static final String KEY_SELECTED_CITY = "selected_city";
    public static final String KEY_CITY_LATITUDE = "city_latitude";
    public static final String KEY_CITY_LONGITUDE = "city_longitude";

    public static final String ACTION_HOME_FRAGMENT = "home_fragment";
    public static final String ACTION_SEARCH_FRAGMENT = "search_fragment";
    public static final String ACTION_RESERVE_FRAGMENT = "reserve_fragment";
    public static final String ACTION_PROFILE_FRAGMENT = "profile_fragment";
    public static final String ACTION_TABLE_RESTAURANT_ACTIVITY = "activity_table_restaurants";
    public static final String ACTION_FAVOURITE_RESTAURANTS_ACTIVITY = "activity_favourite_restaurants";
    public static final String ACTION_SEARCH_RESTAURANT_ACTIVITY = "activity_search_restaurants";
    public static final String RESTAURANT_OBJECT_KEY = "RestaurantObject";

    // base url
    public static final String BASE_URL = "http://www.by-halal.fr/api/";
    // sign in
    public static final String SIGN_IN_END_POINT = "login";
    // sign up
    public static final String SIGN_UP_END_POINT = "signUp";
    // facebook sign in, sign up
    public static final String FACEBOOK_SIGN_IN_END_POINT = "facebookLogin";
    // fragment home
    public static final String HOME_PAGE_FEED = "mainPage";
    // fragment search
    public static final String SEARCH_PAGE_FEED = "searchSort";
    // mark as favourite
    public static final String MARK_AS_FAVOURITE_END_POINT = "addFavourite";
    // edit account
    public static final String EDIT_ACCOUNT_END_POINT = "editAccount";
    // my favourites
    public static final String MY_FAVOURITES_FEED = "myFavourites";
    // list restaurants
    public static final String LIST_RESTAURANTS_FEED = "restaurants";
    // cities in select location
    public static final String LOCATION_CITIES_FEED = "getCities";
    // cuisines in filter activity
    public static final String RESTAURANTS_CUISINES = "getCuisines";
    // search by city
    public static final String SEARCH_BY_CITY_END_POINT = "searchLocation";
    // search by keywords and citY
    public static final String SEARCH_BY_KEYWORDS_AND_CITY = "searchLocationKeyword";
    // contact us
    public static final String CONTACT_US = "contact";
    // polices
    public static final String POLICIES = "getPolices";
    // terms
    public static final String TERMS = "getTerms";
    // capacity
    public static final String RESTAURANT_CAPACITY = "getCapacity";

    public static final String MAKE_RESERVATION_END_POINT = "addReservation";

    public static final String MY_RESERVATIONS = "myReservations";

    public static final String EDIT_RESERVATION = "editReservation";

    public static final String CANCEL_RESERVATION = "deleteReservation";

    public static final String GALLERY_IMAGES = "gallery";
}
