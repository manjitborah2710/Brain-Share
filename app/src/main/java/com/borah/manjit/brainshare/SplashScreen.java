package com.borah.manjit.brainshare;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {
    Button btn_play_quiz;
    Intent intent;
    ImageView iv_brain;
    ImageView[] letters;
    Animation[] animations;
    int val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        btn_play_quiz=findViewById(R.id.play_quiz_btn);
        btn_play_quiz.setOnClickListener(this);
        intent=new Intent(SplashScreen.this,FirstActivity.class);

        iv_brain=findViewById(R.id.iv_brain);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.brain_translate);
        iv_brain.startAnimation(animation);

        Animation animationForBtn=AnimationUtils.loadAnimation(this,R.anim.play_btn_translation);
        btn_play_quiz.startAnimation(animationForBtn);


        letters=new ImageView[10];
        letters[0]=findViewById(R.id.b_1);
        letters[1]=findViewById(R.id.r_1);
        letters[2]=findViewById(R.id.a_1);
        letters[3]=findViewById(R.id.i_1);
        letters[4]=findViewById(R.id.n_1);
        letters[5]=findViewById(R.id.s_1);
        letters[6]=findViewById(R.id.h_1);
        letters[7]=findViewById(R.id.a_2);
        letters[8]=findViewById(R.id.r_2);
        letters[9]=findViewById(R.id.e_1);

        val=200;
        animations=new Animation[10];
        for(int i=9;i>=0;i--){
            animations[i]=AnimationUtils.loadAnimation(this,R.anim.letters_translation);
            animations[i].setDuration(val);
            letters[i].startAnimation(animations[i]);
            val=val+200;
        }




//        Animation animationForLetters=AnimationUtils.loadAnimation(this,R.anim.letters_translation);
//        animationForLetters.setDuration(2000);
//        letters[0].startAnimation(animationForLetters);
//        Animation animationForLetters2=AnimationUtils.loadAnimation(this,R.anim.letters_translation);
//        animationForLetters2.setDuration(2500);
//        letters[1].startAnimation(animationForLetters2);
//        Animation animationForLetters3=AnimationUtils.loadAnimation(this,R.anim.letters_translation);
//        animationForLetters3.setDuration(3000);
//        letters[2].startAnimation(animationForLetters3);






    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.play_quiz_btn:{

                MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.click_sound);
                mediaPlayer.start();
                startActivity(intent);
                break;
            }
            default:{
                break;
            }
        }
    }
}
