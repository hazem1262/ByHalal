package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.City;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;

public class SpinnerCitiesAdapter extends ArrayAdapter {

    Context mContext;
    ArrayList<City> mCitiesList;

    public SpinnerCitiesAdapter(Context context, int resource, ArrayList<City> citiesList) {
        super(context, resource);
        this.mContext = context;
        this.mCitiesList = citiesList;
    }

    @Override
    public int getCount() {
        return mCitiesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_cities_list_item, parent, false);
        }

        TextView spinnerText = convertView.findViewById(R.id.tv_spinner_cities_list_item_text);
        String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, "");
        if (!TextUtils.isEmpty(language)) {
            if (language.equals("français")) {
                spinnerText.setText(String.valueOf(mCitiesList.get(position).getCityNameFr()));
            } else {
                spinnerText.setText(String.valueOf(mCitiesList.get(position).getCityNameFr()));
            }
        } else {
            spinnerText.setText(String.valueOf(mCitiesList.get(position).getCityNameFr()));
        }

        return convertView;
    }
}
