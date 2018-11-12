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
import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;
import com.whinc.widget.ratingbar.RatingBar;

import java.util.ArrayList;
import java.util.List;

public class SearchRestaurantAdapter extends RecyclerView.Adapter<SearchRestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private List<Restaurant> mRestaurantList;
    private final OnRestaurantListClickListener mOnRestaurantListClickListener;

    public SearchRestaurantAdapter(Context context, OnRestaurantListClickListener onRestaurantListClickListener) {
        this.mContext = context;
        this.mOnRestaurantListClickListener = onRestaurantListClickListener;
    }

    public interface OnRestaurantListClickListener {
        void onFavoriteButtonClick(int itemPosition);
        void onListItemClick(int itemPosition);
    }

    //==============================================================================================
    private ArrayList<Integer> mFavoriteItemsList = new ArrayList<>();

    public void addItemToFavoriteItemsList(int id) {
        this.mFavoriteItemsList.add(id);
    }

    public void removeItemFromFavoriteItemsList(int id) {
        if (mFavoriteItemsList.size() > 0) {
            if (mFavoriteItemsList.contains(id)) {
                this.mFavoriteItemsList.remove(mFavoriteItemsList.indexOf(id));
            }
        }
    }

    private ArrayList<Integer> mUnFavoriteItemsList = new ArrayList<>();

    public void addItemToUnFavoriteItemsList(int id) {
        mUnFavoriteItemsList.add(id);
    }

    public void removeItemFromUnFavoriteItemsList(int id) {
        if (mUnFavoriteItemsList.size() > 0) {
            if (mUnFavoriteItemsList.contains(id)) {
                mUnFavoriteItemsList.remove(mUnFavoriteItemsList.indexOf(id));
            }
        }
    }
    //==============================================================================================
    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_restaurant_list_item, viewGroup, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {


        holder.restaurantNameTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
        holder.restaurantCategoryTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
        Picasso.with(mContext)
                .load(mRestaurantList.get(position).getPicture())
                .error(R.drawable.category)
                .into(holder.restaurantImageImageView);

        holder.favoriteButtonImageView.setImageResource(R.drawable.ic_favourite_empty);
        if (mFavoriteItemsList.contains(position)) {
            holder.favoriteButtonImageView.setImageResource(R.drawable.ic_favourite_pink);
        }
        if (mUnFavoriteItemsList.contains(position)) {
            holder.favoriteButtonImageView.setImageResource(R.drawable.ic_favourite_empty);
        }
//        if (mRestaurantList.get(position).getFavourite().equals("true")) {
//            holder.favoriteButtonImageView.setImageResource(R.drawable.ic_favourite_pink);
//        }
//        if (mRestaurantList.get(position).getFavourite().equals("false")) {
//            holder.favoriteButtonImageView.setImageResource(R.drawable.ic_favourite_empty);
//        }
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
        ImageView favoriteButtonImageView;

        public RestaurantViewHolder(View itemView) {
            super(itemView);

            restaurantImageImageView = itemView.findViewById(R.id.iv_search_restaurant_image);
            restaurantNameTextView = itemView.findViewById(R.id.tv_search_restaurant_name);
            restaurantCategoryTextView = itemView.findViewById(R.id.tv_search_restaurant_catrgory);
            restaurantLocationTextView = itemView.findViewById(R.id.tv_search_restaurant_location);
            restaurantRateRatingBar = itemView.findViewById(R.id.rb_search_restaurant_rate);
            favoriteButtonImageView = itemView.findViewById(R.id.iv_search_restaurant_favourite);

            favoriteButtonImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getAdapterPosition();
                    mOnRestaurantListClickListener.onFavoriteButtonClick(itemPosition);
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            mOnRestaurantListClickListener.onListItemClick(itemPosition);
        }
    }
}
