package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.whinc.widget.ratingbar.RatingBar;

import java.util.ArrayList;

public class HomeRestaurantAdapter extends RecyclerView.Adapter<HomeRestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private final OnRestaurantClickListener mOnRestaurantClickListener;

    private ArrayList<Restaurant> mRestaurantList;
    private int mTablePosition;

    private Boolean footerList = false;


    public HomeRestaurantAdapter(Context context, OnRestaurantClickListener onRestaurantClickListener ) {
        this.mContext = context;
        this.mOnRestaurantClickListener = onRestaurantClickListener;
    }
    public HomeRestaurantAdapter(Context context, OnRestaurantClickListener onRestaurantClickListener, Boolean footerList ) {
        this.mContext = context;
        this.mOnRestaurantClickListener = onRestaurantClickListener;
        this.footerList = footerList;
    }

    public interface OnRestaurantClickListener {
        void onRestaurantClick(int parentPosition, int childPosition);
    }

    public void setRestaurantList(ArrayList<Restaurant> restaurantList) {
        this.mRestaurantList = restaurantList;
        notifyDataSetChanged();
    }

    public void setTablePosition(int tablePosition) {
        this.mTablePosition = tablePosition;
    }

    //==============================================================================================
    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (footerList){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_restaurants_list_footer_item, viewGroup, false);
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_restaurant_list_item, viewGroup, false);
        }

        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, "");

        if (!TextUtils.isEmpty(language)) {
            if (language.equals("français")) {
                Picasso.with(mContext)
                        .load(mRestaurantList.get(position).getPicture())
                        .into(holder.restaurantImageImageView);
                if (footerList){
					holder.restaurantImageImageView.setCornerRadius((float) 20);
				}
                holder.restaurantNameTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
                holder.restaurantCategoryTextView.setText(String.valueOf(mRestaurantList.get(position).getCuisineName()));
//                holder.restaurantLocationTextView.setText(String.valueOf(mRestaurantList.get(position).getCityNameFr()));
//                if (!TextUtils.isEmpty(mRestaurantList.get(position).getRate().toString())) {
//                    holder.restaurantRateRatingBar.setCount(Integer.parseInt(mRestaurantList.get(position).getRate().toString()));
//                }
            } else {
                Picasso.with(mContext)
                        .load(mRestaurantList.get(position).getPicture())
                        .into(holder.restaurantImageImageView);
				if (footerList){
					holder.restaurantImageImageView.setCornerRadius((float) 20);
				}
                holder.restaurantNameTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
                holder.restaurantCategoryTextView.setText(String.valueOf(mRestaurantList.get(position).getCuisineName()));
//                holder.restaurantLocationTextView.setText(String.valueOf(mRestaurantList.get(position).getCityNameEn()));
//                if (!TextUtils.isEmpty(mRestaurantList.get(position).getRate().toString())) {
//                    holder.restaurantRateRatingBar.setCount(Integer.parseInt(mRestaurantList.get(position).getRate().toString()));
//                }
            }

            int discount = mRestaurantList.get(position).getPromotionAmount();
            if (discount > 0){
                holder.restaurantDiscount.setText(discount + " %");
            }else {
                holder.discountLayout.setVisibility(View.INVISIBLE);
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
    //==============================================================================================
    class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RoundedImageView restaurantImageImageView;
        TextView restaurantNameTextView;
        TextView restaurantCategoryTextView;
        TextView restaurantLocationTextView;
        RatingBar restaurantRateRatingBar;
        TextView restaurantDiscount;
        LinearLayout discountLayout;

        public RestaurantViewHolder(View itemView) {
            super(itemView);

            restaurantImageImageView = itemView.findViewById(R.id.iv_home_restaurant_image);
            restaurantNameTextView = itemView.findViewById(R.id.tv_home_restaurant_name);
            restaurantCategoryTextView = itemView.findViewById(R.id.tv_home_restaurant_category);
            restaurantLocationTextView = itemView.findViewById(R.id.tv_home_restaurant_location);
            restaurantRateRatingBar = itemView.findViewById(R.id.rb_home_restaurant_rate);
            restaurantDiscount = itemView.findViewById(R.id.discount);
            discountLayout = itemView.findViewById(R.id.discountLayout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnRestaurantClickListener.onRestaurantClick(mTablePosition, mRestaurantList.get(position).getId());
        }
    }
}
