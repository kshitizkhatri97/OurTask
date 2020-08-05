package com.example.boostitvendor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.boostitvendor.Models.item;
import com.example.boostitvendor.MyApplication;
import com.example.boostitvendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>
    implements Filterable {

    private Context mCtx;
    private List<item> itemList,itemlistfull;
    private OnItemClickListener mListener;
    private String EnProd = "https://bi-stag.herokuapp.com/switchProd";
    public ItemAdapter(Context mCtx, List<item> itemList) {
        this.mCtx = mCtx;
        this.itemList = itemList;
        this.itemlistfull = new ArrayList<>(itemList);
    }

    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<item> filteredList = new ArrayList<>();
                String charString = constraint.toString().trim();
                if (charString.isEmpty()) {
                    filteredList = itemlistfull;
                }
                else{
                    for(int i=0;i<itemlistfull.size();i++) {
                        if (itemlistfull.get(i).get_name().toLowerCase().trim().contains(charString.toLowerCase())) {
                            filteredList.add(itemlistfull.get(i));
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList = (ArrayList<item>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener= listener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.itemadapter,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final item item = itemList.get(position);
        holder.t1.setText("ID:" + item.get_id());
        holder.t2.setText(item.get_name());
        holder.t3.setText("QTY:" + item.get_qty().toString());
        holder.t4.setText("Price: ₹" + item.get_price().toString());
        holder.s1.setOnCheckedChangeListener(null);
        if (item.get_enable() == true) {
            holder.s1.setChecked(true);
        } else {
            holder.s1.setChecked(false);
        }

        holder.s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    enableChange(item.get_id(), "0", item.get_shopid());
                    //Toast.makeText(mCtx, item.get_id()+" Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    enableChange(item.get_id(), "1", item.get_shopid());
                    //Toast.makeText(mCtx, item.get_id()+" Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView t1,t2,t3,t4;
        Switch s1;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            t1 = itemView.findViewById(R.id.item_id);
            t2 = itemView.findViewById(R.id.Itemname);
            t3 = itemView.findViewById(R.id.itemqty);
            t4 = itemView.findViewById(R.id.Price);
            s1 = itemView.findViewById(R.id.enable);

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
    public void enableChange(final String id, final String enable, final String shopid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EnProd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(mCtx,response,Toast.LENGTH_LONG).show();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            //Toast.makeText(ShopSummaries.this,Integer.toString(json.getInt("Success")),Toast.LENGTH_LONG).show();
                            if(json.getInt("Success") == 1){
                                if(enable.matches("0")){
                                    Toast.makeText(mCtx, id+" Enabled", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(mCtx, id+" Disabled", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(mCtx, "Sorry there was an error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("prod_id",id);
                params.put("switch", enable);
                params.put("shop_id",shopid);
//            params.put(KEY_EMAIL, email);
                return params;
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
//            headers.put("Content-Type", "application/json");
                headers.put("pvtkey", "97381f90401474fdbb9354c6ec41aef2c2de7d4ff19dc9898c8b7f416f838bf3");
                return headers;
            }

        };


        MyApplication.getInstance().addToRequestQueue(stringRequest, "abc");

    }
}
