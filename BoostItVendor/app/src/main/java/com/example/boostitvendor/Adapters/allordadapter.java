package com.example.boostitvendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boostitvendor.Masterloginsection.Master_orderstatus;
import com.example.boostitvendor.Models.allord_date;
import com.example.boostitvendor.Models.orderdet;
import com.example.boostitvendor.R;

import java.util.List;

public class allordadapter extends RecyclerView.Adapter<allordadapter.OrderViewholder> {

    List<allord_date>  orddatelist;
    Context mCtx;

    public allordadapter(Context mCtx, List<allord_date> orddatelist){
        this.mCtx = mCtx;
        this.orddatelist = orddatelist;
    }

    @NonNull
    @Override
    public allordadapter.OrderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.allordlayout,parent,false);

        return new OrderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull allordadapter.OrderViewholder holder, int position) {
        final List<orderdet> orderstaken;
        orderstaken = orddatelist.get(position).get_orders();
        holder.t1.setText(orddatelist.get(position).get_date());
        Orderdeetadp adapter = new Orderdeetadp(mCtx,orddatelist.get(position).get_orders());
        LinearLayoutManager LMM = new LinearLayoutManager(mCtx);
        holder.r1.setLayoutManager(LMM);
        holder.r1.setAdapter(adapter);

        adapter.setOnItemClickListener(new Orderdeetadp.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id;
                id = orderstaken.get(position).get_ord_id();
                String staff = orderstaken.get(position).get_staff_name() +" "+ orderstaken.get(position).get_staff_lname();
                String mob = orderstaken.get(position).get_mob();
                String paytype = "";
                String isdel = "";
                if(orderstaken.get(position).get_paytype() == 1){
                    paytype = "Online Transaction";
                }
                if(orderstaken.get(position).get_paytype() == 2) {
                    paytype = "Cash On Delivery";
                }
                if(orderstaken.get(position).get_isdel()){
                    isdel=("Delivery");
                }
                else
                {
                    isdel = ("Grab n Go");
                }
                Intent intent = new Intent(mCtx, Master_orderstatus.class);
                intent.putExtra("ord_id", id);
                intent.putExtra("staff_name",staff);
                intent.putExtra("mob",mob);
                intent.putExtra("paytype",paytype);
                intent.putExtra("isdel",isdel);

                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orddatelist.size();
    }

    public class OrderViewholder extends RecyclerView.ViewHolder {
        TextView t1;
        RecyclerView r1;
        public OrderViewholder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.date);
            r1 = itemView.findViewById(R.id.daterecycler);


        }
    }
}
