package com.borah.manjit.brainshare;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        tv=findViewById(R.id.game_over);
        int a[]=getIntent().getExtras().getIntArray("score");
        tv.setText("Score\n"+a[0]+"/"+a[1]);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,FirstActivity.class));
    }

    public void goToStart(View view){
        MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.click_sound);
        mediaPlayer.start();
        startActivity(new Intent(this,FirstActivity.class));
    }
}
