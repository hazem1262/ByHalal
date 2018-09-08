package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.model.modifiedmodels.RestaurantsList1;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements HomeRestaurantAdapter.OnRestaurantClickListener {

    private Context mContext;
    private ArrayList<RestaurantsList1> mRestaurantsLists;
    private final OnTablesClickListener mOnTablesClickListener;

    private LinearLayoutManager mLayoutManager;
    private HomeRestaurantAdapter mRestaurantAdapter;

    public RestaurantsListAdapter(Context context, OnTablesClickListener onTablesClickListener) {
        this.mContext = context;
        this.mOnTablesClickListener = onTablesClickListener;
    }

    public interface OnTablesClickListener {
        void onTableClick(int position);
        void onRestaurantClick(int parentPosition, int childPosition);
    }

    @Override
    public void onRestaurantClick(int parentPosition, int childPosition) {
        mOnTablesClickListener.onRestaurantClick(parentPosition, childPosition);
    }

    public void setRestaurantsLists(ArrayList<RestaurantsList1> restaurantsLists) {
        this.mRestaurantsLists = restaurantsLists;
        notifyDataSetChanged();
    }

    //==============================================================================================



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_tables_list_item, viewGroup, false);
        RestaurantsListViewHolder viewHolder = new RestaurantsListViewHolder(view);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        if (viewType == LIST_BODY){
			mRestaurantAdapter = new HomeRestaurantAdapter(mContext, this);
		}else if (viewType == LIST_FOOTER){
			mRestaurantAdapter = new HomeRestaurantAdapter(mContext, this,true);
		}

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, "");
        if (getItemViewType(position) == LIST_BODY || getItemViewType(position) == LIST_FOOTER){
			RestaurantsListViewHolder bodyHolder = (RestaurantsListViewHolder) holder;
			if (!TextUtils.isEmpty(language)) {
				if (language.equals("français")) {
					bodyHolder.tableHeaderTextView.setText(String.valueOf(mRestaurantsLists.get(position).getListNameFr()));
				} else {
					bodyHolder.tableHeaderTextView.setText(String.valueOf(mRestaurantsLists.get(position).getListNameEn()));
				}
			} else {
				bodyHolder.tableHeaderTextView.setText(String.valueOf(mRestaurantsLists.get(position).getListNameFr()));
			}

			bodyHolder.restaurantsRecyclerView.setLayoutManager(mLayoutManager);
			mRestaurantAdapter.setRestaurantList((ArrayList<Restaurant>) mRestaurantsLists.get(position).getRestaurants());
			mRestaurantAdapter.setTablePosition(position);
			bodyHolder.restaurantsRecyclerView.setAdapter(mRestaurantAdapter);
		}
    }

    @Override
    public int getItemCount() {
        if (mRestaurantsLists != null) {
            return mRestaurantsLists.size();
        } else {
            return 0;
        }
    }

	@Override
	public int getItemViewType(int position) {
    	if (mRestaurantsLists.get(position).getListType() == ConstantsHelper.RESTAURANTS_LIST_HEADER){
    		return LIST_HEADER;
		}else if(position == mRestaurantsLists.size() - 1){
    		return LIST_FOOTER;
		}
		return LIST_BODY;
	}

	//==============================================================================================
    class RestaurantsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tableHeaderTextView;
        TextView seeAllTextView;
        RecyclerView restaurantsRecyclerView;

        public RestaurantsListViewHolder(View itemView) {
            super(itemView);

            tableHeaderTextView = itemView.findViewById(R.id.tv_home_table_header);
            seeAllTextView = itemView.findViewById(R.id.tv_home_see_all);
            restaurantsRecyclerView = itemView.findViewById(R.id.rv_home_restaurants);

            seeAllTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnTablesClickListener.onTableClick(position);
        }
    }

    class FooterListViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    	TextView tableHeaderTextView;
		TextView seeAllTextView;
		RecyclerView restaurantsRecyclerView;
    	public FooterListViewHolder(View itemView){
			super(itemView);

		}

		@Override
		public void onClick(View v) {

		}
	}
    class HeaderListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    	TextView headerRestaurentSectionName;
    	TextView headerRestaurentName;
    	TextView headerRestaurentType;
    	ImageView headerRestaurentImage;
    	public HeaderListViewHolder(View itemView){
    		super(itemView);
    		headerRestaurentSectionName = itemView.findViewById(R.id.sectionHeader);
			headerRestaurentName = itemView.findViewById(R.id.headerResturantName);
			headerRestaurentType = itemView.findViewById(R.id.headerRestaurentType);
			headerRestaurentImage = itemView.findViewById(R.id.restaurentListHeaderImage);
		}
		@Override
		public void onClick(View v) {
			int position = getAdapterPosition();
			mOnTablesClickListener.onTableClick(position);
		}
	}

    private final int LIST_HEADER = 1;
    private final int LIST_BODY = 2;
    private final int LIST_FOOTER = 3;
}
