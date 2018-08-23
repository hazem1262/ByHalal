package com.example.alexander.halalappv1.reservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.utils.ConstantsHelper;
import com.example.alexander.halalappv1.utils.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UpcomingReservationsAdapter extends RecyclerView.Adapter<UpcomingReservationsAdapter.UpcomingReservationViewHolder> {


    private Context mContext;
    private final UpcomingListItemClickListener mListItemClickListener;
    private ArrayList<UpComingReservation> mUpComingReservationsList;

    public UpcomingReservationsAdapter(Context context, UpcomingListItemClickListener listItemClickListener) {
        this.mContext = context;
        this.mListItemClickListener = listItemClickListener;
    }

    public interface UpcomingListItemClickListener {
        void onUpcomingItemClick(int position);
    }

    @Override
    public UpcomingReservationViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_reservations_list_item, viewGroup, false);
        UpcomingReservationViewHolder viewHolder = new UpcomingReservationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UpcomingReservationViewHolder holder, int position) {
        Picasso.with(mContext).load(mUpComingReservationsList.get(position).getRestaurant().getImage()).into(holder.restaurantImageImageView);
        String language = SharedPreferencesHelper.getSharedPreferenceString(mContext, ConstantsHelper.KEY_SELECTED_LANGUAGE, null);
        if (language != null) {
            if (language.equals("français")) {
                String[] reservationDateParts = mUpComingReservationsList.get(position).getBookingData().getReservationDate().split("-");
                String reservationDate = reservationDateParts[2] + "/" + reservationDateParts[1];
                String reservationDay = getDayNameFrench(mUpComingReservationsList.get(position).getBookingData().getReservationDayName());
                holder.reservationDateTextView.setText(reservationDay + "\n" + reservationDate);

                String reservationTime = mUpComingReservationsList.get(position).getBookingData().getReservationTime();
                String numberOfPeople = getNumberTextFrench(mUpComingReservationsList.get(position).getBookingData().getNumberOfPeople());

                holder.restaurantNameTextView.setText(mUpComingReservationsList.get(position).getRestaurant().getName());
                holder.reservationDetailsTextView.setText(reservationTime + " - " + mUpComingReservationsList.get(position).getBookingData().getNumberOfPeople() + " personnes");
            } else {
                String[] reservationDateParts = mUpComingReservationsList.get(position).getBookingData().getReservationDate().split("-");
                String reservationDate = reservationDateParts[2] + "/" + reservationDateParts[1];
                String reservationDay = mUpComingReservationsList.get(position).getBookingData().getReservationDayName();
                holder.reservationDateTextView.setText(reservationDay + "\n" + reservationDate);

                String reservationTime = mUpComingReservationsList.get(position).getBookingData().getReservationTime();
                String numberOfPeople = getNumberTextEnglish(mUpComingReservationsList.get(position).getBookingData().getNumberOfPeople());

                holder.restaurantNameTextView.setText(mUpComingReservationsList.get(position).getRestaurant().getName());
                holder.reservationDetailsTextView.setText(reservationTime + " - " + mUpComingReservationsList.get(position).getBookingData().getNumberOfPeople() + " people");
            }
        }
    }

    private String getDayNameFrench(String dayNameEnglish) {
        if (dayNameEnglish.equalsIgnoreCase("Sat")) {
            return "sam";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Sun")) {
            return "dim";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Mon")) {
            return "lun";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Tue")) {
            return "mar";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Wed")) {
            return "mer";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Thu")) {
            return "jeu";
        }
        else if (dayNameEnglish.equalsIgnoreCase("Fri")) {
            return "ven";
        }
        else {
            return "";
        }
    }

    private String getNumberTextEnglish(String number) {
        if (number.equals("1")) {
            return "one";
        }
        else if (number.equals("2")) {
            return "two";
        }
        else if (number.equals("3")) {
            return "three";
        }
        else if (number.equals("4")) {
            return "four";
        }
        else if (number.equals("5")) {
            return "five";
        }
        else if (number.equals("6")) {
            return "six";
        }
        else if (number.equals("7")) {
            return "seven";
        }
        else if (number.equals("8")) {
            return "eight";
        }
        else if (number.equals("9")) {
            return "nine";
        }
        else if (number.equals("10")) {
            return "ten";
        }
        else if (number.equals("11")) {
            return "eleven";
        }
        else if (number.equals("12")) {
            return "twelve";
        }
        else if (number.equals("13")) {
            return "thirteen";
        }
        else if (number.equals("14")) {
            return "fourteen";
        }
        else if (number.equals("15")) {
            return "fifteen";
        }
        else if (number.equals("16")) {
            return "sixteen";
        }
        else if (number.equals("17")) {
            return "seventeen";
        }
        else if (number.equals("18")) {
            return "eighteen";
        }
        else if (number.equals("19")) {
            return "nineteen";
        }
        else if (number.equals("20")) {
            return "twenty";
        }
        else {
            return "";
        }
    }

    private String getNumberTextFrench(String number) {
        if (number.equals("1")) {
            return "un";
        }
        else if (number.equals("2")) {
            return "deux";
        }
        else if (number.equals("3")) {
            return "trois";
        }
        else if (number.equals("4")) {
            return "quatre";
        }
        else if (number.equals("5")) {
            return "cinq";
        }
        else if (number.equals("6")) {
            return "six";
        }
        else if (number.equals("7")) {
            return "sept";
        }
        else if (number.equals("8")) {
            return "huit";
        }
        else if (number.equals("9")) {
            return "neuf";
        }
        else if (number.equals("10")) {
            return "dix";
        }
        else if (number.equals("11")) {
            return "onze";
        }
        else if (number.equals("12")) {
            return "douze";
        }
        else if (number.equals("13")) {
            return "treize";
        }
        else if (number.equals("14")) {
            return "quatorze";
        }
        else if (number.equals("15")) {
            return "quinze";
        }
        else if (number.equals("16")) {
            return "seize";
        }
        else if (number.equals("17")) {
            return "dix-sept";
        }
        else if (number.equals("18")) {
            return "dix-huit";
        }
        else if (number.equals("19")) {
            return "dix-neuf";
        }
        else if (number.equals("20")) {
            return "vingt";
        }
        else {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        if (mUpComingReservationsList != null) {
            return mUpComingReservationsList.size();
        } else {
            return 0;
        }
    }

    public void setUpComingReservationsList(ArrayList<UpComingReservation> upComingReservationsList) {
        this.mUpComingReservationsList = upComingReservationsList;
        notifyDataSetChanged();
    }

    class UpcomingReservationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView restaurantImageImageView;
        TextView reservationDateTextView;
        TextView restaurantNameTextView;
        TextView reservationDetailsTextView;

        public UpcomingReservationViewHolder(View itemView) {
            super(itemView);

            restaurantImageImageView = itemView.findViewById(R.id.iv_upcoming_restaurant_image);
            reservationDateTextView = itemView.findViewById(R.id.tv_upcoming_reservation_date);
            restaurantNameTextView = itemView.findViewById(R.id.tv_upcoming_restaurant_name);
            reservationDetailsTextView = itemView.findViewById(R.id.tv_upcoming_reservation_details);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListItemClickListener.onUpcomingItemClick(position);
        }
    }
}
