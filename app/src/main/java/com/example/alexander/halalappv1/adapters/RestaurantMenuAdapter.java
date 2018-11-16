package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.newModels.menues.MenuItem;

import java.util.HashMap;
import java.util.List;

public class RestaurantMenuAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mListDataHeader;
    private HashMap<String, List<MenuItem>> mListDataChild;
    private Boolean onlyView = false;

    public RestaurantMenuAdapter(Context context, List<String> listDataHeader, HashMap<String, List<MenuItem>> listDataChild, Boolean onlyView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listDataChild;
        this.onlyView = onlyView;
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(mListDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String menuHeader = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.menu_header_list_item, null);
        }

//        if (isExpanded) {
//            convertView.findViewById(R.id.menu_header_layout).setBackgroundColor(Color.parseColor("#FFFFFF"));
//            convertView.findViewById(R.id.menu_header_line_separator).setVisibility(View.INVISIBLE);
//        } else {
//            convertView.findViewById(R.id.menu_header_layout).setBackgroundColor(Color.parseColor("#F4F4F4"));
//            convertView.findViewById(R.id.menu_header_line_separator).setVisibility(View.VISIBLE);
//        }

        int imageResourceId = isExpanded ? R.drawable.arrow_down : R.drawable.ic_arrow_right_white;
        ImageView arrowImageView = convertView.findViewById(R.id.iv_menu_header_arrow);
        arrowImageView.setImageResource(imageResourceId);

        TextView menuHeaderTextView = convertView.findViewById(R.id.tv_menu_header_header);
        menuHeaderTextView.setText(String.valueOf(menuHeader));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MenuItem menuItem = (MenuItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.menu_child_list_item, null);
        }

        TextView dishNameTextView = convertView.findViewById(R.id.tv_menu_child_dish_name);
        TextView dishPriceTextView = convertView.findViewById(R.id.tv_menu_child_dish_price);
        TextView quantityTextView = convertView.findViewById(R.id.tv_menu_child_quantity);
        View linePinkView = convertView.findViewById(R.id.line_pink);
        ImageView plusImage = convertView.findViewById(R.id.iv_dialog_plus_sign);

        dishNameTextView.setText(String.valueOf(menuItem.getName()));
        dishPriceTextView.setText(String.valueOf(String.valueOf(menuItem.getPrice()) + " " + "€"));
        quantityTextView.setVisibility(View.INVISIBLE);
        linePinkView.setVisibility(View.INVISIBLE);

        if (menuItem.getQuantity() > 0) {
            plusImage.setVisibility(View.INVISIBLE);
            quantityTextView.setVisibility(View.VISIBLE);
            linePinkView.setVisibility(View.VISIBLE);
            quantityTextView.setText(String.valueOf("x" + String.valueOf(menuItem.getQuantity())));
        }else{
            plusImage.setVisibility(View.VISIBLE);
        }

        if (onlyView){
            plusImage.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }


}
