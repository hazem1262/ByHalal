package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.modifiedmodels.Restaurant;
import com.example.alexander.halalappv1.model.modifiedmodels.RestaurantsList1;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.util.ArrayList;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsListViewHolder>
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
    public RestaurantsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_tables_list_item, viewGroup, false);
        RestaurantsListViewHolder viewHolder = new RestaurantsListViewHolder(view);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mRestaurantAdapter = new HomeRestaurantAdapter(mContext, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantsListViewHolder holder, int position) {
        String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, "");
        if (!TextUtils.isEmpty(language)) {
            if (language.equals("français")) {
                holder.tableHeaderTextView.setText(String.valueOf(mRestaurantsLists.get(position).getListNameFr()));
            } else {
                holder.tableHeaderTextView.setText(String.valueOf(mRestaurantsLists.get(position).getListNameEn()));
            }
        } else {
            holder.tableHeaderTextView.setText(String.valueOf(mRestaurantsLists.get(position).getListNameFr()));
        }

        holder.restaurantsRecyclerView.setLayoutManager(mLayoutManager);
        mRestaurantAdapter.setRestaurantList((ArrayList<Restaurant>) mRestaurantsLists.get(position).getRestaurants());
        mRestaurantAdapter.setTablePosition(position);
        holder.restaurantsRecyclerView.setAdapter(mRestaurantAdapter);
    }

    @Override
    public int getItemCount() {
        if (mRestaurantsLists != null) {
            return mRestaurantsLists.size();
        } else {
            return 0;
        }
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
}
