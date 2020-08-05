package com.example.boostitvendor.UnUsedfiles;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.boostitvendor.ItemClickListener;
import com.example.boostitvendor.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderId, txtOrderStatus, txtOrderphone, txtOrderAddress;

    private ItemClickListener itemClickListener;


    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderphone = itemView.findViewById(R.id.order_address);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}