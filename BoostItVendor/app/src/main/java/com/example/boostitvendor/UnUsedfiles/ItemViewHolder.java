package com.example.boostitvendor.UnUsedfiles;

import android.net.sip.SipSession;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boostitvendor.ItemClickListener;
import com.example.boostitvendor.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView txtId,txtname,txtqty,txtprice;
    public Switch swenable;
    public ItemClickListener listner;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtId = itemView.findViewById(R.id.item_id);
        txtname = itemView.findViewById(R.id.Itemname);
        txtqty = itemView.findViewById(R.id.itemqty);
        txtprice = itemView.findViewById(R.id.Price);
        swenable = itemView.findViewById(R.id.enable);

    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.listner = itemClickListener;
    }
}
