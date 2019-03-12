package com.borah.manjit.brainshare;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
