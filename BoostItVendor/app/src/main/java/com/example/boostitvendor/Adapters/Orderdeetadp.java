package com.example.boostitvendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boostitvendor.Models.orderdet;
import com.example.boostitvendor.R;

import java.util.List;

public class Orderdeetadp extends RecyclerView.Adapter<Orderdeetadp.OrderdeetViewHolder> {
    List<orderdet> orders;
    Context mCtx;
    private Orderdeetadp.OnItemClickListener mListener;


    public void setOnItemClickListener(Orderdeetadp.OnItemClickListener listener){
        mListener= listener;

    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }




    public Orderdeetadp(Context mCtx, List<orderdet> orders) {
        this.mCtx = mCtx;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderdeetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.orddeet,parent,false);

        return new OrderdeetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderdeetViewHolder holder, int position) {
        orderdet ord = orders.get(position);
        holder.t1.setText("Order ID: "+ord.get_ord_id());
        /*if(ord.get_paytype() == 1){
            holder.t2.setText("Online Transaction");
        }
        if(ord.get_paytype() == 2){
            holder.t2.setText("Cash On Delivery");
        }*/
        holder.t3.setText("Customer name:"+ord.get_cust_name());
        holder.t4.setText("Staff ID:"+ord.get_staff_id());
        //holder.t5.setText("Staff Name:"+ord.get_staff_name() +" "+ ord.get_staff_lname());
        //holder.t7.setText("Customer mob:"+ord.get_mob());
        holder.t8.setText(ord.get_status());
       /* if(ord.get_isdel()){
            holder.t9.setText("Delivery");
        }
        else
        {
            holder.t9.setText("Grab n Go");
        }*/
        holder.t10.setText("Total: ₹" + ord.get_amount());
        if(ord.get_settled()){
            holder.t11.setText("Settled");
        }
        else
        {
            holder.t11.setText("Not settled");
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class OrderdeetViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5,t7,t8,t9,t10,t11;
        public OrderdeetViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.orddeet_id);
            //t2 = itemView.findViewById(R.id.paytype);
            t3 = itemView.findViewById(R.id.cust_namedeet);
            t4 = itemView.findViewById(R.id.staff_id);
            //t5 = itemView.findViewById(R.id.staff_fname);
           // t7 = itemView.findViewById(R.id.mob);
            t8 = itemView.findViewById(R.id.status);
            //t9 = itemView.findViewById(R.id.isdel);
            t10 = itemView.findViewById(R.id.amount);
            t11 = itemView.findViewById(R.id.settled);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
