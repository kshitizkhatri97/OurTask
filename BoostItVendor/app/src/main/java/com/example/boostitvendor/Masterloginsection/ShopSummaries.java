package com.example.boostitvendor.Masterloginsection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.boostitvendor.Adapters.ItemAdapter;
import com.example.boostitvendor.Adapters.allordadapter;
import com.example.boostitvendor.Adapters.billAdapter;
import com.example.boostitvendor.Models.allord_date;
import com.example.boostitvendor.Models.bill;
import com.example.boostitvendor.Models.item;
import com.example.boostitvendor.Models.orderdet;
import com.example.boostitvendor.MyApplication;
import com.example.boostitvendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopSummaries extends AppCompatActivity {

    SharedPreferences pref;
    int shop_id, vendid,selector = 1;
    StringBuffer ItemURL = new StringBuffer("https://bi-stag.herokuapp.com/vend/shop/"),
            ordersURL = new StringBuffer("https://bi-stag.herokuapp.com/vend/getallorders?shop_id="),
            billURL = new StringBuffer("https://bi-stag.herokuapp.com/settle?shop_id="),
            SettleURL = new StringBuffer("https://bi-stag.herokuapp.com/settle") ;
    String PriceURL = "https://bi-stag.herokuapp.com/prodpri";
    List<allord_date> orderslist;
    List<item> itemlist;
    List<bill> billList;
    JSONArray items, orders, bills;
    RecyclerView recyclerView;
    SwipeRefreshLayout refresh;
    ItemAdapter adapteri;
    billAdapter adapterb;
    allordadapter adaptero;
    Button shopbtn, prodbtn, billbtn;
    TextView infobar,settlebar;
    SearchView mysearchview;
    LinearLayout Llayout,SettleLayout,progbar;
    TextView t1,t2,t3;
    final int UPI_PAYMENT = 0;
    Button b1;
    Double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_login);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        Intent intent = getIntent();
        shop_id =intent.getIntExtra("shop_id",0);
        mysearchview = (androidx.appcompat.widget.SearchView) findViewById(R.id.searchview);
        ItemURL.append(shop_id);
        ordersURL.append(shop_id);
        billURL.append(shop_id);
        infobar = findViewById(R.id.infobar);
        shopbtn = findViewById(R.id.viewbill);
        b1 = findViewById(R.id.billsettle);
        prodbtn = findViewById(R.id.editproduct);
        billbtn = findViewById(R.id.settlebill);
        refresh = findViewById(R.id.frame_container);
        Llayout = findViewById(R.id.summary);
        progbar = findViewById(R.id.progbar);
        SettleLayout = findViewById(R.id.upibtn);
        settlebar = findViewById(R.id.settleamount);
        t1 = findViewById(R.id.net_bal);
        t2 = findViewById(R.id.thisweek);
        t3 = findViewById(R.id.today_rev);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemlist = new ArrayList<>();
                orderslist = new ArrayList<>();
                billList = new ArrayList<>();
                if(selector == 1){
                    infobar.setTextColor(Color.parseColor("#000000"));
                    infobar.setTextSize(15);
                    //mysearchview.setEnabled(true);
                    Llayout.setVisibility(View.INVISIBLE);
                    mysearchview.setVisibility(View.VISIBLE);
                    SettleLayout.setVisibility(View.INVISIBLE);
                    infobar.setText("Click on the item to edit price and use the slider to enable and disable items");
                    requestItem(itemlist, recyclerView);
                }
                if(selector == 2){
                    infobar.setTextColor(Color.parseColor("#FFFFFF"));
                    infobar.setTextSize(15);
                    //mysearchview.setEnabled(false);
                    mysearchview.setVisibility(View.INVISIBLE);
                    SettleLayout.setVisibility(View.INVISIBLE);
                    infobar.setText("List of all orders ongoing and complete");
                    requestallord(orderslist,recyclerView);
                }
                if(selector == 3){
                    //mysearchview.setEnabled(false);
                    Llayout.setVisibility(View.INVISIBLE);
                    SettleLayout.setVisibility(View.VISIBLE);
                    infobar.setText("Please Create a upi request to recieve amount use settle bills to pay " +
                            "any deficits");
                    mysearchview.setVisibility(View.INVISIBLE);
                    requestbill(billList, recyclerView);
                }

                refresh.setRefreshing(false);
            }
        });
        itemlist = new ArrayList<>();
        requestItem(itemlist, recyclerView);
        prodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopbtn.setEnabled(true);
                prodbtn.setEnabled(false);
                billbtn.setEnabled(true);
                selector = 1;
                //mysearchview.setEnabled(true);
                Llayout.setVisibility(View.INVISIBLE);
                SettleLayout.setVisibility(View.INVISIBLE);
                mysearchview.setVisibility(View.VISIBLE);
                infobar.setTextColor(Color.parseColor("#000000"));
                infobar.setTextSize(15);
                infobar.setText("Click on the item to edit price and use the slider to enable and disable items");
                itemlist = new ArrayList<>();
                progbar.setVisibility(View.VISIBLE);
                requestItem(itemlist, recyclerView);


            }
        });
        shopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopbtn.setEnabled(false);
                prodbtn.setEnabled(true);
                billbtn.setEnabled(true);
                selector = 2;
                //mysearchview.setEnabled(false);
                mysearchview.setVisibility(View.INVISIBLE);
                SettleLayout.setVisibility(View.INVISIBLE);
                infobar.setTextColor(Color.parseColor("#000000"));
                infobar.setTextSize(15);
                infobar.setText("List of all orders ongoing and complete");
                itemlist = new ArrayList<>();
                orderslist = new ArrayList<>();
                progbar.setVisibility(View.VISIBLE);
                requestallord(orderslist,recyclerView);

            }
        });
        billbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopbtn.setEnabled(true);
                prodbtn.setEnabled(true);
                billbtn.setEnabled(false);
                //mysearchview.setEnabled(false);
                Llayout.setVisibility(View.INVISIBLE);
                mysearchview.setVisibility(View.INVISIBLE);
                SettleLayout.setVisibility(View.VISIBLE);
                selector = 3;
                infobar.setText("Please Create a upi request to recieve amount use settle bills to pay " +
                        "any deficits");
                //Toast.makeText(ShopSummaries.this, billURL, Toast.LENGTH_SHORT).show();
                billList = new ArrayList<>();
                progbar.setVisibility(View.VISIBLE);
                requestbill(billList,recyclerView);

            }
        });


    }


    public void requestItem(final List<item> itemlist, final RecyclerView recyclerView) {
        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET, ItemURL.toString(), null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            items = response.getJSONArray("products");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject temp = items.getJSONObject(i);
                                try {
                                    Boolean enab = true;
                                    if (temp.getDouble("qty") == 0) {
                                        enab = false;
                                    }
                                    itemlist.add(
                                            new item(
                                                    Integer.toString(temp.getInt("id")),
                                                    temp.getString("product_name"),
                                                    temp.getDouble("qty"),
                                                    temp.getDouble("price"),
                                                    enab,
                                                    Integer.toString(shop_id)
                                            )
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            adapteri = new ItemAdapter(ShopSummaries.this, itemlist);
                            mysearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapteri.getFilter().filter(newText);
                                    return false;
                                }
                            });
                            progbar.setVisibility(View.INVISIBLE);
                            recyclerView.setAdapter(adapteri);


                            adapteri.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    final String id, cuprice;
                                    id = itemlist.get(position).get_id();
                                    cuprice = itemlist.get(position).get_price().toString();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShopSummaries.this);
                                    builder.setTitle("Edit price");
                                    builder.setMessage("Enter neew price (Current price:" + cuprice);

                                    final EditText input = new EditText(ShopSummaries.this);
                                    input.setText(cuprice);
                                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                                    builder.setView(input);

                                    builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String m_Text;
                                            m_Text = input.getText().toString();
                                            if (!m_Text.matches("")) {
                                                PriceChange(id, m_Text);
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
                                }
                            });


                            //Toast.makeText(ShopSummaries.this, itemlist.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ShopSummaries.this, error.toString(), Toast.LENGTH_LONG).show();
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

    public void PriceChange(final String id, final String price) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PriceURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ShopSummaries.this,response,Toast.LENGTH_LONG).show();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            //Toast.makeText(ShopSummaries.this,Integer.toString(json.getInt("Success")),Toast.LENGTH_LONG).show();
                            if (json.getInt("Success") == 1) {
                                Toast.makeText(ShopSummaries.this, "Price changed successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ShopSummaries.this, "Sorry there was an error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShopSummaries.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("prod_id", id);
                params.put("price", price);
                params.put("shop_id", Integer.toString(shop_id));
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

    public void requestallord(final List<allord_date> ordersList, final RecyclerView recyclerView) {
        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET, ordersURL.toString(), null,

                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        Llayout.setVisibility(View.VISIBLE);
                        //Toast.makeText(ShopSummaries.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            String sourceString = "<b>" + "Net Balance:" + "</b> "+"<br>" + Double.toString(response.getDouble("net_balance"))+"</br>";
                            t1.setText(Html.fromHtml(sourceString));
                            String sourceString2 = "<b>" + "Week's revenue:" + "</b> " +"<br>"+ Double.toString(response.getDouble("past_7_days"))+"</br>";
                            t2.setText(Html.fromHtml(sourceString2));
                            String sourceString3 = "<b>" + "Today's revenue:" + "</b> " +"<br>"+ Double.toString(response.getDouble("today_revenue"))+"</br>";
                            t3.setText(Html.fromHtml(sourceString3));
                            //Toast.makeText(ShopSummaries.this, Double.toString(response.getDouble("today_revenue")), Toast.LENGTH_SHORT).show();
                            orders = response.getJSONArray("Allorders");
                            for (int i = 0; i < orders.length(); i++) {
                                JSONObject temp = orders.getJSONObject(i);
                                String date = temp.getString("purchase_date");
                                JSONArray ord = orders.getJSONObject(i).getJSONArray("orders");
                                List<orderdet> orddet = new ArrayList<orderdet>();
                                for(int j = 0; j<ord.length();j++){
                                    JSONObject temp1 = ord.getJSONObject(j).getJSONObject("order");
                                    orderdet x;
                                    orddet.add(
                                           x = new orderdet(
                                                    Integer.toString(temp1.getInt("ord_id")),
                                                    Integer.toString(temp1.getInt("staff_id")),
                                                    temp1.getString("name"),
                                                    temp1.getString("mob"),
                                                    temp1.getString("staff_fname"),
                                                    temp1.getString("staff_lname"),
                                                    temp1.getString("status"),
                                                    Double.toString(temp1.getDouble("amount")),
                                                    temp1.getInt("pay_type"),
                                                    temp1.getBoolean("isdelivery"),
                                                    temp1.getBoolean("settled")
                                            )
                                    );
                                    //Toast.makeText(ShopSummaries.this, Integer.toString(j), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(ShopSummaries.this, x.get_settled().toString(), Toast.LENGTH_SHORT).show();
                                }
                                allord_date x;
                                ordersList.add(
                                        x =new allord_date(
                                                date,
                                                orddet
                                        )
                                );
                                //Toast.makeText(ShopSummaries.this, x.get_orders().get(0).get_amount(), Toast.LENGTH_SHORT).show();
                            }


                            //Toast.makeText(ShopSummaries.this, orders.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progbar.setVisibility(View.INVISIBLE);
                        adaptero = new allordadapter(ShopSummaries.this,ordersList);
                        recyclerView.setAdapter(adaptero);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ShopSummaries.this, error.toString(), Toast.LENGTH_LONG).show();
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
    public void sendSettleRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SettleURL.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(json.getInt("Success") == 1){
                                Toast.makeText(ShopSummaries.this,"Payment complete",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShopSummaries.this,error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("shop_id", Integer.toString(shop_id));
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
    public void requestbill(final List<bill> billList, final RecyclerView recyclerView) {
            JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET, billURL.toString(), null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            bills = response.getJSONArray("bills");
                            //Toast.makeText(ShopSummaries.this, bills.toString(), Toast.LENGTH_SHORT).show();
                            amount = response.getDouble("finamount");
                            if(amount<0){
                                Double amount1 = amount*(-1);
                                settlebar.setTextColor(Color.parseColor("#FF0000"));
                                settlebar.setTextSize(20);
                                settlebar.setText("You need to pay: ₹"+amount1.toString());
                            }
                            else
                            {
                                settlebar.setTextColor(Color.parseColor("#00FF00"));
                                settlebar.setTextSize(20);
                                settlebar.setText("You will receive: ₹"+amount.toString());
                            }

                            for (int i = 0; i < bills.length(); i++) {
                                JSONObject temp = bills.getJSONObject(i);
                                try {

                                    billList.add(
                                              new bill(
                                                    temp.getInt("cust_id"),
                                                    temp.getInt("ord_id"),
                                                    temp.getInt("staff_id"),
                                                    temp.getInt("pay_type"),
                                                    temp.getString("name"),
                                                    temp.getString("purchase_date"),
                                                    temp.getBoolean("isDelivery"),
                                                    temp.getDouble("amount"),
                                                     temp.getDouble("this_trans_value")
                                            )
                                    );

                                    //Toast.makeText(ShopSummaries.this,Integer.toString(billList.size()), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            progbar.setVisibility(View.INVISIBLE);
                            adapterb = new billAdapter(ShopSummaries.this, billList);
                            recyclerView.setAdapter(adapterb);


                            //Toast.makeText(ShopSummaries.this, itemlist.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ShopSummaries.this, error.toString(), Toast.LENGTH_LONG).show();
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
    public void settleUpi(View view){
        String amount = this.amount.toString();
        String note = "Bill Settlement Shop ID:"+shop_id;
        String name = "Karunakara Rai";
        String upiId = "karanrai6@okicici";
        payUsingUpi(amount, upiId, name, note);
    }
    void payUsingUpi(String amount, String upiId, String name, String note) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(ShopSummaries.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(ShopSummaries.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(ShopSummaries.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
                sendSettleRequest();

            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(ShopSummaries.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ShopSummaries.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ShopSummaries.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }



}
