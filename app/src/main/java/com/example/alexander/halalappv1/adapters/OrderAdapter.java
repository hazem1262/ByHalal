package com.example.alexander.halalappv1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.halalappv1.R;
import com.example.alexander.halalappv1.model.newModels.reservation.details.Product;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private ArrayList<Product> mProducts;
	private Context context;
	public OrderAdapter(ArrayList<Product> mProducts, Context context){
		this.mProducts = mProducts;
		this.context = context;
	}
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(context).inflate(R.layout.order_list_cell, viewGroup, false);

		return new OrderViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		((OrderViewHolder) viewHolder).bindItems(mProducts.get(i));
	}

	@Override
	public int getItemCount() {
		return mProducts.size();
	}
	class OrderViewHolder extends RecyclerView.ViewHolder{
		TextView itemCount;
		TextView itemName;
		TextView itemPrice;
		private OrderViewHolder(@NonNull View itemView){
			super(itemView);
			itemCount = itemView.findViewById(R.id.order_list_count);
			itemName = itemView.findViewById(R.id.itemName);
			itemPrice = itemView.findViewById(R.id.itemPrice);
		}

		private void bindItems(final Product product){
			itemCount.setText(product.getQuantity().toString() + "x");
			itemName.setText(product.getName());
			itemPrice.setText(product.getPrice());
		}
	}
}
