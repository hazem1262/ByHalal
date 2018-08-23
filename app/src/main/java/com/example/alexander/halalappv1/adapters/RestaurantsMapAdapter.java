package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;
import com.whinc.widget.ratingbar.RatingBar;

import java.util.List;

public class RestaurantsMapAdapter extends RecyclerView.Adapter<RestaurantsMapAdapter.RestaurantViewHolder> {

    private Context mContext;
    private final OnRestaurantMapListClickListener mOnRestaurantMapListClickListener;
    private List<Restaurant> mRestaurantList;

    public RestaurantsMapAdapter(Context context, OnRestaurantMapListClickListener onRestaurantMapListClickListener ) {
        this.mContext = context;
        this.mOnRestaurantMapListClickListener = onRestaurantMapListClickListener;
    }

    public interface OnRestaurantMapListClickListener {
        void onMapListItemClick(int position);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_map_list_item, viewGroup, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, "");
        if (!TextUtils.isEmpty(language)) {
            if (language.equals("français")) {
                Picasso.with(mContext).load(mRestaurantList.get(position).getImage()).into(holder.restaurantImageImageView);
                holder.restaurantNameTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
                holder.restaurantCategoryTextView.setText(String.valueOf(mRestaurantList.get(position).getCuisineNameFr()));
                holder.restaurantLocationTextView.setText(String.valueOf(mRestaurantList.get(position).getCityNameFr()));
                if (!TextUtils.isEmpty(mRestaurantList.get(position).getRate().toString())) {
                    holder.restaurantRateRatingBar.setCount(Integer.parseInt(mRestaurantList.get(position).getRate().toString()));
                }
            } else {
                Picasso.with(mContext).load(mRestaurantList.get(position).getImage()).into(holder.restaurantImageImageView);
                holder.restaurantNameTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
                holder.restaurantCategoryTextView.setText(String.valueOf(mRestaurantList.get(position).getCuisineNameEn()));
                holder.restaurantLocationTextView.setText(String.valueOf(mRestaurantList.get(position).getCityNameEn()));
                if (!TextUtils.isEmpty(mRestaurantList.get(position).getRate().toString())) {
                    holder.restaurantRateRatingBar.setCount(Integer.parseInt(mRestaurantList.get(position).getRate().toString()));
                }
            }
        } else {
            Picasso.with(mContext).load(mRestaurantList.get(position).getImage()).into(holder.restaurantImageImageView);
            holder.restaurantNameTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
            holder.restaurantCategoryTextView.setText(String.valueOf(mRestaurantList.get(position).getCuisineNameFr()));
            holder.restaurantLocationTextView.setText(String.valueOf(mRestaurantList.get(position).getCityNameFr()));
            if (!TextUtils.isEmpty(mRestaurantList.get(position).getRate().toString())) {
                holder.restaurantRateRatingBar.setCount(Integer.parseInt(mRestaurantList.get(position).getRate().toString()));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mRestaurantList != null) {
            return mRestaurantList.size();
        } else {
            return 0;
        }
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.mRestaurantList = restaurantList;
        notifyDataSetChanged();
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView restaurantImageImageView;
        TextView restaurantNameTextView;
        TextView restaurantCategoryTextView;
        TextView restaurantLocationTextView;
        RatingBar restaurantRateRatingBar;

        public RestaurantViewHolder(View itemView) {
            super(itemView);

            restaurantImageImageView = itemView.findViewById(R.id.iv_map_restaurant_image);
            restaurantNameTextView = itemView.findViewById(R.id.tv_map_restaurant_name);
            restaurantCategoryTextView = itemView.findViewById(R.id.tv_map_restaurant_category);
            restaurantLocationTextView = itemView.findViewById(R.id.tv_map_restaurant_location);
            restaurantRateRatingBar = itemView.findViewById(R.id.rb_map_restaurant_rate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnRestaurantMapListClickListener.onMapListItemClick(position);
        }
    }
}
