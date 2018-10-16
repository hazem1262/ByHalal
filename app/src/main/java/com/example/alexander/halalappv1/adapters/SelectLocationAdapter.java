package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.City;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;

public class SelectLocationAdapter extends RecyclerView.Adapter<SelectLocationAdapter.LocationViewHolder> {

    private Context mContext;
    private final OnItemClickListener mOnItemClickListener;
    private ArrayList<City> mCitiesList;

    private String mCityName;
    private String mCityLatitude;
    private String mCityLongitude;

    public SelectLocationAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_location_list_item, viewGroup, false);
        LocationViewHolder viewHolder = new LocationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        mCityName = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_CITY, "");
        mCityLatitude = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_CITY_LATITUDE, "");
        mCityLongitude = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_CITY_LONGITUDE, "");
        if (!TextUtils.isEmpty(mCityName) && !TextUtils.isEmpty(mCityLatitude) && !TextUtils.isEmpty(mCityLongitude)) {
            if (mCitiesList.get(position).getCityNameFr().equals(mCityName)
                    && mCitiesList.get(position).getCityLatitude().equals(mCityLatitude)
                    && mCitiesList.get(position).getCityLongitude().equals(mCityLongitude)) {
            	// ToDo update with the new icons
                holder.locationTextView.setTextColor(Color.parseColor("#EA4D4D"));
                holder.locationIconImageView.setImageResource(R.drawable.ic_location_pink);
            }
        }

        holder.locationTextView.setText(String.valueOf(mCitiesList.get(position).getCityNameFr()));
    }

    @Override
    public int getItemCount() {
        if (mCitiesList != null) {
            return mCitiesList.size();
        } else {
            return 0;
        }
    }

    public void setCitiesList(ArrayList<City> citiesList) {
        this.mCitiesList = citiesList;
        notifyDataSetChanged();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView locationTextView;
        ImageView locationIconImageView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.tv_select_location);
            locationIconImageView = itemView.findViewById(R.id.iv_select_location_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnItemClickListener.onItemClick(position);
        }
    }
}
