package com.example.boostitvendor.StaffSection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boostitvendor.Adapters.OrderAdapter;
import com.example.boostitvendor.MainActivity;
import com.example.boostitvendor.Models.Order;
import com.example.boostitvendor.MyApplication;
import com.example.boostitvendor.Profile;
import com.example.boostitvendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersPage extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderAdapter adapter;
    List<Order> orderList;
    TextView txt;

    SharedPreferences pref;
    LinearLayout progbar;
    public String m_Text = "";
    public static final String CHANNEL_ID = "boostit_vendor";
    private static final String CHANNEL_NAME = "Vendor app";
    private static final String CHANNEL_DESC = "Vendor app Notification";
    String timeslot = null;

    StringBuffer OrderURL = new StringBuffer("https://bi-stag.herokuapp.com/staff/orders?shop_id="),
            AddrURL = new StringBuffer("https://bi-stag.herokuapp.com/getaddr?add_id=");

    JSONArray orders;
    int shop_id,staff_id,req_type = 0;
    Button orderbtn,ongoingbtn,deliveredbtn;
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_page);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        staff_id = pref.getInt("staff_id",0);
        shop_id = pref.getInt("shop_id",0);
        //Toast.makeText(this,pref.getString("m_pass","Error"),Toast.LENGTH_SHORT).show();
        refreshLayout = findViewById(R.id.frame_container);
        recyclerView = findViewById(R.id.recyclerView);
        orderbtn = findViewById(R.id.orders);
        progbar = findViewById(R.id.progbar);
        ongoingbtn = findViewById(R.id.ongoing);
        deliveredbtn = findViewById(R.id.delivered);
        recyclerView.hasFixedSize();
        txt = (TextView)findViewById(R.id.helloText);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        //FirebaseMessaging.getInstance().subscribeToTopic("");
        txt.setText("Hello " + pref.getString("name", null));
        //adapter = new OrderAdapter(this,orderList);
        //recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                orderList = new ArrayList<>();
                OrderURL = new StringBuffer("https://bi-stag.herokuapp.com/staff/orders?shop_id=");
                requestOrder(shop_id,staff_id,recyclerView, orderList,req_type);
                progbar.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(false);
            }
        });
        ongoingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_type = 1;
                orderbtn.setEnabled(true);
                ongoingbtn.setEnabled(false);
                deliveredbtn.setEnabled(true);
                orderList = new ArrayList<>();
                OrderURL = new StringBuffer("https://bi-stag.herokuapp.com/staff/orders?shop_id=");
                progbar.setVisibility(View.VISIBLE);
                requestOrder(shop_id,staff_id, recyclerView, orderList,req_type);
            }
        });
        orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_type = 0;
                orderbtn.setEnabled(false);
                ongoingbtn.setEnabled(true);
                deliveredbtn.setEnabled(true);
                orderList = new ArrayList<>();
                OrderURL = new StringBuffer("https://bi-stag.herokuapp.com/staff/orders?shop_id=");
                progbar.setVisibility(View.VISIBLE);
                requestOrder(shop_id,staff_id, recyclerView, orderList,req_type);
            }
        });
        deliveredbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req_type = 2;
                orderbtn.setEnabled(true);
                ongoingbtn.setEnabled(true);
                deliveredbtn.setEnabled(false);
                orderList = new ArrayList<>();
                OrderURL = new StringBuffer("https://bi-stag.herokuapp.com/staff/orders?shop_id=");
                progbar.setVisibility(View.VISIBLE);
                requestOrder(shop_id,staff_id, recyclerView, orderList,req_type);
            }
        });
        progbar.setVisibility(View.VISIBLE);
        requestOrder(shop_id,staff_id, recyclerView, orderList,req_type);
    }

    public void requestOrder(int shop_id,int staff_id, final RecyclerView recyclerView, final List<Order> orderList,final int req_type) {

        OrderURL.append(shop_id+"&staff_id="+staff_id);
        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET, OrderURL.toString(), null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            orders = response.getJSONArray("orders");
                            //Toast.makeText(OrdersPage.this, response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject temp = orders.getJSONObject(i);
                                String address="0";
                                if(!temp.getBoolean("isDelivery")&&!temp.getString("status").equals("Delivered")){
                                    address = Integer.toString(temp.getInt("otp"));
                                    timeslot = temp.getString("slot");
                                }
                                else if(temp.getBoolean("isDelivery"))
                                {
                                    JSONObject addr = temp.getJSONObject("addr");
                                    address =
                                            addr.getString("house") + "," + addr.getString("street")
                                                    + ",\n" + addr.getString("locality") + ",\n" + addr.getString("city")
                                                    + " " + addr.getString("pin") + ",\n" + addr.getString("state");
                                }
                                //Toast.makeText(OrdersPage.this,Integer.toString(temp.getInt("id")), Toast.LENGTH_SHORT).show();

                                if(req_type == 0){
                                    if(temp.getString("status").equals("Received")){
                                        try{
                                            orderList.add(
                                                    new Order(
                                                            Integer.toString(temp.getInt("id")),
                                                            temp.getString("status"),
                                                            temp.getString("cust_name"),
                                                            temp.getString("cust_mob"),
                                                            address,
                                                            temp.getBoolean("isDelivery"),
                                                            timeslot
                                                    )
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }else if(req_type == 1){
                                    if(!temp.getString("status").equals("Received") && !temp.getString("status").equals("Delivered") && !temp.getString("status").equals("Cancelled")){
                                        try{
                                            orderList.add(
                                                    new Order(
                                                            Integer.toString(temp.getInt("id")),
                                                            temp.getString("status"),
                                                            temp.getString("cust_name"),
                                                            temp.getString("cust_mob"),
                                                            address,
                                                            temp.getBoolean("isDelivery"),
                                                            timeslot
                                                    )
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }  else if(req_type ==2){
                                    if(temp.getString("status").equals("Delivered")){
                                        try{
                                            orderList.add(
                                                    new Order(
                                                            Integer.toString(temp.getInt("id")),
                                                            temp.getString("status"),
                                                            temp.getString("cust_name"),
                                                            temp.getString("cust_mob"),
                                                            address,
                                                            temp.getBoolean("isDelivery"),
                                                            timeslot
                                                    )
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                            progbar.setVisibility(View.INVISIBLE);
                            adapter = new OrderAdapter(OrdersPage.this, orderList);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if(orderList.get(position).getDel()) {
                                        String ord_id = orderList.get(position).getOrd_id();
                                        Intent intent = new Intent(OrdersPage.this, OrderStatus.class);
                                        intent.putExtra("ord_id", ord_id);
                                        intent.putExtra("req_type", req_type);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        String ord_id = orderList.get(position).getOrd_id();
                                        Intent intent = new Intent(OrdersPage.this, grabandgoStatus.class);
                                        intent.putExtra("ord_id", ord_id);
                                        intent.putExtra("req_type", req_type);
                                        intent.putExtra("timeslot",orderList.get(position).getSlot());
                                        startActivity(intent);
                                    }
                                    //Toast.makeText(OrdersPage.this,ord_id,Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(OrdersPage.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
//            headers.put("Content-Type", "application/json");
                headers.put("pvtkey", "97381f90401474fdbb9354c6ec41aef2c2de7d4ff19dc9898c8b7f416f838bf3");
                return headers;
            }

        };

        MyApplication.getInstance().addToRequestQueue(orderRequest, "abc");
    }


    public void logOut(View view){
        SharedPreferences pref = getSharedPreferences("user_details",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("username");
        editor.remove("password");
        editor.remove("name");
        editor.remove("staff_id");
        //editor.remove("vend_id");
        editor.remove("first_name");
        editor.remove("last_name");
        editor.remove("shop_id");
        editor.commit();
        startActivity(new Intent(OrdersPage.this, MainActivity.class));

    }
    public void profile(View view){
        startActivity(new Intent(OrdersPage.this, Profile.class));
    }
    /*public void master(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(OrdersPage.this);
        builder.setTitle("Master Login");
        builder.setMessage("Enter master password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
               if(m_Text.matches(pref.getString("m_pass",null)))
                {
                    Toast.makeText(OrdersPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OrdersPage.this,shopdetail_untabbed.class));
                }
                else
                {
                    Toast.makeText(OrdersPage.this, "Please enter the appropriate password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }*/
}

