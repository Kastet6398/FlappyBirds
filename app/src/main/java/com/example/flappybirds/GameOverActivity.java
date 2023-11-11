package com.example.flappybirds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        int score = getIntent().getIntExtra("score", 0);
        Button restart = findViewById(R.id.restart);
        Button share = findViewById(R.id.share);
        restart.setOnClickListener(v -> startActivity(new Intent(this, StartGameActivity.class)));
        share.setOnClickListener(v -> startActivity(new Intent(this, ShareActivity.class).putExtra("score", score)));
        this.<android.widget.TextView>findViewById(R.id.score).setText("Game over.\nYour score: " + score);
        Handler handler = new Handler();
        handler.postDelayed(() -> restart.setVisibility(View.VISIBLE), 1000);
        handler.postDelayed(() -> share.setVisibility(View.VISIBLE), 2000);

        Lib.setScore(score, getExternalFilesDir("MyFiles"));
    }
}