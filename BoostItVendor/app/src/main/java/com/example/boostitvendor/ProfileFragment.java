package com.example.boostitvendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    Toolbar toolbar;
    SharedPreferences pref;
    TextView fname, lname, mob;
    Button edit;

    public ProfileFragment(){
        //Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        pref = this.getActivity().getSharedPreferences("user_details",MODE_PRIVATE);
        fname=(TextView)view.findViewById(R.id.fnamedisp);
        lname = (TextView)view.findViewById(R.id.lnamedisp);
        mob = (TextView)view.findViewById(R.id.mobdisp);
        toolbar = view.findViewById(R.id.profile_toolbar);

        fname.setText(pref.getString("first_name",null));
        lname.setText(pref.getString("last_name",null));
        mob.setText(pref.getString("username",null));
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        edit=(Button)view.findViewById(R.id.editb);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Profile_editor.class));
            }
        });


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
            toolbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_signin));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
