package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.activities.RestaurantProfileActivity;
import com.example.alexander.halalappv1.activities.TableRestaurantsActivity;
import com.example.alexander.halalappv1.model.newModels.Category;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.alexander.halalappv1.fragments.HomeFragment.CATEGORY_NAME;
import static com.example.alexander.halalappv1.fragments.HomeFragment.RESTAURENT_KEY;
import static com.example.alexander.halalappv1.fragments.HomeFragment.TABLE_ID_KEY;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private ArrayList<Category> myCategoryList;
    private Context mContext;
    private int lastPosition = -1;

    // constructor for my adapter to set the arrayList data and the context to my private references
    public CategoriesAdapter(ArrayList<Category> arrayList, Context context){
        myCategoryList = arrayList;
        mContext = context;
    }

    // method to sort the stories referring to seen stories and new ones

    @Override
    public void onViewDetachedFromWindow(@NonNull CategoriesAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_category_item, viewGroup, false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindItems(myCategoryList.get(i));
        setAnimation(viewHolder.itemView, i);
    }


    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return myCategoryList.size();
    }

    // this is my custom subclass from ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // reference to the views in my layout
        private ImageView categoryPicture;
        private TextView categoryName;
        // reference to the view
        private View mView;

        // constructor for my view holder
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            categoryPicture = mView.findViewById(R.id.cell_category_image);
            categoryName = mView.findViewById(R.id.cell_category_name);
            // set onClickListener on the category image and the Category name
            categoryPicture.setOnClickListener(this);
            categoryName.setOnClickListener(this);
        }

        // this method to set data inside views
        private void bindItems(final Category item) {
            Picasso.with(mContext)
                    .load(item.getPicture())
                    .error(R.drawable.category)
                    .into(categoryPicture);
            categoryName.setText(item.getName());
        }

        public void clearAnimation()
        {
            this.categoryPicture.clearAnimation();
        }

        @Override
        public void onClick(View view) {
            // getting the category id to perform action
            int categoryId = myCategoryList.get(getLayoutPosition()).getId();
            // TODO :: here perform action with ID
            Intent intent = new Intent(mContext, TableRestaurantsActivity.class);
            intent.putExtra(CATEGORY_NAME, myCategoryList.get(getLayoutPosition()).getName());
            intent.putExtra(TABLE_ID_KEY, categoryId);
            mContext.startActivity(intent);
        }
    }


}