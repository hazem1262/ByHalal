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

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {

    private Context mContext;
    private final OnTimeClickListener mOnTimeClickListener;
    private ArrayList<String> mTimeList;
    private int mClickedItem = -10;

    public TimeAdapter(Context context, OnTimeClickListener onTimeClickListener) {
        this.mContext = context;
        this.mOnTimeClickListener = onTimeClickListener;
    }

    public interface OnTimeClickListener {
        void onTimeSelected(int position);
    }

    public void setClickedItem(int clickedItem) {
        this.mClickedItem = clickedItem;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_list_item, viewGroup, false);
        TimeViewHolder viewHolder = new TimeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        holder.timeTextView.setText(String.valueOf(mTimeList.get(position)));

        if (position == mClickedItem) {
            holder.timeTextView.setBackgroundResource(R.drawable.shape_bg_pink_b_pink_br_8);
            holder.timeTextView.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.timeTextView.setBackgroundResource(R.drawable.shape_bg_white_b_grey_br_4);
            holder.timeTextView.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        if (mTimeList != null) {
            return mTimeList.size();
        } else {
            return 0;
        }
    }

    public void setTimeList(ArrayList<String> timeList) {
        this.mTimeList = timeList;
        notifyDataSetChanged();
    }

    class TimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView timeTextView;

        public TimeViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.tv_selected_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnTimeClickListener.onTimeSelected(position);
        }
    }
}
