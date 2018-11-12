package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.WorkDay;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkingHoursAdapter extends RecyclerView.Adapter<WorkingHoursAdapter.WorkingHoursViewHolder> {

    private ArrayList<WorkDay> mWorkDaysList;
    private Context mContext;

    public WorkingHoursAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public WorkingHoursViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.working_hours_list_item, viewGroup, false);
        WorkingHoursViewHolder viewHolder = new WorkingHoursViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(WorkingHoursViewHolder holder, int position) {
        String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String currentDayOfTheWeek = simpleDateFormat.format(date);

        if (language != null) {
            if (language.equals("français")) {
                String dayNameFr = mWorkDaysList.get(position).getDayNameFr();
                if (!TextUtils.isEmpty(dayNameFr)) {
                    if (dayNameFr.equalsIgnoreCase(currentDayOfTheWeek)) {
                        holder.dayTextView.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.dayTextView.setTextAppearance(R.style.CurrentDayStyle);
                        holder.dayTextView.setText(String.valueOf(mWorkDaysList.get(position).getDayNameFr()));
                        holder.timeTextView.setTextColor(Color.parseColor("#000000"));
                        try {
                            if (!TextUtils.isEmpty(mWorkDaysList.get(position).getWorkingHours().get(0).trim())) {
                                holder.timeTextView.setText(String.valueOf(mWorkDaysList.get(position).getWorkingHours().get(0).trim()));
                            } else {
                                holder.timeTextView.setText(String.valueOf("Fermé"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            holder.dayTextView.setText(String.valueOf(mWorkDaysList.get(position).getDayNameFr()));
                            if (!TextUtils.isEmpty(mWorkDaysList.get(position).getWorkingHours().get(0).trim())) {
                                holder.timeTextView.setText(String.valueOf(mWorkDaysList.get(position).getWorkingHours().get(0).trim()));
                            } else {
                                holder.timeTextView.setText(String.valueOf("Fermé"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                String dayNameEn = mWorkDaysList.get(position).getDayName();
                if (!TextUtils.isEmpty(dayNameEn)) {
                    if (dayNameEn.equalsIgnoreCase(currentDayOfTheWeek)) {
                        holder.dayTextView.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.dayTextView.setTextAppearance(R.style.CurrentDayStyle);
                        holder.dayTextView.setText(String.valueOf(mWorkDaysList.get(position).getDayName()));
                        holder.timeTextView.setTextColor(Color.parseColor("#000000"));
                        try {
                            if (!TextUtils.isEmpty(mWorkDaysList.get(position).getWorkingHours().get(0).trim())) {
                                holder.timeTextView.setText(String.valueOf(mWorkDaysList.get(position).getWorkingHours().get(0).trim()));
                            } else {
                                holder.timeTextView.setText(String.valueOf("Closed"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            holder.dayTextView.setText(String.valueOf(mWorkDaysList.get(position).getDayName()));
                            if (!TextUtils.isEmpty(mWorkDaysList.get(position).getWorkingHours().get(0).trim())) {
                                holder.timeTextView.setText(String.valueOf(mWorkDaysList.get(position).getWorkingHours().get(0).trim()));
                            } else {
                                holder.timeTextView.setText(String.valueOf("Closed"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mWorkDaysList != null) {
            return mWorkDaysList.size();
        } else {
            return 0;
        }
    }

    public void setWorkDaysList(ArrayList<WorkDay> workDaysList) {
        this.mWorkDaysList = workDaysList;
        notifyDataSetChanged();
    }

    class WorkingHoursViewHolder extends RecyclerView.ViewHolder {

        TextView dayTextView;
        TextView timeTextView;

        public WorkingHoursViewHolder(View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.tv_working_hours_day);
            timeTextView = itemView.findViewById(R.id.tv_working_hours_time);
        }
    }
}
