package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.newModels.CategoriesWithRestaurant;
import com.example.alexander.halalappv1.model.newModels.Restaurant;

import java.util.ArrayList;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements HomeRestaurantAdapter.OnRestaurantClickListener {

    private Context mContext;
    private ArrayList<CategoriesWithRestaurant> mRestaurantsLists;
    private final OnTablesClickListener mOnTablesClickListener;

    private HomeRestaurantAdapter mRestaurantAdapter;
	private RecyclerView.RecycledViewPool viewPool;

    public RestaurantsListAdapter(Context context, OnTablesClickListener onTablesClickListener) {
        this.mContext = context;
        this.mOnTablesClickListener = onTablesClickListener;
		viewPool = new RecyclerView.RecycledViewPool();
    }

    public interface OnTablesClickListener {
        void onTableClick(int position, String name);
        void onRestaurantClick(int parentPosition, int childPosition);
    }

    @Override
    public void onRestaurantClick(int parentPosition, int childPosition) {
        mOnTablesClickListener.onRestaurantClick(parentPosition, childPosition);
    }

    public void setRestaurantsLists(ArrayList<CategoriesWithRestaurant> restaurantsLists) {
        this.mRestaurantsLists = restaurantsLists;
        notifyDataSetChanged();
    }

    //==============================================================================================



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_tables_list_item, viewGroup, false);
        RestaurantsListViewHolder viewHolder = new RestaurantsListViewHolder(view);
		mRestaurantAdapter = new HomeRestaurantAdapter(mContext, this);
		viewHolder.restaurantsRecyclerView.setRecycledViewPool(viewPool);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TimingLogger logger = new TimingLogger("scroll", "onbind");
        logger.addSplit("before onbindview");
		RestaurantsListViewHolder bodyHolder = (RestaurantsListViewHolder) holder;
		bodyHolder.tableHeaderTextView.setText(String.valueOf(mRestaurantsLists.get(position).getCatName()));
		mRestaurantAdapter.setRestaurantList((ArrayList<Restaurant>) mRestaurantsLists.get(position).getRestaurants());
		mRestaurantAdapter.setTablePosition(position);
		bodyHolder.restaurantsRecyclerView.setAdapter(mRestaurantAdapter);
        logger.addSplit("after onbindview");
        logger.dumpToLog();
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
            restaurantsRecyclerView.setHasFixedSize(true);
            restaurantsRecyclerView.setItemViewCacheSize(5);
            restaurantsRecyclerView.setDrawingCacheEnabled(true);
            restaurantsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            seeAllTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnTablesClickListener.onTableClick(mRestaurantsLists.get(position).getCatId(), mRestaurantsLists.get(position).getCatName());
        }
    }


}
