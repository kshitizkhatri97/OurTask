package com.example.boostitvendor.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boostitvendor.Models.bill;
import com.example.boostitvendor.R;

import java.util.List;

public class billAdapter extends RecyclerView.Adapter<billAdapter.billViewHolder> {

    Context mCtx;
    List<bill> billList;


    public billAdapter(Context mCtx, List<bill> billList) {
        this.mCtx = mCtx;
        this.billList = billList;
    }
    @NonNull
    @Override
    public billViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.billcard,parent,false);
        return new billViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull billViewHolder holder, int position) {
        final bill bill = billList.get(position);
        Double trans;
        holder.t1.setText("Order ID: "+Integer.toString(bill.get_ord_id()));
        holder.t2.setText("Customer ID: "+Integer.toString(bill.get_cust_id()));
        holder.t3.setText("Customer name:" +bill.get_cust_name());
        holder.t4.setText("Amount: ₹"+bill.get_amount().toString());
        holder.t5.setText(bill.get_date());
        if(bill.get_isdel()){
            holder.t6.setText("Delivery");
        }
        else
        {
            holder.t6.setText("Grab n Go");
        }
        if(bill.get_paytype()==1){
            holder.t7.setText("Online transaction");
        }
        else if(bill.get_paytype() == 2){
            holder.t7.setText("Cash on delivery");
        }
        trans = bill.get_trans_value();
        if(bill.get_trans_value()<0){
            trans = trans*(-1);
            holder.t8.setTextColor(Color.parseColor("#FF0000"));
            holder.t8.setText("Transaction cost: ₹"+trans.toString());
        }
        else
        {
            holder.t8.setTextColor(Color.parseColor("#00FF00"));
            holder.t8.setText("Cost received: ₹"+trans.toString());
        }



    }

    @Override
    public int getItemCount() { return billList.size(); }

    public class billViewHolder extends RecyclerView.ViewHolder{
        TextView t1,t2,t3,t4,t5,t6,t7,t8;
        public billViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.bill_ord_id);
            t2 = itemView.findViewById(R.id.bill_cust_id);
            t3 = itemView.findViewById(R.id.bill_cust_name);
            t4 = itemView.findViewById(R.id.bill_amount);
            t5 = itemView.findViewById(R.id.bill_date);
            t6 = itemView.findViewById(R.id.bill_isdel);
            t7 = itemView.findViewById(R.id.bill_paytype);
            t8 = itemView.findViewById(R.id.bill_trans_vaule);
        }
    }
}
