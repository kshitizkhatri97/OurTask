package com.example.boostitvendor.Masterloginsection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boostitvendor.MyApplication;
import com.example.boostitvendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Master_orderstatus extends AppCompatActivity {
    TextView t1,t2;
    LinearLayout linearLayout;
    String ord_id,staff,mob,paytype,isdel;
    String REGISTER_URL_ORDER = "https://bi-stag.herokuapp.com/ord?ord_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_orderstatus);
        Intent intent = getIntent();
        ord_id = intent.getStringExtra("ord_id");
        staff = intent.getStringExtra("staff_name");
        mob = intent.getStringExtra("mob");
        paytype = intent.getStringExtra("paytype");
        isdel = intent.getStringExtra("isdel");
        t1 = findViewById(R.id.textView1);
        t2 = findViewById(R.id.textView2);
        linearLayout = findViewById(R.id.linearLayout);
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
                                t2.setText("Staff name: " + staff + "\nMobile: " + mob +
                                        "\n" + paytype + "\n" +isdel+
                                        "\nTotal Amount: ₹" + Double.toString(response.getDouble("amount")));

                            JSONArray products = response.getJSONArray("products");
                            for(int i = 0; i<products.length();i++)
                            {
                                JSONObject temp = products.getJSONObject(i);
                                TextView textView = new TextView(getApplicationContext());
                                textView.setText(String.format("%s - %s", temp.getString("prod_name"), "₹"+Double.toString(temp.getDouble("price")*temp.getDouble("qty"))));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-2);
                                params.topMargin =15;
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextSize(20);
                                textView.setPadding(10,10,10,10);
                                textView.setBackgroundColor(Color.rgb(200,200,200));
                                textView.setLayoutParams(params);
                                linearLayout.addView(textView);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Master_orderstatus.this,error.toString(),Toast.LENGTH_LONG).show();
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
}