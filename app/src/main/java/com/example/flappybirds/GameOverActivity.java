package com.example.flappybirds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class GameOverActivity extends CustomApplication {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        int score = getIntent().getIntExtra("score", 0);
        Lib.setScore(score);
        Button restart = findViewById(R.id.restart);
        Button share = findViewById(R.id.share);
        Button back = findViewById(R.id.back);
        restart.setOnClickListener(v -> startActivity(new Intent(this, StartGameActivity.class)));
        share.setOnClickListener(v -> startActivity(new Intent(this, ShareActivity.class).putExtra("score", score)));
        back.setOnClickListener(v -> startActivity(new Intent(GameOverActivity.this, MainActivity.class)));
        changeScore(score);
        Handler handler = new Handler();
        handler.postDelayed(() -> restart.setVisibility(View.VISIBLE), 500);
        handler.postDelayed(() -> share.setVisibility(View.VISIBLE), 1000);
        handler.postDelayed(() -> back.setVisibility(View.VISIBLE), 1500);
    }

    @Override
    void changeScore(int score) {
        this.<android.widget.TextView>findViewById(R.id.score).setText(String.format(getResources().getString(R.string.game_over), score));
    }
}