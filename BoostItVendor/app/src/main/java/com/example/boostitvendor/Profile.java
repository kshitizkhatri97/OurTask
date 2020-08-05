package com.example.boostitvendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    SharedPreferences pref;
    TextView fname,lname,mob;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        fname=(TextView)findViewById(R.id.fnamedisp);
        lname = (TextView)findViewById(R.id.lnamedisp);
        mob = (TextView)findViewById(R.id.mobdisp);
        toolbar = findViewById(R.id.profile_toolbar);

        fname.setText(pref.getString("first_name",null));
        lname.setText(pref.getString("last_name",null));
        mob.setText(pref.getString("username",null));
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        edit=(Button)findViewById(R.id.editb);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Profile_editor.class));
            }
        });

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (toolbar != null) {
            toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_signin));
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId()==android.R.id.home)
       {
           finish();
       }
       return super.onOptionsItemSelected(item);
    }

}
