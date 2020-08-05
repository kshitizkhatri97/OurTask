package com.example.boostitvendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile_editor extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    EditText fname,lname,mob;
    Button save;
    SharedPreferences pref;
    int id;
    String f_name,l_name,mob_no,sid,msg,EDITURL="https://bi-stag.herokuapp.com/staff/edit";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        setContentView(R.layout.activity_profile_editor);
        fname = (EditText)findViewById(R.id.editfnamedisp);
        lname = (EditText)findViewById(R.id.editlnamedisp);
        mob = (EditText)findViewById(R.id.editmobdisp);
        toolbar = findViewById(R.id.profedit_toolbar);

        fname.setText(pref.getString("first_name",null));
        lname.setText(pref.getString("last_name",null));
        mob.setText(pref.getString("username",null));

        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        save=(Button)findViewById(R.id.saveb);
        id= pref.getInt("staff_id",0);
        sid = Integer.toString(id);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                f_name = fname.getText().toString().trim();
                l_name = lname.getText().toString().trim();
                mob_no = mob.getText().toString().trim();
                if(f_name==null||l_name==null||mob_no==null)
                {
                    Toast.makeText(Profile_editor.this,"Please fill all columns",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mob_no.length()!=10&&mob_no.length()!=13)
                {
                    Toast.makeText(Profile_editor.this,"Enter a valid phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                sendRequest(f_name,l_name,mob_no,sid);
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void sendRequest(final String first, final String last, final String mobile, final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDITURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            Toast.makeText(Profile_editor.this,json.getString("msg"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(Profile_editor.this,Profile.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Profile_editor.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("staff_id", id);
                params.put("fname", first);
                params.put("lname",last);
                params.put("mob",mobile);

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


}
