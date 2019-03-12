package com.borah.manjit.brainshare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    View splash,mainV;

    //for splash

    ProgressBar pb;
    TextView loadQnTv;
    String game;

    private static final String TAG="mn";
    TextView qn_field;
    TextView[] op;
    int ans;
    String value[];
    FirebaseDatabase database;
    DatabaseReference myRef;
    int totalCount;
    int pos;
    boolean stopIncr;
    boolean enableNext;
    ArrayList<GameClass> questions ;
    Animation animation;
    int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        enableNext=false;

        animation= AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in);
        animation.setDuration(3000);


        mainV=getLayoutInflater().inflate(R.layout.activity_main,null);
        mainV.setAnimation(animation);

        //create splash

        splash=getLayoutInflater().inflate(R.layout.splash_view,null);
        pb=splash.findViewById(R.id.progressBar_loadingscreen);
        pb.setVisibility(View.VISIBLE);

        loadQnTv=splash.findViewById(R.id.loading_questions_tv_splash);
        loadQnTv.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_in));

        setContentView(splash);



        qn_field=mainV.findViewById(R.id.qn);
        op=new TextView[4];
        op[0]=mainV.findViewById(R.id.op1);
        op[1]=mainV.findViewById(R.id.op2);
        op[2]=mainV.findViewById(R.id.op3);
        op[3]=mainV.findViewById(R.id.op4);
        value=new String[4];
        questions=new ArrayList<GameClass>();
        totalCount=0;
        stopIncr=false;
        enableTheOptions(false);



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        game=getIntent().getExtras().getString("game").toLowerCase();
        startGame();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }

    void startGame(){
        pos=0;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                Toast.makeText(getApplicationContext(),"questions added",Toast.LENGTH_LONG).show();

                Iterable<DataSnapshot> itr=dataSnapshot.child("games").child(game).getChildren();
                for(DataSnapshot ds : itr){
                    GameClass obj=ds.getValue(GameClass.class);
                    questions.add(obj);
                }


                fillQuestion(questions);
                pb.setVisibility(View.GONE);
                setContentView(mainV);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    public void fillQuestion(ArrayList<GameClass> qns){

        for(TextView x:op){
            x.setBackground(ContextCompat.getDrawable(this,R.drawable.questions_textview_background));
        }

        if(pos>=qns.size()) {
            stopIncr=true;
            startActivity(new Intent(this,GameOver.class).putExtra("score",new int[]{score,pos}));
            return;
        }

        Log.d("mn",pos+"   "+qns.size());

        qn_field.setText(qns.get(pos).qn);



        value[0]=qns.get(pos).op1;
        value[1]=qns.get(pos).op2;

        value[2]=qns.get(pos).op3;

        value[3]=qns.get(pos).op4;

        ans=qns.get(pos).ans;

        op[0].setText(value[0]);

        op[1].setText(value[1]);

        op[2].setText(value[2]);
        op[3].setText(value[3]);

        enableTheOptions(true);
        enableNext=true;
        invalidateOptionsMenu();
        pos++;
    }


    public void setColor(View v){
        TextView v2=(TextView)v;
        if(!v2.getText().toString().equals(value[ans-1])){
            v2.setBackground(ContextCompat.getDrawable(this,R.drawable.wrong_bg));
//            Toast.makeText(getApplicationContext(),"wrong",Toast.LENGTH_SHORT).show();
        }
        else{
            score++;
        }
        op[ans-1].setBackground(ContextCompat.getDrawable(this,R.drawable.correct_bg));
        enableTheOptions(false);

        enableNext=false;
        invalidateOptionsMenu();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fillQuestion(questions);
            }
        },2000);
    }

    private void enableTheOptions(boolean state){
        for (TextView x : op){
            x.setClickable(state);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.inst:{
                break;
            }
            case R.id.next:{
                if(!stopIncr){
                    fillQuestion(questions);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.next);
        item.setEnabled(enableNext);
        return true;
    }
}
