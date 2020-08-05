package com.example.boostitvendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boostitvendor.Models.shop;
import com.example.boostitvendor.MyApplication;
import com.example.boostitvendor.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    Context mCtx;
    List<shop> shopList;
    private ShopAdapter.OnItemClickListener mListener;
    StringBuffer SShopURL = new StringBuffer("https://bi-stag.herokuapp.com/vend/switchshop?shop_id=");
    Boolean enab;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;

    }
    public ShopAdapter(Context mCtx, List<shop> shopList){
        this.mCtx = mCtx;
        this.shopList = shopList;
    }
    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.shoplayout,parent,false);

        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopViewHolder holder, int position) {
        final shop shop = shopList.get(position);
        holder.t1.setText("ID:" + shop.get_shopid());
        holder.t2.setText(shop.get_shopname());
        if(shop.get_shopservicing() == true)
        {
            holder.s1.setChecked(true);
        }
        else
        {
            holder.s1.setChecked(false);
        }
        enab = shop.get_shopservicing();
        holder.s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SShopURL = new StringBuffer("https://bi-stag.herokuapp.com/vend/switchshop?shop_id=");
                if(!enab == true){
                    enab = !enab;
                    Serviceshop(Integer.toString(shop.get_shopid()),"0");
                    holder.s1.setText("Shop Enabled");
                    //Toast.makeText(mCtx, item.get_id()+" Enabled", Toast.LENGTH_SHORT).show();
                }
                else
                {   enab = !enab;
                    Serviceshop(Integer.toString(shop.get_shopid()),"1");
                    //Toast.makeText(mCtx, item.get_id()+" Disabled", Toast.LENGTH_SHORT).show();
                    holder.s1.setText("Shop Disabled");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2;
        Switch s1;
        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.shopList_id);
            t2 = itemView.findViewById(R.id.shopList_name);
            s1 = itemView.findViewById(R.id.shopList_enable);
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
    public void Serviceshop(final String id,final String enable) {
        SShopURL.append(id);
        SShopURL.append("&servicing="+enable);

        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET,SShopURL.toString(), null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(mCtx, "Shop: "+id+" "+response.getString("Success"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();

                headers.put("pvtkey", "97381f90401474fdbb9354c6ec41aef2c2de7d4ff19dc9898c8b7f416f838bf3");
                return headers;
            }

        };

        MyApplication.getInstance().addToRequestQueue(orderRequest, "abc");
    }

}
