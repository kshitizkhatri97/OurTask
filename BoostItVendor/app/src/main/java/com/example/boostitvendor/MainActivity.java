package com.example.boostitvendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.boostitvendor.Masterloginsection.shopdetail_untabbed;
import com.example.boostitvendor.StaffSection.OrdersPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String REGISTER_URL = "https://bi-stag.herokuapp.com/staff/login",
            Token_URL = "https://bi-stag.herokuapp.com/token";
    EditText username, password;
    StringBuffer Forgot_URL = new StringBuffer("https://bi-stag.herokuapp.com/fpwd?email=");
    SharedPreferences pref;
    ProgressBar prog;
    Boolean vendor;
    public static final String CHANNEL_ID = "boostit_vendor";
    private static final String CHANNEL_NAME = "Boosit Vendor app";
    private static final String CHANNEL_DESC = "Boostit Vendor app Notification";
    Intent intent;
    int id,flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText3);
        prog = (ProgressBar)findViewById(R.id.loginprogress);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(MainActivity.this, OrdersPage.class);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
//        intent = new Intent(MainActivity.this,OrderViewHolder.class);
        if(pref.contains("username") && pref.contains("password")){
            flag=1;
            sendRequest(pref.getString("username",null),pref.getString("password",null),button);
            prog.setVisibility(View.VISIBLE);

        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void sendRequest(final String mobile, final String pass, final View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response.substring,Toast.LENGTH_LONG).show();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            prog.setVisibility(View.INVISIBLE);
                            if (json.getBoolean("authenticated") == false) {
                                Toast.makeText(MainActivity.this, json.getString("error"), Toast.LENGTH_LONG).show();
                                flag=0;
                                view.setEnabled(true);
                            }else if(flag == 1){
                                if (json.getBoolean("isVendor")){
                                    startActivity(new Intent(MainActivity.this, shopdetail_untabbed.class));
                                }
                                else
                                    startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Logged in Successfully!", Toast.LENGTH_LONG).show();
                                id = json.getInt("id");
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("username", mobile);
                                editor.putString("password", pass);
                                id = json.getInt("id");
                                editor.putInt("shop_id", json.getInt("shop_id"));
                                if(vendor = json.getBoolean("isVendor")){
                                    editor.putInt("vend_id", id);
                                }
                                else{
                                    editor.putInt("staff_id", id);
                                }

                                editor.putString("last_name", json.getString("lname"));
                                editor.putString("first_name", json.getString("fname"));
                                //editor.putString("m_pass", json.getString("m_access"));
                                //editor.putInt("vend_id",json.getInt("vend_id"));
                                if (json.getString("lname").matches("null")) {
                                    editor.putString("name", json.getString("fname"));
                                    //Toast.makeText(MainActivity.this, (Integer.toString()), Toast.LENGTH_LONG).show();
                                } else {
                                    editor.putString("name", (json.getString("fname") +" "+ json.getString("lname")));
                                    //Toast.makeText(MainActivity.this, (json.getString("fname") + json.getString("lname")), Toast.LENGTH_LONG).show();
                                }
                                editor.commit();
                                //if(!pref.contains("toekn")) {
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (task.isSuccessful()) {
                                                        String token = task.getResult().getToken();
                                                        //Toast.makeText(OrdersPage.this, token, Toast.LENGTH_SHORT).show();
                                                        Log.e("token",token);
                                                        //txt.setText(token);
                                                        sendToken(token);
                                                    } else {
                                                        //Toast.makeText(OrdersPage.this,"Token not generated",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                //}
                                if(vendor){
                                    startActivity(new Intent(MainActivity.this,shopdetail_untabbed.class));
                                }
                                else{
                                    startActivity(intent);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        prog.setVisibility(View.INVISIBLE);
                        view.setEnabled(true);

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("mob", mobile);
                params.put("pwd", pass);
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

    public void sendToken(final String token) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Token_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            //Toast.makeText(MainActivity.this,json.getString("msg"),Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("toekn",token);
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(MainActivity.this,Profile.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("staff_id", Integer.toString(id));
                params.put("token",token);

                return params;
            }

            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("pvtkey", "97381f90401474fdbb9354c6ec41aef2c2de7d4ff19dc9898c8b7f416f838bf3");
                return headers;
            }

        };
        MyApplication.getInstance().addToRequestQueue(stringRequest, "abc");
    }

    public void signInClick(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(user.matches("")||pass.matches("")){
            Toast.makeText(MainActivity.this,"Please fill all columns", Toast.LENGTH_SHORT).show();
            return;
        }
        prog.setVisibility(View.VISIBLE);
        view.setEnabled(false);
        sendRequest(user, pass, view);
    }
    public void forgotRequest(final String email){
        Boolean checkmail;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        checkmail = pat.matcher(email).matches();
        if(!checkmail){
            Toast.makeText(MainActivity.this,"Please enter a valid email",Toast.LENGTH_SHORT).show();
            return;
        }
        Forgot_URL.append(email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Forgot_URL.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response.substring(8,(response.length()-4)),Toast.LENGTH_LONG).show();
                        Toast.makeText(MainActivity.this,"Check your email for the password reset link",Toast.LENGTH_LONG).show(); ;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
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
    public void ForgetClick(View view){
        String user = username.getText().toString();
        if(user.matches("")){
            Toast.makeText(MainActivity.this,"Please fill in the email", Toast.LENGTH_SHORT).show();
            return;
        }
        forgotRequest(user);
    }
}
