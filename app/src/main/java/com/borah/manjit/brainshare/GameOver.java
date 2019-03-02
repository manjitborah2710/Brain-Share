package com.borah.manjit.brainshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}
