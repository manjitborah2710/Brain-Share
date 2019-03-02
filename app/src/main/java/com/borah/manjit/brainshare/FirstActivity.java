package com.borah.manjit.brainshare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {

    View splash;
    View mainV;

    TextView tv_app_name;
    final String APP_NAME="Brain Share";

    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    ListView lv;

    Animation animation;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        count=0;

        splash=getLayoutInflater().inflate(R.layout.activity_first,null);
        ImageView  im=splash.findViewById(R.id.start_screen_logo);
        tv_app_name=splash.findViewById(R.id.app_name_tv);

        animation=AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in);
        animation.setDuration(5000);

        im.setAnimation(animation);
        setContentView(splash);

        list=new ArrayList<String>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("gamenames");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> itr=dataSnapshot.getChildren();
                for(DataSnapshot a : itr){
                    list.add(a.getValue(String.class));
                }
                adapter=new CustomAdapter(getApplicationContext(),list);

                mainV=getLayoutInflater().inflate(R.layout.first_activity_main_layout,null);
                animation.setDuration(4000);
                mainV.setAnimation(animation);
                lv=mainV.findViewById(R.id.list_of_games);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s= (String) lv.getItemAtPosition(position);
                        startActivity(new Intent(FirstActivity.this,MainActivity.class).putExtra("game",s));
                    }
                });
                setContentView(mainV);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        count++;
        if(count>1){
            finish();
        }
        else{
            Toast.makeText(this,"press one more time to exit",Toast.LENGTH_LONG).show();
        }
    }
}
