package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.Cuisine;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;

public class CuisineSpinnerAdapter extends ArrayAdapter {

    Context mContext;
    ArrayList<Cuisine> mCuisinesList;

    public CuisineSpinnerAdapter(Context context, int resource, ArrayList<Cuisine> cuisinesList) {
        super(context, resource);
        this.mContext = context;
        this.mCuisinesList = cuisinesList;
    }

    @Override
    public int getCount() {
        return mCuisinesList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spinner_list_item, parent, false);
        }

        TextView spinnerText = convertView.findViewById(R.id.tv_spinner_list_item_text);
        if (position==0){
            spinnerText.setText("Cuisines");

        } else {
            String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, "");
            if (!TextUtils.isEmpty(language)) {
                if (language.equals("français")) {
                    spinnerText.setText(String.valueOf(mCuisinesList.get(position).getCuisineNameFr()));
                } else {
                    spinnerText.setText(String.valueOf(mCuisinesList.get(position).getCuisineNameEn()));
                }
            } else {
                spinnerText.setText(String.valueOf(mCuisinesList.get(position).getCuisineNameFr()));
            }
        }

        return convertView;
    }
}
