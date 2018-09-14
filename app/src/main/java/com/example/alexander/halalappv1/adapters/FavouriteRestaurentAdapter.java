package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.whinc.widget.ratingbar.RatingBar;

import java.util.ArrayList;

/**
 * Created by Hazem on 9/14/2018.
 */

public class FavouriteRestaurentAdapter extends RecyclerView.Adapter<FavouriteRestaurentAdapter.RestaurantViewHolder> {
	private Context mContext;
	private final HomeRestaurantAdapter.OnRestaurantClickListener mOnRestaurantClickListener;

	private ArrayList<Restaurant> mRestaurantList;
	private int mTablePosition;

	public void setRestaurantList(ArrayList<Restaurant> restaurantList) {
		this.mRestaurantList = restaurantList;
		notifyDataSetChanged();
	}
	public FavouriteRestaurentAdapter(Context context, HomeRestaurantAdapter.OnRestaurantClickListener onRestaurantClickListener ) {
		this.mContext = context;
		this.mOnRestaurantClickListener = onRestaurantClickListener;
	}


	@Override
	public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_fav_restaurent, parent, false);
		RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(RestaurantViewHolder holder, int position) {
		String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, "");
		holder.restaurantNameTextView.setText(String.valueOf(mRestaurantList.get(position).getName()));
		holder.restaurantCategoryTextView.setText(String.valueOf(mRestaurantList.get(position).getCuisineNameFr()));
//		holder.restaurantLocationTextView.setText(String.valueOf(mRestaurantList.get(position).getCityNameFr()));
//		if (!TextUtils.isEmpty(mRestaurantList.get(position).getRate().toString())) {
//			holder.restaurantRateRatingBar.setCount(Integer.parseInt(mRestaurantList.get(position).getRate().toString()));
//		}
		Picasso.with(mContext)
				.load(mRestaurantList.get(position).getImage())
				.into(holder.restaurantImageImageView);
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
//		TextView restaurantLocationTextView;
//		RatingBar restaurantRateRatingBar;

		public RestaurantViewHolder(View itemView) {
			super(itemView);

			restaurantImageImageView = itemView.findViewById(R.id.iv_home_restaurant_image);
			restaurantNameTextView = itemView.findViewById(R.id.tv_home_restaurant_name);
			restaurantCategoryTextView = itemView.findViewById(R.id.tv_home_restaurant_category);
//			restaurantLocationTextView = itemView.findViewById(R.id.tv_home_restaurant_location);
//			restaurantRateRatingBar = itemView.findViewById(R.id.rb_home_restaurant_rate);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			int position = getAdapterPosition();
			mOnRestaurantClickListener.onRestaurantClick(mTablePosition, position);
		}
	}
}
