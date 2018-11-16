package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.RestaurantProfileActivity;
import com.example.alexander.halalappv1.activities.SearchRestaurantActivity;
import com.example.alexander.halalappv1.model.newModels.CategoriesWithRestaurant;
import com.example.alexander.halalappv1.model.newModels.Category;
import com.example.alexander.halalappv1.model.newModels.Restaurant;
import com.example.alexander.halalappv1.model.newModels.RestaurantOfTheWeek;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.alexander.halalappv1.fragments.HomeFragment.RESTAURENT_KEY;
import static com.example.alexander.halalappv1.utils.ConstantsHelper.ACTION_HOME_CATEGORIES;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements HomeRestaurantAdapter.OnRestaurantClickListener {

    private Context mContext;
    private ArrayList<Object> mRestaurantsLists;  // CategoriesWithRestaurant -> body
                                                  // RestaurantOfTheWeek -> header
                                                  //
    private final OnTablesClickListener mOnTablesClickListener;

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

    public void setRestaurantsLists(ArrayList<Object> restaurantsLists) {
        this.mRestaurantsLists = restaurantsLists;
        notifyDataSetChanged();
    }

    //==============================================================================================



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view;

        if (viewType == LIST_HEADER){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_restaurents_list_header, viewGroup, false);
            return new HeaderViewHolder(view);

        }else if (viewType == LIST_BODY){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_tables_list_item, viewGroup, false);
            RestaurantsListViewHolder viewHolder = new RestaurantsListViewHolder(view);
            return viewHolder;
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_recyclerview, viewGroup, false);
            return new CategoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        double startTime = System.currentTimeMillis();
        if (getItemViewType(position) == LIST_HEADER){
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.bindView((RestaurantOfTheWeek)mRestaurantsLists.get(position));
        }else if(getItemViewType(position) == LIST_BODY){
            RestaurantsListViewHolder bodyHolder = (RestaurantsListViewHolder) holder;
            bodyHolder.bindView((CategoriesWithRestaurant) mRestaurantsLists.get(position));
        }else {
            CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;
            categoryHolder.bindView((ArrayList<Category>)mRestaurantsLists.get(position));
        }
        double endTime = System.currentTimeMillis();
        double diff = endTime - startTime;
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
        Object cellType = mRestaurantsLists.get(position);
        if (cellType instanceof CategoriesWithRestaurant){
            return LIST_BODY;
        }else if (cellType instanceof RestaurantOfTheWeek){
            return LIST_HEADER;
        }else{
            return LIST_FOOTER;
        }
    }


    //==============================================================================================
    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_home_table_header;
        TextView see_all_category;
        RecyclerView categories_recycler_view;
        private CategoriesAdapter categoriesAdapter;
        private ArrayList<Category> categoriesList;
        public CategoryViewHolder(View v){
            super(v);
            tv_home_table_header = v.findViewById(R.id.tv_home_table_header);
            see_all_category = v.findViewById(R.id.see_all_category);
            see_all_category.setOnClickListener(this);
            categories_recycler_view = v.findViewById(R.id.categories_recycler_view);
            // init recycler
            categoriesList = new ArrayList<>();
            categoriesAdapter = new CategoriesAdapter(categoriesList, mContext);
            categories_recycler_view.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL, false));
            categories_recycler_view.setHasFixedSize(true);
            categories_recycler_view.setAdapter(categoriesAdapter);
        }

        public void bindView(ArrayList<Category> categories){
            categoriesList.clear();
            categoriesList.addAll(categories);
            categoriesAdapter.notifyDataSetChanged();
        }
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.see_all_category){
                Intent intent = new Intent(mContext, SearchRestaurantActivity.class);
                intent.setAction(ACTION_HOME_CATEGORIES);
                mContext.startActivity(intent);
            }
        }
    }
    //==============================================================================================
    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView headerDiscount;
        ImageView restaurentListHeaderImage;
        TextView headerResturantName;
        TextView headerRestaurentType;
        LinearLayout discount;
        public HeaderViewHolder(View v){
            super(v);
            headerDiscount = v.findViewById(R.id.headerDiscount);
            restaurentListHeaderImage = v.findViewById(R.id.restaurentListHeaderImage);
            headerResturantName = v.findViewById(R.id.headerResturantName);
            headerRestaurentType = v.findViewById(R.id.headerRestaurentType);
            discount = v.findViewById(R.id.discount);
            restaurentListHeaderImage.setOnClickListener(this);
        }
        public void bindView(RestaurantOfTheWeek headerRestaurent){
            Picasso.with(mContext)
                    .load(headerRestaurent.getPicture())
                    .into(restaurentListHeaderImage);
            headerResturantName.setText(headerRestaurent.getName());
            headerRestaurentType.setText(headerRestaurent.getCuisineName());
            if (headerRestaurent.getPromotionAmount() > 0){
                headerDiscount.setText(headerRestaurent.getPromotionAmount() + "%");
            }else{
                discount.setVisibility(View.GONE);
            }
            restaurentListHeaderImage.setTag(headerRestaurent.getId());
        }
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.restaurentListHeaderImage){
                Intent intent = new Intent(mContext, RestaurantProfileActivity.class);
                intent.putExtra(RESTAURENT_KEY, (int)v.getTag());
                intent.setAction(ConstantsHelper.ACTION_HOME_FRAGMENT);
                mContext.startActivity(intent);
            }
        }
    }
    //==============================================================================================
    class RestaurantsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tableHeaderTextView;
        TextView seeAllTextView;
        RecyclerView restaurantsRecyclerView;
        private HomeRestaurantAdapter mRestaurantAdapter;
        private ArrayList<Restaurant> bodyList = new ArrayList<>();


        public RestaurantsListViewHolder(View itemView) {
            super(itemView);

            tableHeaderTextView = itemView.findViewById(R.id.tv_home_table_header);
            seeAllTextView = itemView.findViewById(R.id.tv_home_see_all);
            restaurantsRecyclerView = itemView.findViewById(R.id.rv_home_restaurants);
            restaurantsRecyclerView.setHasFixedSize(true);
            restaurantsRecyclerView.setItemViewCacheSize(20);
            restaurantsRecyclerView.setDrawingCacheEnabled(true);
            restaurantsRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            seeAllTextView.setOnClickListener(this);
            if (mRestaurantAdapter == null){
                mRestaurantAdapter = new HomeRestaurantAdapter(mContext, RestaurantsListAdapter.this);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL, false);
            layoutManager.setInitialPrefetchItemCount(5);
            restaurantsRecyclerView.setLayoutManager(layoutManager);
            mRestaurantAdapter.setRestaurantList(bodyList);
            mRestaurantAdapter.setHasStableIds(true);
            restaurantsRecyclerView.setRecycledViewPool(viewPool);
            restaurantsRecyclerView.setAdapter(mRestaurantAdapter);

        }

        public void bindView(CategoriesWithRestaurant categoriesWithRestaurant){
            tableHeaderTextView.setText(String.valueOf(categoriesWithRestaurant.getCatName()));
            mRestaurantAdapter.setRestaurantList((ArrayList<Restaurant>) categoriesWithRestaurant.getRestaurants());
            bodyList.clear();
            bodyList.addAll((ArrayList<Restaurant>) categoriesWithRestaurant.getRestaurants());
            mRestaurantAdapter.setTablePosition(getAdapterPosition());
            mRestaurantAdapter.notifyDataSetChanged();
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CategoriesWithRestaurant categoriesWithRestaurant = (CategoriesWithRestaurant) mRestaurantsLists.get(position);
            mOnTablesClickListener.onTableClick(categoriesWithRestaurant.getCatId(), categoriesWithRestaurant.getCatName());
        }
    }

    private final int LIST_HEADER = 1;
    private final int LIST_BODY = 2;
    private final int LIST_FOOTER = 3;

}
