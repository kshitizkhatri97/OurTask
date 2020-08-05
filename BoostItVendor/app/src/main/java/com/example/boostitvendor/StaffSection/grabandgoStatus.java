package com.example.boostitvendor.StaffSection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.android.volley.toolbox.StringRequest;
import com.example.boostitvendor.MyApplication;
import com.example.boostitvendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class grabandgoStatus extends AppCompatActivity {
    String ord_id;
    String REGISTER_URL_ORDER = "https://bi-stag.herokuapp.com/ord?ord_id=";
    String REGISTER_URL_UPDATE = "https://bi-stag.herokuapp.com/vend/track";
    String REGISTER_URL_ACCEPT = "https://bi-stag.herokuapp.com/staff/orders?ord_id=";
    TextView t1,t2,t3;
    LinearLayout linearLayout,progbar;
    Button b,b1;
    SharedPreferences pref;
    int req_type,staff_id,shop_id;
    String slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabandgo_status);
        Intent intent = getIntent();
        ord_id = intent.getStringExtra("ord_id");
        req_type = intent.getIntExtra("req_type",0);
        slot = intent.getStringExtra("timeslot");
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        staff_id = pref.getInt("staff_id",0);
        progbar = findViewById(R.id.progbar);

        shop_id = pref.getInt("shop_id",0);
        t1 = findViewById(R.id.textView1);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        linearLayout = findViewById(R.id.linearLayout);
        b = findViewById(R.id.button);
        b1 = findViewById(R.id.button2);
        if(req_type == 0){
            b.setVisibility(View.VISIBLE);
            b1.setVisibility(View.GONE);
            b.setText("Confirm Order");
        }
        else if(req_type == 1){
            b.setVisibility(View.GONE);
            b1.setVisibility(View.VISIBLE);
        }
        else if(req_type == 2){
            b.setVisibility(View.GONE);
            b1.setVisibility(View.GONE);
        }
        progbar.setVisibility(View.VISIBLE);
        sendOrderRequest(ord_id);
    }
    public void sendOrderRequest(final String order_id) {

        REGISTER_URL_ORDER = REGISTER_URL_ORDER + ord_id;
        JsonObjectRequest orderRequest = new JsonObjectRequest(
                Request.Method.GET,
                REGISTER_URL_ORDER,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            t1.setText(String.format("%s - %s", response.getString("name"), response.getString("mob")));
                            if (!response.getString("status").equals("Delivered")){
                                t2.setText("OTP: " + Integer.toString(response.getInt("otp")));
                                t3.setText("TimeSlot: " + slot);
                            }
                            else
                            {
                                t2.setText("Order Picked up");
                            }
                            String currentStatus = response.getString("status");
                            JSONArray products = response.getJSONArray("products");
                            for(int i = 0; i<products.length();i++)
                            {
                                JSONObject temp = products.getJSONObject(i);
                                TextView textView = new TextView(getApplicationContext());
                                textView.setText(String.format("%s - %s", temp.getString("prod_name"), temp.getString("qty")));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-2);
                                params.topMargin =15;
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextSize(20);
                                textView.setPadding(10,10,10,10);
                                textView.setBackgroundColor(Color.rgb(87,118,242));
                                textView.setLayoutParams(params);
                                linearLayout.addView(textView);
                            }
                            if(req_type == 1){
                                switch (currentStatus)
                                {
                                    case "Confirmed":
                                            b1.setText("Order packed");
                                        break;
                                    case "Ready":
                                        b1.setText("Order Delivered");
                                        break;
                                    case "":
                                        b1.setText("Order Delivered");
                                        break;
                                }
                            }
                            progbar.setVisibility(View.INVISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(grabandgoStatus.this,error.toString(),Toast.LENGTH_LONG).show();
                        progbar.setVisibility(View.INVISIBLE);
                    }
                }){


            /*@Override
            public Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("ord_id",order_id);
                return params;
            }*/

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("pvtkey", "97381f90401474fdbb9354c6ec41aef2c2de7d4ff19dc9898c8b7f416f838bf3");
                return headers;
            }

        };

        MyApplication.getInstance().addToRequestQueue(orderRequest, "abc");
    }
    public void updateStatus(final String status, final String order_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("updatestatus", "Status Updated to" + status);
                        if (status.equals("En Route")) {
                            b1.setText("Order Delivered");
                        } else if (status.equals("Delivered")) {
                            b1.setVisibility(View.GONE);
                            req_type = 2;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(grabandgoStatus.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ord_id", order_id);
                params.put("status", status);
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
    public void acceptOrder(final String staffid, final String order_id) {
        String REGISTER_URL_ACCEPT1 = REGISTER_URL_ACCEPT+order_id+"&staff_id="+staffid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_ACCEPT1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OrderAccept","Order has been accepted");
                        b.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                        b1.setText("Order packed");
                        req_type = 1;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(grabandgoStatus.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ord_id", order_id);
                params.put("staff_id",staffid);
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
    public void orderConfirmClick(View view){
        Log.e("ord_id",ord_id);
        Log.e("staff_id",Integer.toString(staff_id));
        acceptOrder(Integer.toString(staff_id),ord_id);
    }

    public void changeStatusClick(View view){
        String status="";
        if(b1.getText() == "Order packed"){
            status = "En Route";
        }else if(b1.getText() == "Order Delivered"){
            status = "Delivered";
        }
        updateStatus(status,ord_id);
    }

}
