package com.example.boostitvendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boostitvendor.Models.Order;
import com.example.boostitvendor.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    private Context mCtx;
    private List<Order> orderList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;

    }
    public OrderAdapter(Context mCtx, List<Order> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
    }



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.t1.setText(order.getOrd_id());
        holder.t2.setText(order.getStatus());
        holder.t3.setText(order.getName());
        holder.t4.setText(order.getMob());
        if(order.getDel()) {
            holder.t6.setText("Delivery");
            holder.t5.setText(order.getAdd());
        }
        else if(!order.getAdd().equals("0"))
        {
            holder.t6.setText("Grab and go");
            holder.t5.setText("OTP:"+order.getAdd()+"\n"+order.getSlot());
        }
        else
        {
            holder.t6.setText("Grab and go");
            holder.t5.setText("Complete");
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView t1,t2,t3,t4,t5,t6;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            t1 = itemView.findViewById(R.id.order_id);
            t2 = itemView.findViewById(R.id.order_status);
            t3 = itemView.findViewById(R.id.cus_name);
            t4 = itemView.findViewById(R.id.cus_phone);
            t5 = itemView.findViewById(R.id.order_address);
            t6 = itemView.findViewById(R.id.ordertype);

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
