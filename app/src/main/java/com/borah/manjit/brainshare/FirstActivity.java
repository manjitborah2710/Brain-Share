package com.borah.manjit.brainshare;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.cardemulation.HostApduService;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

    View mainV;

    TextView tv_app_name;
    final String APP_NAME="Brain Share";

    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    ListView lv;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CustomProgressDialog dialog=new CustomProgressDialog(this,"Loading....");
        dialog.setCancelable(false);

        dialog.show();



        count=0;
        mainV=getLayoutInflater().inflate(R.layout.first_activity_main_layout,null);
        setContentView(mainV);
        lv=mainV.findViewById(R.id.list_of_games);

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

                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s= (String) lv.getItemAtPosition(position);
                        MediaPlayer mediaPlayer= MediaPlayer.create(FirstActivity.this,R.raw.click_sound);
                        mediaPlayer.start();
                        startActivity(new Intent(FirstActivity.this,MainActivity.class).putExtra("game",s));
                    }
                });

                dialog.cancel();

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
            finishAffinity();
        }
        Toast.makeText(this,"press one more time to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count=0;
            }
        },2000);
    }
}
