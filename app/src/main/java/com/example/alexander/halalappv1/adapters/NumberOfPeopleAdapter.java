package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;

import java.util.ArrayList;

public class NumberOfPeopleAdapter extends RecyclerView.Adapter<NumberOfPeopleAdapter.NumberOfPeopleViewHolder> {

    private Context mContext;
    private final OnNumberClickListener mOnNumberClickListener;
    private ArrayList<String> mNumberOfPeopleList;
    private int mClickedItem = -10;

    public NumberOfPeopleAdapter(Context context, OnNumberClickListener numberClickListener) {
        this.mContext = context;
        this.mOnNumberClickListener = numberClickListener;
    }

    public interface OnNumberClickListener {
        void onNumberSelected(int position);
    }

    public void setClickedItem(int clickedItem) {
        this.mClickedItem = clickedItem;
    }

    @Override
    public NumberOfPeopleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.number_of_people_list_item, viewGroup, false);
        NumberOfPeopleViewHolder viewHolder = new NumberOfPeopleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberOfPeopleViewHolder holder, int position) {
        holder.numberOfPeopleTextView.setText(String.valueOf(mNumberOfPeopleList.get(position)));
        if (position == mClickedItem) {
            holder.numberOfPeopleTextView.setBackgroundResource(R.drawable.shape_circle_bg_pink);
            holder.numberOfPeopleTextView.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.numberOfPeopleTextView.setBackgroundResource(R.drawable.shape_circle_bg_transparent);
            holder.numberOfPeopleTextView.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        if (mNumberOfPeopleList != null) {
            return mNumberOfPeopleList.size();
        } else {
            return 0;
        }
    }

    public void setNumberOfPeopleList(ArrayList<String> numberOfPeopleList) {
        this.mNumberOfPeopleList = numberOfPeopleList;
        notifyDataSetChanged();
    }

    class NumberOfPeopleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView numberOfPeopleTextView;

        public NumberOfPeopleViewHolder(View itemView) {
            super(itemView);
            numberOfPeopleTextView = itemView.findViewById(R.id.tv_selected_number_of_people);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnNumberClickListener.onNumberSelected(position);
        }
    }
}
